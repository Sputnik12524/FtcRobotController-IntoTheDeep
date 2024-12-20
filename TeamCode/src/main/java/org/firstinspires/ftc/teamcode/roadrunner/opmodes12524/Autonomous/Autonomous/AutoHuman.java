package org.firstinspires.ftc.teamcode.roadrunner.modules12524.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.modules12524.DriveTrainMecanum;

@Autonomous(name = "Auto Human Player", group = "Robot")
public class AutoHuman extends LinearOpMode {
    private DriveTrainMecanum driveTrain;
    //lift

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrainMecanum(hardwareMap,this);
        Pose2d startPose = new Pose2d(12,-58);
        driveTrain.setPoseEstimate(startPose);

        Trajectory trajectoryToSubmarine = driveTrain.trajectoryBuilder(startPose)
                .forward(39.5)
                .build();

        Trajectory trajectoryToSamples = driveTrain.trajectoryBuilder(trajectoryToSubmarine.end().plus(new Pose2d(0,0, Math.toRadians(90))))
                .splineTo(new Vector2d(12,-23),Math.toRadians(0))
                .splineTo(new Vector2d(32,22), Math.toRadians(0))
                .splineTo(new Vector2d(43,26), Math.toRadians(0))
                .build();

        waitForStart();

        while (opModeIsActive() & !isStopRequested()){
            driveTrain.followTrajectory(trajectoryToSubmarine);
            //here will be lift and scoring a specimen on the chamber
            driveTrain.followTrajectory(trajectoryToSamples);
        }
    }
}
