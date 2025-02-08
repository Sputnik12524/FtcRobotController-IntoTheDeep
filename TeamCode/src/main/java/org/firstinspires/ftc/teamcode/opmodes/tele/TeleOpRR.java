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

    /// Колесная база
    public static double VELO_SCALE_COEF = 0.00225;
    public static double CORRECTION_COEF = 7;


    /// Подъемник
    ElapsedTime liftTimer = new ElapsedTime();
    LiftPositions posLift = LiftPositions.LIFT_ZERO;
    private static double TARGET_FSM = 0;

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


    /// Клешня для образцов
    private boolean stateLeftBumper = false;
    private boolean stateRightBumper = false;

    /// Захват
    private boolean brushInStatus = false;
    private boolean brushOutStatus = false;
    private boolean stateA1 = false;
    private boolean stateB1 = false;

    private boolean flag;


    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lt = new Lift(this);
        // DriveTrain dt = new DriveTrain(this);
        Shoulder sl = new Shoulder(this);
        Intake in = new Intake(this);
        Claw cl = new Claw(this);

        lt.liftMotorPowerDriver.start();
        in.samplesTaker.start();
        sl.sampleShTaker.start();

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

            // Автомат для подъемника


            // Автомат для плеча с клешней
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
            sl.shoulderPosition();

            switch (posLift) {
                case LIFT_ZERO:
                    if (gamepad2.dpad_up) {
                        TARGET_FSM = Lift.POS_HIGH_BASKET;
                        posLift = LiftPositions.LIFT_TO_BASKET;
                    }
                    if (gamepad2.dpad_right) {
                        TARGET_FSM = Lift.POS_SIDE;
                        posLift = LiftPositions.LIFT_TO_SIDE;
                    }
                    break;
                case FIND_ZERO:
                    break;
                case ZERO_FOUND:
                    break;
                case LIFT_TO_SIDE:
                    if (gamepad2.dpad_up) {
                        TARGET_FSM = Lift.POS_HIGH_SPECIMEN_BEFORE;
                        posLift = LiftPositions.LIFT_TO_SPECIMEN_BEFORE;
                    }
                    if (gamepad2.left_bumper)

                    break;
                case LIFT_TO_SPECIMEN_BEFORE:

                    break;
                case LIFT_TO_SPECIMEN_AFTER:

                    break;
                case LIFT_TO_BASKET:
                    if (gamepad2.dpad_down) {
                     TARGET_FSM = 0;
                     posLift = LiftPositions.LIFT_ZERO;
                    }
                    break;
            }


            // Управление клешней.
            if (gamepad2.left_bumper && !stateLeftBumper) {
                sl.switchPositionShoulder();
            }
            if (gamepad2.right_bumper && !stateRightBumper) {
                cl.switchPositionLift();
            }
            stateLeftBumper = gamepad2.left_bumper;
            stateRightBumper = gamepad2.right_bumper;


            //  Управление захватом
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
            //выдвижение
            in.extUpdatePosition(-gamepad1.right_stick_y); //с помощью стика

            //многопоточность
            if (gamepad1.right_stick_button && in.getExtensionPositionR() >= 0.2) {
                in.needOuttake();
                brushInStatus = false;
                brushOutStatus = false;
            } else if (gamepad1.right_stick_button && in.getExtensionPositionR() < 0.2) {
                in.needTake();
                brushInStatus = false;
                brushOutStatus = false;
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
        lt.liftMotorPowerDriver.interrupt();
        in.samplesTaker.interrupt();
        sl.sampleShTaker.interrupt();
    }

    public static class PoseStorage {
        public static Pose2d currentPose = new Pose2d();
    }

}

