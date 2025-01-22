package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "For Test", group = "Robot")
@Config
public class TestAuto extends LinearOpMode {
    public static double x_distance = 43.2;
    public static double y_distance = 30;
    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum drivetrain = new DriveTrainMecanum(hardwareMap, this);

        Pose2d startPose = new Pose2d(-11,-53,0);
        drivetrain.setPoseEstimate(startPose);
        TrajectorySequence trajectory = drivetrain.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(-52,-40),90).build();

        waitForStart();
            drivetrain.followTrajectorySequence(trajectory);
        }
    }
