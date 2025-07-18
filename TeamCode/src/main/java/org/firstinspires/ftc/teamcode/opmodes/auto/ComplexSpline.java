package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "2. Complex", group = "Robot")
@Config
public class ComplexSpline extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);

        Pose2d startPose = new Pose2d(-38, 57, Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);


        TrajectorySequence trajectory = driveTrain.trajectorySequenceBuilder(startPose)
                .waitSeconds(3)
                .turn(Math.toRadians(-45))
                .splineTo(new Vector2d(-40,1),Math.toRadians(-90))
                .turn(Math.toRadians(-45))
                .build();
        Trajectory trajectoryUiA = driveTrain.trajectoryBuilder(startPose,true)
                .splineTo(new Vector2d(-38,57), Math.toRadians(-90))
                .build();

        waitForStart();

        if (isStopRequested()) return;

        driveTrain.followTrajectorySequence(trajectory);
        driveTrain.followTrajectory(trajectoryUiA);
    }
}
