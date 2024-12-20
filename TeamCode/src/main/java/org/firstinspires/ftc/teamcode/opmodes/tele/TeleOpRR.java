package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.roadrunner.modules12524.DriveTrainMecanum;
/*
Need test
 */
@TeleOp(name="TeleOp Road Runner")
@Config
public class TeleOpRR extends LinearOpMode {

    private double straight;
    private double side;
    private double rotate;
    public static double VELO_SCALE_COEF = 0.00225;
    public static double CORRECTION_COEF = 7;
    private double w_target;
    private double w_real;

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);

        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        PoseStorage.currentPose = driveTrain.getPoseEstimate();
        driveTrain.setPoseEstimate(PoseStorage.currentPose);

        //straight = gamepad1.left_stick_y * DriveTrainMecanum.multiplier;
        //side = gamepad1.right_stick_x * DriveTrainMecanum.multiplier;
        //rotate = (gamepad1.left_trigger - gamepad1.right_trigger) * DriveTrainMecanum.multiplier;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            boolean stateB = gamepad2.b;
            boolean stateY = gamepad2.y;
            double speed = gamepad2.right_stick_y;

            lift.setLiftMotorPower(gamepad2.right_stick_y);
            w_target = gamepad1.left_trigger - gamepad1.right_trigger;
            w_real = driveTrain.getExternalHeadingVelocity();
            if (Math.abs(w_real) < 1) {
                w_real = 0;
            }
            rotate = CORRECTION_COEF * (w_target - w_real * VELO_SCALE_COEF) + w_target;

            driveTrain.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y * DriveTrainMecanum.multiplier,
                            gamepad1.right_stick_x * DriveTrainMecanum.multiplier,
                            rotate * DriveTrainMecanum.multiplier
                    )
            );

            if(gamepad1.left_bumper){
                driveTrain.switchSlowMode();
            }

            driveTrain.update();

            if (gamepad2.b && !stateB) {
                lift.resetZero();
            }

            if(gamepad1.x) {
                driveTrain.resetIMU();
            }

//            if (gamepad2.y && !stateY) {
//                lift.updatePIDF();
//            }

            // Read pose
            Pose2d poseEstimate = driveTrain.getPoseEstimate();

            // Print pose to telemetry
            //telemetry.addData("W x:", driveTrain.imu.getRobotAngularVelocity(AngleUnit.DEGREES).xRotationRate);
            //telemetry.addData("W y:", driveTrain.imu.getRobotAngularVelocity(AngleUnit.DEGREES).yRotationRate);
            //telemetry.addData("W z", driveTrain.imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate);
            telemetry.addData("W real", w_real);
            telemetry.addData("W target", w_target);
            telemetry.addData("Rotate", rotate);
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }

    static class PoseStorage {
        public static Pose2d currentPose = new Pose2d();
    }
}

