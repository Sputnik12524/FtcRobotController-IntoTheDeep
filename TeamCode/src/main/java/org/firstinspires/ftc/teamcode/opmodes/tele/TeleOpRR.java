package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.opmodes.tele.ConstantsOfTeleOp.LiftPositions;
import org.firstinspires.ftc.teamcode.opmodes.tele.ConstantsOfTeleOp.IntakePositions;
import org.firstinspires.ftc.teamcode.opmodes.tele.ConstantsOfTeleOp.ShoulderClawPositions;
import static org.firstinspires.ftc.teamcode.opmodes.tele.ConstantsOfTeleOp.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@TeleOp(name = "TeleOp Road Runner")
@Config
public class TeleOpRR extends LinearOpMode {

    DriveTrainMecanum driveTrain; Shoulder sl; Lift lt; Intake in; Claw cl;

    /// LIFT AND CLAW(for specimens)
    private final ElapsedTime liftTimer = new ElapsedTime();
    LiftPositions posLift = LiftPositions.LIFT_ZERO;
    double targetLiftFSM = 0;
    private boolean stateDpadUp2 = false;
    private boolean stateDpadDown2 = false;
    private boolean stateDpadLeft2 = false;
    private boolean stateDpadRight2 = false;
    private boolean stateLeftStickButton = false;
    private boolean stateRightBumper2 = false; // Claw for specimens


    /// SHOULDER AND CLAW(for samples)
    private final ElapsedTime shoulderTimer = new ElapsedTime();
    ShoulderClawPositions posShoulder = ShoulderClawPositions.START_POSE;
    double shoulderFSM = Shoulder.INITIAL_POSITION;

    private boolean stateA2 = false;
    private boolean stateB2 = false;
    private boolean stateLeftBumper2 = false;

    /// INTAKE
    private final ElapsedTime intakeTimer = new ElapsedTime();
    IntakePositions posIntake = IntakePositions.OUTTAKE_POS;
    double extFSM = Intake.EXTENSION_MIN;
    double flipFSM = Intake.FLIP_OUTTAKE;

    private boolean brushInStatus = false;
    private boolean brushOutStatus = false;
    private boolean stateA1 = false;
    private boolean stateB1 = false;
    private boolean stateRightBumper1 = false;
    private boolean stateLeftBumper1 = false;
    private boolean necessaryFlipPos;

    private boolean stateSensor = false;
    public Intake.Color badColor;

    ///different things
    private boolean initWait = false;

    ///ALL FSM
    private Map<LiftPositions, Supplier<LiftPositions>> liftFSMMap = new HashMap<LiftPositions, Supplier<LiftPositions>>() {{
        put(LiftPositions.LIFT_ZERO, () -> {
            if (gamepad2.dpad_up && !stateDpadUp2) {
                driveTrain.slowMode();
                targetLiftFSM = Lift.POS_HIGH_BASKET;
                return LiftPositions.LIFT_TO_BASKET;
            }
            if (gamepad2.dpad_right && !stateDpadRight2) {
                targetLiftFSM = Lift.POS_SIDE;
                return LiftPositions.LIFT_TO_SIDE;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                return LiftPositions.ZERO_UPDATE;
            }
            return LiftPositions.LIFT_ZERO;
        });
        put(LiftPositions.LIFT_TO_BASKET, () -> {
            if (gamepad2.dpad_down && !stateDpadDown2) {
                driveTrain.standartMode();
                targetLiftFSM = 0;
                return LiftPositions.LIFT_ZERO;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                driveTrain.standartMode();
                return LiftPositions.ZERO_UPDATE;
            }
            return LiftPositions.LIFT_TO_BASKET;
        });
        put(LiftPositions.LIFT_TO_SIDE, () -> {
            if (gamepad2.dpad_up && !stateDpadUp2) {
                driveTrain.slowMode();
                targetLiftFSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                return LiftPositions.LIFT_TO_SPECIMEN_BEFORE;
            }
            if (gamepad2.dpad_left && !stateDpadLeft2) {
                targetLiftFSM = 0;
                return LiftPositions.LIFT_ZERO;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                return LiftPositions.ZERO_UPDATE;
            }
            return LiftPositions.LIFT_TO_SIDE;
        });
        put(LiftPositions.LIFT_TO_SPECIMEN_BEFORE, () -> {
            if (gamepad2.dpad_down && !stateDpadDown2) {
                driveTrain.standartMode();
                targetLiftFSM = Lift.POS_HIGH_SPECIMEN_AFTER;
                return LiftPositions.LIFT_TO_SPECIMEN_AFTER;
            }
            if (gamepad2.dpad_right && !stateDpadRight2) {
                driveTrain.standartMode();
                targetLiftFSM = Lift.POS_SIDE;
                return LiftPositions.LIFT_TO_SIDE;
            }
            if (gamepad2.dpad_left && !stateDpadLeft2) {
                driveTrain.standartMode();
                targetLiftFSM = 0;
                return LiftPositions.LIFT_ZERO;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                driveTrain.standartMode();
                return LiftPositions.ZERO_UPDATE;
            }
            return LiftPositions.LIFT_TO_SPECIMEN_BEFORE;
        });
        put(LiftPositions.LIFT_TO_SPECIMEN_AFTER, () -> {
            if (gamepad2.dpad_up && !stateDpadUp2) {
                driveTrain.slowMode();
                targetLiftFSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                return LiftPositions.LIFT_TO_SPECIMEN_BEFORE;
            }
            if (gamepad2.dpad_right && !stateDpadRight2) {
                targetLiftFSM = Lift.POS_SIDE;
                return LiftPositions.LIFT_TO_SIDE;
            }
            if (gamepad2.dpad_left && !stateDpadLeft2) {
                targetLiftFSM = 0;
                return LiftPositions.LIFT_ZERO;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                return LiftPositions.ZERO_UPDATE;
            }
            return LiftPositions.LIFT_TO_SPECIMEN_AFTER;
        });
        put(LiftPositions.WAIT_UPDATE, () -> {
            if (lt.isMagneting()) {
                lt.resetZero();
                return LiftPositions.LIFT_ZERO;
            }
            if (Math.abs(lt.getError()) <= 0.05) {
                return LiftPositions.ZERO_UPDATE;
            }
            return LiftPositions.WAIT_UPDATE;
        });
        put(LiftPositions.ZERO_UPDATE, () -> {
            targetLiftFSM += 3;
            return LiftPositions.WAIT_UPDATE;
        });
    }};
    private Map<ShoulderClawPositions, Supplier<ShoulderClawPositions>> shoulderFSMMap =
            new HashMap<ShoulderClawPositions, Supplier<ShoulderClawPositions>>() {{
        put(ShoulderClawPositions.START_POSE, () -> {
            if (gamepad2.a && !stateA2 && posIntake == IntakePositions.OUTTAKE_POS && (in.getFlipPositionR() == Intake.FLIP_OUTTAKE)) {
                sl.openSh();
                shoulderFSM = Shoulder.POS_SH_FOR_INTAKE;
                return ShoulderClawPositions.MOVED_TO_INTAKE;
            }
            if (gamepad2.b && stateB2) {
                shoulderTimer.reset();
                shoulderFSM = Shoulder.POS_SH_BASKET;
                return ShoulderClawPositions.MOVING_TO_BASKET_FROM_START;
            }
            return ShoulderClawPositions.START_POSE;
        });
        put(ShoulderClawPositions.MOVED_TO_INTAKE, () -> {
            if (gamepad2.b && !stateB2) {
                shoulderTimer.reset();
                sl.closeSh();
                return ShoulderClawPositions.CLAW_CLOSING_TO_BASKET;
            }
            return ShoulderClawPositions.MOVED_TO_INTAKE;
        });
        put(ShoulderClawPositions.CLAW_CLOSING_TO_BASKET, () -> {
            if (shoulderTimer.milliseconds() >= TIME_CLOSING_CLAW) {
                shoulderTimer.reset();
                shoulderFSM = Shoulder.POS_SH_BASKET;
                return ShoulderClawPositions.MOVING_TO_BASKET;
            }
            return ShoulderClawPositions.CLAW_CLOSING_TO_BASKET;
        });
        put(ShoulderClawPositions.MOVING_TO_BASKET, () -> {
            if (shoulderTimer.milliseconds() >= TIME_SH_TO_BASKET) {
                return ShoulderClawPositions.MOVED_TO_BASKET;
            }
            return ShoulderClawPositions.MOVING_TO_BASKET;
        });
        put(ShoulderClawPositions.MOVING_TO_BASKET_FROM_START, () -> {
            if (shoulderTimer.milliseconds() >= (TIME_SH_TO_BASKET / 2)) {
                return ShoulderClawPositions.MOVED_TO_BASKET;
            }
            return ShoulderClawPositions.MOVING_TO_BASKET_FROM_START;
        });
        put(ShoulderClawPositions.MOVED_TO_BASKET, () -> {
            if (gamepad2.b && !stateB2) {
                sl.openSh();
                return ShoulderClawPositions.CLAW_OPENED;
            }
            if (gamepad2.a && !stateA2) {
                shoulderFSM = Shoulder.INITIAL_POSITION;
                return ShoulderClawPositions.START_POSE;
            }
            return ShoulderClawPositions.MOVED_TO_BASKET;
        });
        put(ShoulderClawPositions.CLAW_OPENED, () -> {
            if (gamepad2.a && !stateA2) {
                shoulderFSM = Shoulder.INITIAL_POSITION;
                return ShoulderClawPositions.START_POSE;
            }
            return ShoulderClawPositions.CLAW_OPENED;
        });
    }};
    private Map<IntakePositions, Supplier<IntakePositions>> intakeFSMMap = new HashMap<IntakePositions, Supplier<IntakePositions>>() {{
        put(IntakePositions.OUTTAKE_POS, () -> {
            if (gamepad1.right_bumper && !stateRightBumper1) {
                intakeTimer.reset();
                extFSM = Intake.EXTENSION_MAX;
                return IntakePositions.EXTENDING_OUT;
            }
            if (in.getExtensionPositionR() >= NECESSARY_EXT_POS) {
                driveTrain.slowMode();
                return IntakePositions.INTAKE_POS;
            }
            return IntakePositions.OUTTAKE_POS;
        });
        put(IntakePositions.EXTENDING_OUT, () -> {
            if (intakeTimer.milliseconds() >= TIME_EXT) {
                intakeTimer.reset();
                flipFSM = Intake.FLIP_INTAKE;
                return IntakePositions.FLIPPING_OUT;
            }
            return IntakePositions.EXTENDING_OUT;
        });
        put(IntakePositions.FLIPPING_OUT, () -> {
            if (intakeTimer.milliseconds() >= FLIP_TIME) {
                in.brushOuttake();
                brushInStatus = false;
                brushOutStatus = true;
                return IntakePositions.BRUSHING_OUT;
            }
            return IntakePositions.FLIPPING_OUT;
        });
        put(IntakePositions.BRUSHING_OUT, () -> {
            if (intakeTimer.milliseconds() >= BRUSHING_OUT_TIME) {
                driveTrain.slowMode();
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                return IntakePositions.INTAKE_POS;
            }
            return IntakePositions.BRUSHING_OUT;
        });
        put(IntakePositions.INTAKE_POS, () -> {
            if (gamepad1.right_bumper && !stateRightBumper1 || ((in.getColorSample() != badColor) && (in.getColorSample() != Intake.Color.NONE) && stateSensor)) { // #НеБойсяПж
                driveTrain.standartMode();
                intakeTimer.reset();
                flipFSM = Intake.FLIP_OUTTAKE;
                in.brushIntake();
                brushInStatus = true;
                brushOutStatus = false;
                return IntakePositions.FLIPPING_IN;
            } else if ((in.getExtensionPositionR() < NECESSARY_EXT_POS) && (in.getFlipPositionR() == Intake.FLIP_OUTTAKE)) {
                driveTrain.standartMode();
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                return IntakePositions.OUTTAKE_POS;
            } else if ((in.getColorSample() == badColor) && stateSensor) {
                intakeTimer.reset();
                in.brushOuttake();
                brushInStatus = false;
                brushOutStatus = true;
                posIntake = IntakePositions.REMOVE_TRASH;
            } else if (flipFSM == Intake.FLIP_OUTTAKE) {
                return IntakePositions.INTAKE_POS_FOR_FLIP;
            }
            return IntakePositions.INTAKE_POS;
        });
        put(IntakePositions.INTAKE_POS_FOR_FLIP, () -> {
            if (gamepad1.right_bumper && !stateRightBumper1) { // #НеБойсяПж
                driveTrain.standartMode();
                intakeTimer.reset();
                flipFSM = Intake.FLIP_OUTTAKE;
                in.brushIntake();
                brushInStatus = true;
                brushOutStatus = false;
                return IntakePositions.FLIPPING_IN;
            } else if ((in.getExtensionPositionR() < NECESSARY_EXT_POS) && (in.getFlipPositionR() == Intake.FLIP_OUTTAKE)) {
                driveTrain.standartMode();
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                return IntakePositions.OUTTAKE_POS;
            } else if (flipFSM == Intake.FLIP_INTAKE) {
                intakeTimer.reset();
                return IntakePositions.FLIPPING_OUT;
            }
            return IntakePositions.INTAKE_POS_FOR_FLIP;
        });
        put(IntakePositions.REMOVE_TRASH, () -> {
            if (intakeTimer.milliseconds() >= BRUSH_TIME) {
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                return IntakePositions.INTAKE_POS;
            }
            return IntakePositions.REMOVE_TRASH;
        });
        put(IntakePositions.FLIPPING_IN, () -> {
            if (intakeTimer.milliseconds() >= FLIP_TIME) {
                intakeTimer.reset();
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                extFSM = Intake.EXTENSION_MIN;
                return IntakePositions.EXTENDING_IN;
            }
            return IntakePositions.FLIPPING_IN;
        });
        put(IntakePositions.EXTENDING_IN, () -> {
            if (intakeTimer.milliseconds() >= TIME_EXT) {
                return IntakePositions.OUTTAKE_POS;
            }
            return IntakePositions.EXTENDING_IN;
        });

    }};

    public void liftFSM() {
        posLift = liftFSMMap.get(posLift).get();
    }
    public void shoulderFSM() {
        posShoulder = shoulderFSMMap.get(posShoulder).get();
    }
    public void intakeFSM() {
        posIntake = intakeFSMMap.get(posIntake).get();
    }


    @Override
    public void runOpMode() {
        driveTrain = new DriveTrainMecanum(hardwareMap, this);
        lt = new Lift(this);
        sl = new Shoulder(this);
        in = new Intake(this);
        cl = new Claw(this);

        necessaryFlipPos = true;

        sl.closeSh();
        cl.openLift();
        sl.shoulderPosition(Shoulder.INITIAL_POSITION);
        in.extensionPosition(Intake.EXT_START_POS);
        in.flipPosition(Intake.FLIP_OUTTAKE);
        lt.resetZero();
        liftTimer.reset();
        shoulderTimer.reset();
        intakeTimer.reset();

        lt.liftMotorPowerDriver.start();

        driveTrain.standartMode();
        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        PoseStorage.currentPose = driveTrain.getPoseEstimate();
        driveTrain.setPoseEstimate(PoseStorage.currentPose);

        /// Alliance selection
        while (opModeInInit()) {
            while (!initWait && opModeIsActive()) {
                if (gamepad1.x) { //Синий альянс
                    badColor = Intake.Color.RED;
                    telemetry.addLine("СИНИЙ АЛЬЯНС");
                    telemetry.update();
                    initWait = true;
                }
                else if (gamepad1.b) { //Красный альянс
                    badColor = Intake.Color.BLUE;
                    telemetry.addLine("КРАСНЫЙ АЛЬЯНС");
                    telemetry.update();
                    initWait = true;
                }
                else if (gamepad1.a) { //Для тестов
                    badColor = null;
                    telemetry.addLine("БЕЗ АЛЬЯНСА");
                    telemetry.update();
                    initWait = true;
                } else {
                    telemetry.addLine("НАЖМИТЕ КНОПКУ ДЛЯ ВЫБОРА АЛЬЯНСА");
                    telemetry.update();
                }
            }
        }

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {

            /// DriveTrain ALL:
            double w_target = gamepad1.left_trigger - gamepad1.right_trigger;
            double w_real = driveTrain.getExternalHeadingVelocity();
            if (Math.abs(w_real) < 1) w_real = 0;
            double rotate = CORRECTION_COEF * (w_target - w_real * VELO_SCALE_COEF) + w_target;
            driveTrain.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y * DriveTrainMecanum.multiplier,
                            gamepad1.left_stick_x * DriveTrainMecanum.multiplier,
                            rotate * DriveTrainMecanum.multiplier * DriveTrainMecanum.TURN_COEF
                    )
            );

            if (gamepad1.dpad_left) driveTrain.resetIMU();
            if (gamepad1.left_bumper && !stateLeftBumper1) driveTrain.switchSlowMode();
            stateLeftBumper1 = gamepad1.left_bumper;


            ///Lift FSM
            liftFSM();
            lt.setTarget(targetLiftFSM);
            stateDpadUp2 = gamepad2.dpad_up;
            stateDpadDown2 = gamepad2.dpad_down;
            stateDpadLeft2 = gamepad2.dpad_left;
            stateDpadRight2 = gamepad2.dpad_right;
            stateLeftStickButton = gamepad2.left_bumper;

            if (gamepad2.right_bumper && !stateRightBumper2) cl.switchPositionLift();
            stateRightBumper2 = gamepad2.right_bumper;


            ///Shoulder and claw FSM:
            shoulderFSM();
            stateA2 = gamepad2.a;
            stateB2 = gamepad2.b;
            sl.shoulderPosition(shoulderFSM);

            if (gamepad2.left_bumper && !stateLeftBumper2) sl.switchPositionShoulder();
            stateLeftBumper2 = gamepad2.left_bumper;


            ///Intake FSM:
            intakeFSM();
            in.extensionPosition(extFSM);
            in.flipPosition(flipFSM);
            stateRightBumper1 = gamepad1.right_bumper;

            /// Manual control:
            //Extension:
            extFSM += -gamepad1.right_stick_y * Intake.EXT_K * Intake.EXTENSION_STEP;

            //Brushes:
            if (gamepad1.a && !brushInStatus && !stateA1) {
                in.brushIntake();
                brushInStatus = true;
                brushOutStatus = false;

            } else if (gamepad1.a && brushInStatus && !stateA1) {
                in.brushStop();
                brushInStatus = false;
            }
            if (gamepad1.b && !brushOutStatus && !stateB1) {
                in.brushOuttake();
                brushOutStatus = true;
                brushInStatus = false;
            } else if (gamepad1.b && brushOutStatus && !stateB1) {
                in.brushStop();
                brushOutStatus = false;
            }
            stateA1 = gamepad1.a;
            stateB1 = gamepad1.b;

            //flip
            if (gamepad1.y) flipFSM = Intake.FLIP_OUTTAKE;
            if (gamepad1.x) flipFSM = Intake.FLIP_INTAKE;

            //Color Sensor
            if (gamepad1.dpad_up) stateSensor = true;
            else if (gamepad1.dpad_down) stateSensor = false;


            /// Telemetry
            telemetry.addLine(String.join(" ","УПРАВЛЕНИЕ НЕ ДАМ", "САНЕЧКА, СБРОС НУЛЯ", "НА КНОПКУ ЛЕВОГО СТИКА!!"));

            telemetry.addData("EXTENSION POS", in.getExtensionPositionR());

            telemetry.addData("Color:", in.getColorSample());
            telemetry.addData("Hue:", in.getHue());
            telemetry.addData("Saturation:", in.getSaturation());
            telemetry.addData("Value:", in.getValue());

            telemetry.addData("State FSM Lift:", posLift);
            telemetry.addData("State FSM Shoulder:", posShoulder);
            telemetry.addData("State FSM Intake", posIntake);

            telemetry.update();

            /// Dashboard telemetry
            FtcDashboard.getInstance().getTelemetry().addData("error dt:", w_target - w_real);
            FtcDashboard.getInstance().getTelemetry().addData("w_target", w_target);
            FtcDashboard.getInstance().getTelemetry().addData("w_real", w_real);
            FtcDashboard.getInstance().getTelemetry().addData("rotate", rotate);
            FtcDashboard.getInstance().getTelemetry().update();
        }
        lt.liftMotorPowerDriver.interrupt();
    }
    public static class PoseStorage {
        public static Pose2d currentPose = new Pose2d();
    }
}