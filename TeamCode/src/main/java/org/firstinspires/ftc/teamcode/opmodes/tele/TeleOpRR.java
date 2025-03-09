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

import static org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum.turnCoef;
import static org.firstinspires.ftc.teamcode.opmodes.tele.ConstantsOfTeleOp.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@TeleOp(name = "TeleOp Road Runner")
@Config
public class TeleOpRR extends LinearOpMode {

    DriveTrainMecanum driveTrain;
    Shoulder sl;
    Lift lt;
    Intake in;
    Claw cl;

    /// LIFT AND CLAW(for specimens)
    private final ElapsedTime liftTimer = new ElapsedTime();
    LiftStates posLift = LiftStates.LIFT_ZERO;
    double targetLiftFSM = 0;
    private boolean stateDpadUp2 = false;
    private boolean stateDpadDown2 = false;
    private boolean stateDpadLeft2 = false;
    private boolean stateDpadRight2 = false;
    private boolean stateLeftStickButton = false;
    private boolean stateRightBumper2 = false; // Claw for specimens


    /// SHOULDER AND CLAW(for samples)
    private final ElapsedTime shoulderTimer = new ElapsedTime();
    ShoulderClawStates posShoulder = ShoulderClawStates.START_POSE;
    double shoulderFSM = Shoulder.SH_POS_INIT;

    private boolean stateA2 = false;
    private boolean stateB2 = false;
    private boolean stateLeftBumper2 = false;

    /// INTAKE
    private final ElapsedTime intakeTimer = new ElapsedTime();
    IntakeStates posIntake = IntakeStates.FOLDED_POS;
    double extFSM = Intake.EXT_POS_MIN;
    double flipFSM = Intake.FLIP_POS_FOR_OUTTAKE;

    private boolean brushInStatus = false;
    private boolean brushOutStatus = false;
    private boolean stateA1 = false;
    private boolean stateB1 = false;
    private boolean stateRightBumper1 = false;
    private boolean stateLeftBumper1 = false;

    private boolean stateSensor = false;
    public Intake.Color badColor;

    ///different things
    private boolean initWait = false;

    ///ALL FSM
    private final Map<LiftStates, Supplier<LiftStates>> liftFSMMap = new HashMap<LiftStates, Supplier<LiftStates>>() {{
        put(LiftStates.LIFT_ZERO, () -> {
            if (gamepad2.dpad_up && !stateDpadUp2) {
                driveTrain.slowMode();
                targetLiftFSM = Lift.POS_HIGH_BASKET;
                return LiftStates.LIFT_TO_BASKET;
            }
            if (gamepad2.dpad_right && !stateDpadRight2) {
                targetLiftFSM = Lift.POS_SIDE;
                return LiftStates.LIFT_TO_SIDE;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                return LiftStates.ZERO_UPDATE;
            }
            return LiftStates.LIFT_ZERO;
        });
        put(LiftStates.LIFT_TO_BASKET, () -> {
            if (gamepad2.dpad_down && !stateDpadDown2) {
                driveTrain.standartMode();
                targetLiftFSM = 0;
                return LiftStates.LIFT_ZERO;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                driveTrain.standartMode();
                return LiftStates.ZERO_UPDATE;
            }
            return LiftStates.LIFT_TO_BASKET;
        });
        put(LiftStates.LIFT_TO_SIDE, () -> {
            if (gamepad2.dpad_up && !stateDpadUp2) {
                driveTrain.slowMode();
                targetLiftFSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                return LiftStates.LIFT_TO_SPECIMEN_BEFORE;
            }
            if (gamepad2.dpad_left && !stateDpadLeft2) {
                targetLiftFSM = 0;
                return LiftStates.LIFT_ZERO;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                return LiftStates.ZERO_UPDATE;
            }
            return LiftStates.LIFT_TO_SIDE;
        });
        put(LiftStates.LIFT_TO_SPECIMEN_BEFORE, () -> {
            if (gamepad2.dpad_down && !stateDpadDown2) {
                driveTrain.standartMode();
                targetLiftFSM = Lift.POS_HIGH_SPECIMEN_AFTER;
                return LiftStates.LIFT_TO_SPECIMEN_AFTER;
            }
            if (gamepad2.dpad_right && !stateDpadRight2) {
                driveTrain.standartMode();
                targetLiftFSM = Lift.POS_SIDE;
                return LiftStates.LIFT_TO_SIDE;
            }
            if (gamepad2.dpad_left && !stateDpadLeft2) {
                driveTrain.standartMode();
                targetLiftFSM = 0;
                return LiftStates.LIFT_ZERO;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                driveTrain.standartMode();
                return LiftStates.ZERO_UPDATE;
            }
            return LiftStates.LIFT_TO_SPECIMEN_BEFORE;
        });
        put(LiftStates.LIFT_TO_SPECIMEN_AFTER, () -> {
            if (gamepad2.dpad_up && !stateDpadUp2) {
                driveTrain.slowMode();
                targetLiftFSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                return LiftStates.LIFT_TO_SPECIMEN_BEFORE;
            }
            if (gamepad2.dpad_right && !stateDpadRight2) {
                targetLiftFSM = Lift.POS_SIDE;
                return LiftStates.LIFT_TO_SIDE;
            }
            if (gamepad2.dpad_left && !stateDpadLeft2) {
                targetLiftFSM = 0;
                return LiftStates.LIFT_ZERO;
            }
            if (gamepad2.left_stick_button && !stateLeftStickButton) {
                return LiftStates.ZERO_UPDATE;
            }
            return LiftStates.LIFT_TO_SPECIMEN_AFTER;
        });
        put(LiftStates.WAIT_UPDATE, () -> {
            if (lt.isMagneting()) {
                lt.resetZero();
                return LiftStates.LIFT_ZERO;
            }
            if (Math.abs(lt.getError()) <= 0.05) {
                return LiftStates.ZERO_UPDATE;
            }
            return LiftStates.WAIT_UPDATE;
        });
        put(LiftStates.ZERO_UPDATE, () -> {
            targetLiftFSM += 3;
            return LiftStates.WAIT_UPDATE;
        });
    }};
    private final Map<ShoulderClawStates, Supplier<ShoulderClawStates>> shoulderFSMMap =
            new HashMap<ShoulderClawStates, Supplier<ShoulderClawStates>>() {{
        put(ShoulderClawStates.START_POSE, () -> {
            if (gamepad2.a && !stateA2 && posIntake == IntakeStates.FOLDED_POS && (in.getFlipPositionR() == Intake.FLIP_POS_FOR_OUTTAKE)) {
                sl.openSh();
                shoulderFSM = Shoulder.SH_POS_TO_INTAKE;
                return ShoulderClawStates.MOVED_TO_INTAKE;
            }
            if (gamepad2.b && stateB2) {
                shoulderTimer.reset();
                shoulderFSM = Shoulder.SH_POS_TO_BASKET;
                return ShoulderClawStates.MOVING_TO_BASKET_FROM_START;
            }
            return ShoulderClawStates.START_POSE;
        });
        put(ShoulderClawStates.MOVED_TO_INTAKE, () -> {
            if (gamepad2.b && !stateB2) {
                shoulderTimer.reset();
                sl.closeSh();
                return ShoulderClawStates.CLAW_CLOSING_TO_BASKET;
            }
            return ShoulderClawStates.MOVED_TO_INTAKE;
        });
        put(ShoulderClawStates.CLAW_CLOSING_TO_BASKET, () -> {
            if (shoulderTimer.milliseconds() >= CLAW_CLOSING_TIME) {
                shoulderTimer.reset();
                shoulderFSM = Shoulder.SH_POS_TO_BASKET;
                return ShoulderClawStates.MOVING_TO_BASKET;
            }
            return ShoulderClawStates.CLAW_CLOSING_TO_BASKET;
        });
        put(ShoulderClawStates.MOVING_TO_BASKET, () -> {
            if (shoulderTimer.milliseconds() >= SH_TO_BASKET_TIME) {
                return ShoulderClawStates.MOVED_TO_BASKET;
            }
            return ShoulderClawStates.MOVING_TO_BASKET;
        });
        put(ShoulderClawStates.MOVING_TO_BASKET_FROM_START, () -> {
            if (shoulderTimer.milliseconds() >= (SH_TO_BASKET_TIME / 2)) {
                return ShoulderClawStates.MOVED_TO_BASKET;
            }
            return ShoulderClawStates.MOVING_TO_BASKET_FROM_START;
        });
        put(ShoulderClawStates.MOVED_TO_BASKET, () -> {
            if (gamepad2.b && !stateB2) {
                sl.openSh();
                return ShoulderClawStates.CLAW_OPENED;
            }
            if (gamepad2.a && !stateA2) {
                shoulderFSM = Shoulder.SH_POS_INIT;
                return ShoulderClawStates.START_POSE;
            }
            return ShoulderClawStates.MOVED_TO_BASKET;
        });
        put(ShoulderClawStates.CLAW_OPENED, () -> {
            if (gamepad2.a && !stateA2) {
                shoulderFSM = Shoulder.SH_POS_INIT;
                return ShoulderClawStates.START_POSE;
            }
            return ShoulderClawStates.CLAW_OPENED;
        });
    }};
    private final Map<IntakeStates, Supplier<IntakeStates>> intakeFSMMap = new HashMap<IntakeStates, Supplier<IntakeStates>>() {{
        put(IntakeStates.FOLDED_POS, () -> {
            if (gamepad1.right_bumper && !stateRightBumper1) {
                intakeTimer.reset();
                extFSM = Intake.EXT_POS_MAX;
                return IntakeStates.EXTENDING_OUT;
            }
            if (in.getExtensionPositionR() >= NECESSARY_EXT_POS) {
                driveTrain.slowMode();
                return IntakeStates.UNFOLDED_POS;
            }
            return IntakeStates.FOLDED_POS;
        });
        put(IntakeStates.EXTENDING_OUT, () -> {
            if (intakeTimer.milliseconds() >= EXT_TIME) {
                intakeTimer.reset();
                flipFSM = Intake.FLIP_POS_FOR_TAKE;
                return IntakeStates.FLIPPING_OUT;
            }
            return IntakeStates.EXTENDING_OUT;
        });
        put(IntakeStates.FLIPPING_OUT, () -> {
            if (intakeTimer.milliseconds() >= FLIP_TIME) {
                in.brushOuttake();
                brushInStatus = false;
                brushOutStatus = true;
                return IntakeStates.BRUSHING_OUT;
            }
            return IntakeStates.FLIPPING_OUT;
        });
        put(IntakeStates.BRUSHING_OUT, () -> {
            if (intakeTimer.milliseconds() >= BRUSHING_OUT_TIME) {
                driveTrain.slowMode();
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                return IntakeStates.UNFOLDED_POS;
            }
            return IntakeStates.BRUSHING_OUT;
        });
        put(IntakeStates.UNFOLDED_POS, () -> {
            if (gamepad1.right_bumper && !stateRightBumper1 || ((in.getColorSample() != badColor) && (in.getColorSample() != Intake.Color.NONE) && stateSensor)) { // #НеБойсяПж
                driveTrain.standartMode();
                intakeTimer.reset();
                flipFSM = Intake.FLIP_POS_FOR_OUTTAKE;
                in.brushIntake();
                brushInStatus = true;
                brushOutStatus = false;
                return IntakeStates.FLIPPING_IN;
            } else if ((in.getExtensionPositionR() < NECESSARY_EXT_POS) && (in.getFlipPositionR() == Intake.FLIP_POS_FOR_OUTTAKE)) {
                driveTrain.standartMode();
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                return IntakeStates.FOLDED_POS;
            } else if ((in.getColorSample() == badColor) && stateSensor) {
                intakeTimer.reset();
                in.brushOuttake();
                brushInStatus = false;
                brushOutStatus = true;
                posIntake = IntakeStates.REMOVE_TRASH;
            } else if (flipFSM == Intake.FLIP_POS_FOR_OUTTAKE) {
                return IntakeStates.UNFOLDED_POS_FOR_FLIP;
            }
            return IntakeStates.UNFOLDED_POS;
        });
        put(IntakeStates.UNFOLDED_POS_FOR_FLIP, () -> {
            if (gamepad1.right_bumper && !stateRightBumper1) { /// #НеБойсяПж
                driveTrain.standartMode();
                intakeTimer.reset();
                flipFSM = Intake.FLIP_POS_FOR_OUTTAKE;
                in.brushIntake();
                brushInStatus = true;
                brushOutStatus = false;
                return IntakeStates.FLIPPING_IN;
            } else if ((in.getExtensionPositionR() < NECESSARY_EXT_POS) && (in.getFlipPositionR() == Intake.FLIP_POS_FOR_OUTTAKE)) {
                driveTrain.standartMode();
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                return IntakeStates.FOLDED_POS;
            } else if (flipFSM == Intake.FLIP_POS_FOR_TAKE) {
                intakeTimer.reset();
                return IntakeStates.FLIPPING_OUT;
            }
            return IntakeStates.UNFOLDED_POS_FOR_FLIP;
        });
        put(IntakeStates.REMOVE_TRASH, () -> {
            if (intakeTimer.milliseconds() >= BRUSH_TIME) {
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                return IntakeStates.UNFOLDED_POS;
            }
            return IntakeStates.REMOVE_TRASH;
        });
        put(IntakeStates.FLIPPING_IN, () -> {
            if (intakeTimer.milliseconds() >= FLIP_TIME) {
                intakeTimer.reset();
                in.brushStop();
                brushInStatus = false;
                brushOutStatus = false;
                extFSM = Intake.EXT_POS_MIN;
                return IntakeStates.EXTENDING_IN;
            }
            return IntakeStates.FLIPPING_IN;
        });
        put(IntakeStates.EXTENDING_IN, () -> {
            if (intakeTimer.milliseconds() >= EXT_TIME) {
                return IntakeStates.FOLDED_POS;
            }
            return IntakeStates.EXTENDING_IN;
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

        sl.closeSh();
        cl.openLift();
        sl.shoulderPosition(Shoulder.SH_POS_INIT);
        in.extensionPosition(Intake.EXT_POS_INIT);
        in.flipPosition(Intake.FLIP_POS_FOR_OUTTAKE);
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
                if (gamepad1.x) { //Blue alliance
                    badColor = Intake.Color.RED;
                    telemetry.addLine("СИНИЙ АЛЬЯНС");
                    telemetry.update();
                    initWait = true;
                }
                else if (gamepad1.b) { //Red alliance
                    badColor = Intake.Color.BLUE;
                    telemetry.addLine("КРАСНЫЙ АЛЬЯНС");
                    telemetry.update();
                    initWait = true;
                }
                else if (gamepad1.a) { //For tests
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
                            rotate * DriveTrainMecanum.multiplier * turnCoef
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
            extFSM += -gamepad1.right_stick_y * Intake.EXT_SPEED_COEF * Intake.EXTENSION_STEP;

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
            if (gamepad1.y) flipFSM = Intake.FLIP_POS_FOR_OUTTAKE;
            if (gamepad1.x) flipFSM = Intake.FLIP_POS_FOR_TAKE;

            //Color Sensor
            if (gamepad1.dpad_up) stateSensor = true;
            else if (gamepad1.dpad_down) stateSensor = false;


            /// Telemetry
            telemetry.addLine(String.join(" ","УПРАВЛЕНИЕ НЕ ДАМ", "САНЕЧКА, СБРОС НУЛЯ", "НА КНОПКУ ЛЕВОГО СТИКА!!"));

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