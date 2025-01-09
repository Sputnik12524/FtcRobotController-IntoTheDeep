package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.driveTrainMecanum.DriveTrainMecanum;

public class AutoBasket extends LinearOpMode {

    private DriveTrainMecanum driveTrain;
    //lift

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrainMecanum(hardwareMap, this);

        Trajectory trajectoryToSubmarine = driveTrain.trajectoryBuilder(new Pose2d())
                .forward(39.5)
                .build();

        Trajectory trajectoryToYellowSample = driveTrain.trajectoryBuilder(trajectoryToSubmarine.end().plus(new Pose2d(0,0,Math.toRadians(90))))
                .splineTo(new Vector2d(0,0), Math.toRadians(0))
                .splineTo(new Vector2d(-20,45), Math.toRadians(0))
                .build();

        Trajectory trajectoryToNet = driveTrain.trajectoryBuilder(trajectoryToYellowSample.end().plus(new Pose2d(0,0,Math.toRadians(0))))
                .splineTo(new Vector2d(-23,9), Math.toRadians(0))
                .build();

        Trajectory trajectoryToAscent = driveTrain.trajectoryBuilder(trajectoryToNet.end().plus(new Pose2d(0,0,Math.toRadians(45))))
                .splineTo(new Vector2d(-10,45), Math.toRadians(0))
                .build();
        waitForStart();

        while(opModeIsActive() & !isStopRequested()) {
            driveTrain.followTrajectory(trajectoryToSubmarine);
            //score specimen
            driveTrain.followTrajectory(trajectoryToYellowSample);
            //take a sample
            driveTrain.followTrajectory(trajectoryToNet);
            //score sample to basket
            driveTrain.followTrajectory(trajectoryToAscent);
        }
    }
}
