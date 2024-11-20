package org.firstinspires.ftc.teamcode.roadrunner.opmodes12524.TeleOp;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.roadrunner.modules12524.DriveTrainMecanum;

@TeleOp(name="TeleOp Road Runner")
public class TeleOpRR extends LinearOpMode {


    private double straight;
    private double side;
    private double rotate;

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);

        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        PoseStorage.currentPose = driveTrain.getPoseEstimate();


        driveTrain.setPoseEstimate(PoseStorage.currentPose);

        //straight = gamepad1.left_stick_y * DriveTrainMecanum.multiplier;
        //side = gamepad1.right_stick_x * DriveTrainMecanum.multiplier;
        //rotate = (gamepad1.left_trigger - gamepad1.right_trigger) * DriveTrainMecanum.multiplier;
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            driveTrain.setWeightedDrivePower(
                    new Pose2d(
                            gamepad1.left_stick_y * DriveTrainMecanum.multiplier,
                            gamepad1.right_stick_x * DriveTrainMecanum.multiplier,
                            (gamepad1.left_trigger - gamepad1.right_trigger) * DriveTrainMecanum.multiplier
                    )
            );

            if(gamepad1.left_bumper){
                driveTrain.switchSlowMode();
            }

            driveTrain.update();

            // Read pose
            Pose2d poseEstimate = driveTrain.getPoseEstimate();

            // Print pose to telemetry
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }
}

 class PoseStorage {
    public static Pose2d currentPose = new Pose2d();
}