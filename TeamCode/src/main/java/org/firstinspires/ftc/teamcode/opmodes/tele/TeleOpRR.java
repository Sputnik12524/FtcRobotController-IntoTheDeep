package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;

@TeleOp(name = "TeleOp Road Runner")
@Config
public class TeleOpRR extends LinearOpMode {

    public static double VELO_SCALE_COEF = 0.00225;
    public static double CORRECTION_COEF = 7;

    // Подъемник
    private boolean statusDpadDownBefore = false;
    private boolean statusDpadUpBefore = false;
    private int posLift = 0;

    // Клешни
    private boolean stateLeftBumper = false;
    private boolean stateRightBumper = false;

    //щетки
    private boolean brushInStatus = false;
    private boolean brushOutStatus = false;
    private boolean stateA = false;
    private boolean stateB = false;


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

            // Управление подъемником
            if (gamepad2.dpad_up && !statusDpadUpBefore && posLift < 6) {
                posLift += 1;
            }
            statusDpadUpBefore = gamepad2.dpad_up;
            if (gamepad2.dpad_down && !statusDpadDownBefore && posLift > 0) {
                posLift -= 1;
            }
            statusDpadDownBefore = gamepad2.dpad_down;


            switch (posLift) {
                case (0):
                    lt.setTarget(Lift.POS_LOWEST);
                    break;
                case (1):
                    lt.setTarget(Lift.POS_SIDE_2);
                    break;
                case (2):
                    lt.setTarget(Lift.POS_SIDE);
                    break;
                case (3):
                    lt.setTarget(Lift.POS_LOW_SPECIMEN_BEFORE);
                    break;
                case (4):
                    lt.setTarget(Lift.POS_LOW_BASKET);
                    break;
                case (5):
                    lt.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE);
                    break;
                case (6):
                    lt.setTarget(Lift.POS_HIGH_BASKET);
                    break;
            }
            if (gamepad2.dpad_left) {
                posLift = 0;
            }
            if (gamepad2.dpad_right) {
                posLift = 5;
            }


            // Управление плечо
            // по позициям
            if (gamepad2.b) {
                sl.needToBasketSh();
            } else if (gamepad2.a) {
                sl.openSh();
                sl.shoulderPosition(Shoulder.POS_SH_FOR_INTAKE);
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
            if (gamepad1.a && !brushInStatus && !stateA) {
                in.brushIntake();
                brushInStatus = true;
                brushOutStatus = false;

            } else if (gamepad1.a && brushInStatus && !stateA) {
                in.brushStop();
                brushInStatus = false;
            }
            if (gamepad1.b && !brushOutStatus && !stateB) {
                in.brushOuttake();
                brushOutStatus = true;
                brushInStatus = false;
            } else if (gamepad1.b && brushOutStatus && !stateB) {
                in.brushStop();
                brushOutStatus = false;
            }
            stateA = gamepad1.a;
            stateB = gamepad1.b;

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
            if(gamepad2.x) {
                posLift = 0;
                lt.setTarget(0);
                sleep(150);
                lt.resetZero();
            }


            // Print pose to telemetry
            telemetry.addLine("УПРАВЛЕНИЕ НЕ ДАМ");

            telemetry.addData("Color:", in.getColorSample());

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

