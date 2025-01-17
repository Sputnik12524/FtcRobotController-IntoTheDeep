package org.firstinspires.ftc.teamcode.opmodes.auto.archive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;

@Autonomous(name = "Auto Far Park", group = "Robot")
@Config
@Disabled
public class AutoFPark extends LinearOpMode {
    private DriveTrainMecanum drivetrain;
    public static double DISTANCE = 110;
    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new DriveTrainMecanum(hardwareMap,this);
        Trajectory trajectory = drivetrain.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();

        waitForStart();

        sleep(3500);
        drivetrain.followTrajectory(trajectory);
    }
}
