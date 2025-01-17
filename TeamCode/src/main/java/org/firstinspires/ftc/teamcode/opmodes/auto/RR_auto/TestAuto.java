package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.TestDT;

@Autonomous(name = "For Test", group = "Robot")
@Config
public class TestAuto extends LinearOpMode {
    private TestDT drivetrain;
    public static double x_distance = 43.2;
    public static double y_distance = 30;
    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new TestDT(hardwareMap,this);

        Pose2d startPose = new Pose2d(46,-46,0);
        drivetrain.setPoseEstimate(startPose);
        Trajectory trajectory = drivetrain.trajectoryBuilder(startPose) //добавить true для реверса
                .splineTo(new Vector2d(-46,-10), Math.toRadians(0))
                .splineTo(new Vector2d(56,-10), Math.toRadians(0))
                .build();

        waitForStart();
            drivetrain.followTrajectory(trajectory);
        }
    }
