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
        LIFT_ZERO, FIND_ZERO, ZERO_FOUND,
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
        INTAKE_POS, OUTTAKE_POS,
        EXTENDING_OUT, EXTENDED_OUT,
        FLIPPING_IN, FLIPPED_IN, EXTENDING_IN

    }

    /// Колесная база
    public static double VELO_SCALE_COEF = 0.00225;
    public static double CORRECTION_COEF = 7;


    /// Подъемник
    ElapsedTime liftTimer = new ElapsedTime();
    LiftPositions posLift = LiftPositions.LIFT_ZERO;
    private static double TARGET_FSM = 0;
    private boolean stateDpadUp2 = false;
    private boolean stateDpadDown2 = false;
    private boolean stateDpadLeft2 = false;
    private boolean stateDpadRight2 = false;


    /// Плечо и клешня
    ElapsedTime shoulderTimer = new ElapsedTime();
    ShoulderClawPositions posShoulder = ShoulderClawPositions.START_POSE;
    public double SHOULDER_FSM = 0;
    public double CLAW_SH_FSM = 0;
    public static double SH_TIME_TO_BASKET = 2;
    public static double SH_TIME_TO_INTAKE = 0.5;
    public static double CL_TIME_CLOSING = 1.5;
    private boolean stateA2 = false;
    private boolean stateB2 = false;

    /// Выдвижной ахват
    ElapsedTime intakeTimer = new ElapsedTime();
    IntakePositions posIntake = IntakePositions.INTAKE_POS;
    public double EXT_FSM = 0;
    public double FLIP_FSM = 0;
    public static double EXT_TIME = 2;
    public static double FLIP_TIME = 1;

    public static double NECESSARY_EXT_POS = 0.2;


    private boolean brushInStatus = false;
    private boolean brushOutStatus = false;
    private boolean stateA1 = false;
    private boolean stateB1 = false;

    private boolean flag;

    /// Клешня для образцов
    private boolean stateRightBumper = false;
//    private boolean stateLeftBumper = false;


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

        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        PoseStorage.currentPose = driveTrain.getPoseEstimate();
        driveTrain.setPoseEstimate(PoseStorage.currentPose);

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
                        TARGET_FSM = Lift.POS_HIGH_BASKET;
                        posLift = LiftPositions.LIFT_TO_BASKET;
                    }
                    if (gamepad2.dpad_right && !stateDpadRight2) {
                        TARGET_FSM = Lift.POS_SIDE;
                        posLift = LiftPositions.LIFT_TO_SIDE;
                    }
                    break;
                case LIFT_TO_BASKET:
                    if (gamepad2.dpad_down && !stateDpadDown2) {
                        TARGET_FSM = 0;
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    break;

                case LIFT_TO_SIDE:
                    if (gamepad2.dpad_up && !stateDpadUp2) {
                        TARGET_FSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                        posLift = LiftPositions.LIFT_TO_SPECIMEN_BEFORE;
                    }
                    if (gamepad2.dpad_left && !stateDpadLeft2) {
                        TARGET_FSM = 0;
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    break;
                case LIFT_TO_SPECIMEN_BEFORE:
                    if (gamepad2.dpad_down && !stateDpadDown2) {
                        TARGET_FSM = Lift.POS_HIGH_SPECIMEN_AFTER;
                        posLift = LiftPositions.LIFT_TO_SPECIMEN_AFTER;
                    }
                    if (gamepad2.dpad_right && !stateDpadRight2) {
                        TARGET_FSM = Lift.POS_SIDE;
                        posLift = LiftPositions.LIFT_TO_SIDE;
                    }
                    if (gamepad2.dpad_left && !stateDpadRight2) {
                        TARGET_FSM = 0;
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    break;
                case LIFT_TO_SPECIMEN_AFTER:
                    if (gamepad2.dpad_up && !stateDpadUp2) {
                        TARGET_FSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                        posLift = LiftPositions.LIFT_TO_SPECIMEN_BEFORE;
                    }
                    if (gamepad2.dpad_right && !stateDpadRight2) {
                        TARGET_FSM = Lift.POS_SIDE;
                        posLift = LiftPositions.LIFT_TO_SIDE;
                    }
                    if (gamepad2.dpad_left && !stateDpadRight2) {
                        TARGET_FSM = 0;
                        posLift = LiftPositions.LIFT_ZERO;
                    }
                    break;


                case FIND_ZERO:
                    break;
                case ZERO_FOUND:
                    break;
            }
            lt.setTarget(TARGET_FSM);
            stateDpadUp2 = gamepad2.dpad_up;
            stateDpadDown2 = gamepad2.dpad_down;
            stateDpadLeft2 = gamepad2.dpad_left;
            stateDpadRight2 = gamepad2.dpad_right;

            // Управление клешней подъемника
            if (gamepad2.right_bumper && !stateRightBumper) {
                cl.switchPositionLift();
            }
            stateRightBumper = gamepad2.right_bumper;





            /// Автомат для плеча с клешней
            switch (posShoulder) {
                case START_POSE:
                    if ((gamepad2.a || flag) && !stateA2) {
                        shoulderTimer.reset();
                        CLAW_SH_FSM = Shoulder.CLAW_OPEN;
                        SHOULDER_FSM = Shoulder.POS_SH_FOR_INTAKE;
                        flag = false;
                        posShoulder = ShoulderClawPositions.MOVING_TO_INTAKE;
                    }
                    if (gamepad2.b && !stateB2) {
                        shoulderTimer.reset();
                        CLAW_SH_FSM = Shoulder.CLAW_CLOSE;
                        SHOULDER_FSM = Shoulder.POS_SH_BASKET;
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
                        CLAW_SH_FSM = Shoulder.CLAW_OPEN;
                        posShoulder = ShoulderClawPositions.CLAW_OPEN;
                    }
                    break;
                case CLAW_OPEN:
                    if (gamepad2.a && !stateA2) {
                        SHOULDER_FSM = Shoulder.INITIAL_POSITION;
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
                        CLAW_SH_FSM = Shoulder.CLAW_CLOSE;
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
                    SHOULDER_FSM = Shoulder.POS_SH_BASKET;
                    posShoulder = ShoulderClawPositions.MOVING_TO_BASKET;
                    break;
            }
            stateA2 = gamepad2.a;
            stateB2 = gamepad2.b;
            sl.shoulderPosition(SHOULDER_FSM);




            ///Выдвижной захват
            switch (posIntake) {
                case INTAKE_POS:
                    if (gamepad1.right_stick_button) {
                        intakeTimer.reset();
                        EXT_FSM = Intake.EXTENSION_MAX;
                        posIntake = IntakePositions.EXTENDING_OUT;
                        flag = false;
                    }
                    if (in.getExtensionPositionR() >= NECESSARY_EXT_POS) {
                        posIntake = IntakePositions.OUTTAKE_POS;
                        flag = false;
                    }
                    break;
                case EXTENDING_OUT:
                    if (intakeTimer.seconds() >= EXT_TIME) {
                        FLIP_FSM = Intake.FLIP_OUTTAKE;
                        posIntake = IntakePositions.EXTENDED_OUT;
                    }
                    break;
                case OUTTAKE_POS:
                    if (gamepad1.right_stick_button) {
                        intakeTimer.reset();
                        FLIP_FSM = Intake.FLIP_INTAKE;
                        in.brushIntake();
                        brushInStatus = true;
                        brushOutStatus = false;
                    }
                    if (in.getExtensionPositionR() < NECESSARY_EXT_POS) {
                        FLIP_FSM = Intake.FLIP_INTAKE;
                        in.brushStop();
                        brushInStatus = false;
                        brushOutStatus = false;

                        flag = true;
                        posIntake = IntakePositions.INTAKE_POS;
                    }
                    break;
                case FLIPPING_IN:
                    if (intakeTimer.seconds() >= FLIP_TIME) {
                        intakeTimer.reset();
                        in.brushStop();
                        brushInStatus = false;
                        brushOutStatus = false;
                        EXT_FSM = Intake.EXTENSION_MIN;
                        posIntake = IntakePositions.EXTENDING_IN;
                    }
                    break;
                case EXTENDING_IN:
                    if (intakeTimer.seconds() >= EXT_TIME) {
                        flag = true;
                        posIntake = IntakePositions.INTAKE_POS;
                    }
                    break;
            }
            in.extensionPosition(EXT_FSM);
            in.flipPosition(FLIP_FSM);

            //выдвижение
            in.extUpdatePosition(-gamepad1.right_stick_y); //с помощью стика
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
                in.flipPosition(Intake.FLIP_INTAKE);
            }
            if (gamepad1.x) {
                in.flipPosition(Intake.FLIP_OUTTAKE);
            }


            // Print pose to telemetry
            telemetry.addLine("УПРАВЛЕНИЕ НЕ ДАМ");


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
            telemetry.update();
        }
    }

    public static class PoseStorage {
        public static Pose2d currentPose = new Pose2d();
    }

}

