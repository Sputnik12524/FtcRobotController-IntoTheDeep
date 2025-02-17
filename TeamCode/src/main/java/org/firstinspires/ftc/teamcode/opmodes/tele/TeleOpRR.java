package org.firstinspires.ftc.teamcode.opmodes.tele;

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

@TeleOp(name = "TeleOp Road Runner")
@Config
public class TeleOpRR extends LinearOpMode {

    public enum LiftPositions {
        LIFT_ZERO, WAIT_UPDATE, ZERO_UPDATE,
        LIFT_TO_SIDE, LIFT_TO_SPECIMEN_BEFORE, LIFT_TO_SPECIMEN_AFTER,
        LIFT_TO_BASKET
    }

    public enum ShoulderClawPositions {
        START_POSE,
        MOVING_TO_INTAKE, MOVED_TO_INTAKE,
        CLAW_CLOSING, CLAW_CLOSED,
        MOVING_TO_BASKET, MOVED_TO_BASKET, CLAW_OPEN

    }

    public enum IntakePositions {
        OUTTAKE_POS, INTAKE_POS,
        EXTENDING_OUT,
        FLIPPING_IN, EXTENDING_IN,
        REMOVE_TRASH,

    }

    /// Колесная база
    public static double VELO_SCALE_COEF = 0.00225;
    public static double CORRECTION_COEF = 20;


    /// Подъемник
    private final ElapsedTime liftTimer = new ElapsedTime();
    LiftPositions posLift = LiftPositions.LIFT_ZERO;
    double targetLiftFSM = 0;
    private boolean stateDpadUp2 = false;
    private boolean stateDpadDown2 = false;
    private boolean stateDpadLeft2 = false;
    private boolean stateDpadRight2 = false;
    private boolean stateLeftBumper = false;
    private boolean stateRightBumper = false; // Клешня для образцов


    /// Плечо и клешня
    private final ElapsedTime shoulderTimer = new ElapsedTime();
    ShoulderClawPositions posShoulder = ShoulderClawPositions.START_POSE;
    double shoulderFSM = Shoulder.INITIAL_POSITION;
    public static double SH_TIME_TO_BASKET = 1;
    public static double SH_TIME_TO_INTAKE = 0.5;
    private boolean stateA2 = false;
    private boolean stateB2 = false;

    /// Выдвижной ахват
    private final ElapsedTime intakeTimer = new ElapsedTime();
    IntakePositions posIntake = IntakePositions.OUTTAKE_POS;
    double extFSM = Intake.EXTENSION_MIN;
    double flipFSM = Intake.FLIP_OUTTAKE;
    public static double EXT_TIME = 1;
    public static double FLIP_TIME = 0.5;
    public static double BRUSH_TIME = 0.6;

    public static double NECESSARY_EXT_POS = 0.2;

    public Intake.Color badColor;


    private boolean brushInStatus = false;
    private boolean brushOutStatus = false;
    private boolean stateA1 = false;
    private boolean stateB1 = false;
    private boolean stateStickRb = false;

    private boolean flag;


    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lt = new Lift(this);
        // DriveTrain dt = new DriveTrain(this);
        Shoulder sl = new Shoulder(this);
        Intake in = new Intake(this);
        Claw cl = new Claw(this);

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

        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        PoseStorage.currentPose = driveTrain.getPoseEstimate();
        driveTrain.setPoseEstimate(PoseStorage.currentPose);

        while (opModeInInit()) {
            if (gamepad1.x) { //Синий альянс
                badColor = Intake.Color.RED;
            }
            if (gamepad1.b) { //Красный альянс
                badColor = Intake.Color.BLUE;
            }
            if (gamepad1.a) { //Для тестов
                badColor = null;
            }
        }

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {

            //КБ
            double w_target = gamepad1.left_trigger - gamepad1.right_trigger;
            double w_real = driveTrain.getExternalHeadingVelocity();
            if (Math.abs(w_real) < 1) {
                w_real = 0;
            }
            double rotate = CORRECTION_COEF * (w_target - w_real * VELO_SCALE_COEF) + w_target;

            driveTrain.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y * DriveTrainMecanum.multiplier,
                            gamepad1.left_stick_x * DriveTrainMecanum.multiplier,
                            rotate * DriveTrainMecanum.multiplier
                    )
            );

            if (gamepad1.left_bumper) {
                driveTrain.switchSlowMode();
            }

            driveTrain.update();

            if (gamepad1.dpad_left) {
                driveTrain.resetIMU();
            }

            // Read pose
            Pose2d poseEstimate = driveTrain.getPoseEstimate();

            /// Автомат для подъемника
            switch (posLift) {
                case LIFT_ZERO:
                    if (gamepad2.dpad_up && !stateDpadUp2) {
                        targetLiftFSM = Lift.POS_HIGH_BASKET;
                        posLift = LiftPositions.LIFT_TO_BASKET;
                    }
                    if (gamepad2.dpad_right && !stateDpadRight2) {
                        targetLiftFSM = Lift.POS_SIDE;
                        posLift = LiftPositions.LIFT_TO_SIDE;
                    }
                    if (gamepad2.left_stick_button && !stateLeftBumper) {
                        posLift = LiftPositions.ZERO_UPDATE;
                    }
                    break;
                case LIFT_TO_BASKET:
                    if (gamepad2.dpad_down && !stateDpadDown2) {
                        targetLiftFSM = 0;
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    if (gamepad2.left_stick_button && !stateLeftBumper) {
                        posLift = LiftPositions.ZERO_UPDATE;
                    }
                    break;

                case LIFT_TO_SIDE:
                    if (gamepad2.dpad_up && !stateDpadUp2) {
                        targetLiftFSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                        posLift = LiftPositions.LIFT_TO_SPECIMEN_BEFORE;
                    }
                    if (gamepad2.dpad_left && !stateDpadLeft2) {
                        targetLiftFSM = 0;
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    if (gamepad2.left_stick_button && !stateLeftBumper) {
                        posLift = LiftPositions.ZERO_UPDATE;
                    }
                    break;
                case LIFT_TO_SPECIMEN_BEFORE:
                    if (gamepad2.dpad_down && !stateDpadDown2) {
                        targetLiftFSM = Lift.POS_HIGH_SPECIMEN_AFTER;
                        posLift = LiftPositions.LIFT_TO_SPECIMEN_AFTER;
                    }
                    if (gamepad2.dpad_right && !stateDpadRight2) {
                        targetLiftFSM = Lift.POS_SIDE;
                        posLift = LiftPositions.LIFT_TO_SIDE;
                    }
                    if (gamepad2.dpad_left && !stateDpadRight2) {
                        targetLiftFSM = 0;
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    if (gamepad2.left_stick_button && !stateLeftBumper) {
                        posLift = LiftPositions.ZERO_UPDATE;
                    }
                    break;
                case LIFT_TO_SPECIMEN_AFTER:
                    if (gamepad2.dpad_up && !stateDpadUp2) {
                        targetLiftFSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                        posLift = LiftPositions.LIFT_TO_SPECIMEN_BEFORE;
                    }
                    if (gamepad2.dpad_right && !stateDpadRight2) {
                        targetLiftFSM = Lift.POS_SIDE;
                        posLift = LiftPositions.LIFT_TO_SIDE;
                    }
                    if (gamepad2.dpad_left && !stateDpadRight2) {
                        targetLiftFSM = 0;
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    if (gamepad2.left_stick_button && !stateLeftBumper) {
                        posLift = LiftPositions.ZERO_UPDATE;
                    }
                    break;
                case WAIT_UPDATE:
                    if (lt.isMagneting()) {
                        lt.resetZero();
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    if (Math.abs(lt.getError()) <= 0.05) {
                        posLift = LiftPositions.ZERO_UPDATE;
                    }
                    break;
                case ZERO_UPDATE:
                    targetLiftFSM += 1;
                    posLift = LiftPositions.WAIT_UPDATE;
                    break;
            }
            lt.setTarget(targetLiftFSM);
            stateDpadUp2 = gamepad2.dpad_up;
            stateDpadDown2 = gamepad2.dpad_down;
            stateDpadLeft2 = gamepad2.dpad_left;
            stateDpadRight2 = gamepad2.dpad_right;
            stateLeftBumper = gamepad2.left_bumper;

            // Управление клешней подъемника
            if (gamepad2.right_bumper && !stateRightBumper) {
                cl.switchPositionLift();
            }
            stateRightBumper = gamepad2.right_bumper;





            /// Автомат для плеча с клешней
            switch (posShoulder) {
                case START_POSE:
                    if (gamepad2.a && flag && !stateA2) {
                        shoulderTimer.reset();
                        sl.openSh();
                        shoulderFSM = Shoulder.POS_SH_FOR_INTAKE;
                        posShoulder = ShoulderClawPositions.MOVING_TO_INTAKE;
                    }
                    if (gamepad2.b && !stateB2) {
                        shoulderTimer.reset();
                        sl.closeSh();
                        shoulderFSM = Shoulder.POS_SH_BASKET;
                        posShoulder = ShoulderClawPositions.MOVING_TO_BASKET;
                    }
                    break;
                case MOVING_TO_BASKET:
                    if (shoulderTimer.seconds() >= SH_TIME_TO_BASKET) {
                        posShoulder = ShoulderClawPositions.MOVED_TO_BASKET;
                    }
                    break;
                case MOVED_TO_BASKET:
                    if (gamepad2.b && !stateB2) {
                        sl.openSh();
                        posShoulder = ShoulderClawPositions.CLAW_OPEN;
                    }
                    break;
                case CLAW_OPEN:
                    if (gamepad2.a && !stateA2) {
                        shoulderFSM = Shoulder.INITIAL_POSITION;
                        posShoulder = ShoulderClawPositions.START_POSE;
                    }

                    break;
                case MOVING_TO_INTAKE:
                    if (shoulderTimer.seconds() >= SH_TIME_TO_INTAKE) {
                        posShoulder = ShoulderClawPositions.MOVED_TO_INTAKE;
                    }
                    break;
                case MOVED_TO_INTAKE:
                    if (gamepad2.b && !stateB2) {
                        shoulderTimer.reset();
                        sl.closeSh();
                        posShoulder = ShoulderClawPositions.CLAW_CLOSING;
                    }
                    break;
                case CLAW_CLOSING:
                    if (shoulderTimer.seconds() >= SH_TIME_TO_BASKET) {
                        posShoulder = ShoulderClawPositions.CLAW_CLOSED;
                    }
                    break;
                case CLAW_CLOSED:
                    shoulderTimer.reset();
                    shoulderFSM = Shoulder.POS_SH_BASKET;
                    posShoulder = ShoulderClawPositions.MOVING_TO_BASKET;
                    break;
            }
            stateA2 = gamepad2.a;
            stateB2 = gamepad2.b;
            sl.shoulderPosition(shoulderFSM);




            ///Выдвижной захват
            switch (posIntake) {
                case OUTTAKE_POS: //Инит поза
                    flag = true;

                    if (gamepad1.right_stick_button && stateStickRb) {
                        intakeTimer.reset();
                        extFSM = Intake.EXTENSION_MAX;
                        posIntake = IntakePositions.EXTENDING_OUT;
                        flag = true;
                    }
                    if (in.getExtensionPositionR() >= NECESSARY_EXT_POS) {
                        posIntake = IntakePositions.INTAKE_POS;
                    }
                    break;
                case EXTENDING_OUT:
                    flag = false;
                    if (intakeTimer.seconds() >= EXT_TIME) {
                        flipFSM = Intake.FLIP_INTAKE;
                        posIntake = IntakePositions.INTAKE_POS;
                        flag = false;
                    }
                    break;
                case INTAKE_POS: //Берем пробы
                    flag = false;
                    if (gamepad1.right_stick_button && stateStickRb || (in.getColorSample() != badColor) || (in.getColorSample() != Intake.Color.NONE)) {
                        intakeTimer.reset();
                        flipFSM = Intake.FLIP_OUTTAKE;
                        in.brushIntake();
                        brushInStatus = true;
                        brushOutStatus = false;
                        posIntake = IntakePositions.FLIPPING_IN;
                    }
                    if ((in.getExtensionPositionR() < NECESSARY_EXT_POS) && (in.getFlipPositionR() == Intake.FLIP_OUTTAKE)) {
                        in.brushStop();
                        brushInStatus = false;
                        brushOutStatus = false;
                        posIntake = IntakePositions.OUTTAKE_POS;
                    }
                    if (in.getColorSample() == badColor) {
                        intakeTimer.reset();
                        in.brushOuttake();
                        brushInStatus = false;
                        brushOutStatus = true;
                        posIntake = IntakePositions.REMOVE_TRASH;
                    }
                    break;
                case REMOVE_TRASH:
                    if (intakeTimer.seconds() >= BRUSH_TIME) {
                        in.brushStop();
                        brushInStatus = false;
                        brushOutStatus = false;
                        posIntake = IntakePositions.INTAKE_POS;
                    }
                    break;
                case FLIPPING_IN:
                    flag = false;
                    if (intakeTimer.seconds() >= FLIP_TIME) {
                        intakeTimer.reset();
                        in.brushStop();
                        brushInStatus = false;
                        brushOutStatus = false;
                        extFSM = Intake.EXTENSION_MIN;
                        posIntake = IntakePositions.EXTENDING_IN;
                    }
                    break;
                case EXTENDING_IN:
                    flag = false;
                    if (intakeTimer.seconds() >= EXT_TIME) {
                        posIntake = IntakePositions.OUTTAKE_POS;
                    }
                    break;
            }
            in.extensionPosition(extFSM);
            in.flipPosition(flipFSM);
            stateStickRb = gamepad1.right_stick_button;

            //выдвижение
            extFSM += -gamepad1.right_stick_y * Intake.EXT_K * Intake.EXTENSION_STEP;
            //щетка
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

            //переворот
            if (gamepad1.y) {
                flipFSM = Intake.FLIP_OUTTAKE;
            }
            if (gamepad1.x) {
                flipFSM = Intake.FLIP_INTAKE;
            }


            // Print pose to telemetry
            telemetry.addLine("УПРАВЛЕНИЕ НЕ ДАМ");

            telemetry.addData("Color:", in.getColorSample());
            telemetry.addData("Hue:", in.getHue());
            telemetry.addData("Saturation:", in.getSaturation());
            telemetry.addData("Value:", in.getValue());
            telemetry.addData("PosLift:", posLift);
            telemetry.addData("Lift Encoder Position: ", lt.getCurrentPosition());
            telemetry.addData("Lift Motor Speed: ", lt.getPower());
            telemetry.addData("Stick Position: ", gamepad2.right_stick_y);
            telemetry.addData("shoulder", sl.getPosition());
            telemetry.addData("W real", w_real);
            telemetry.addData("W target", w_target);
            telemetry.addData("Rotate", rotate);
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            telemetry.addData("Состояние Lift:", posLift);
            telemetry.addData("Состояние Shoulder:", posShoulder);
            telemetry.addData("Состояние Intake", posIntake);

            telemetry.update();
        }
        lt.liftMotorPowerDriver.interrupt();
    }
    public static class PoseStorage {
        public static Pose2d currentPose = new Pose2d();
    }

}

