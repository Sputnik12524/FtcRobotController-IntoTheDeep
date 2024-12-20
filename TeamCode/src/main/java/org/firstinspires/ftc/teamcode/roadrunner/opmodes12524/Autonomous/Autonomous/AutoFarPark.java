package org.firstinspires.ftc.teamcode.roadrunner.modules12524.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.roadrunner.modules12524.DriveTrainMecanum;

@Autonomous(name = "Auto Far Park", group = "Robot")
@Config
public class AutoFarPark extends LinearOpMode {
    //private DriveTrain driveTrain;
    private DriveTrainMecanum drivetrain;
    public static double DISTANCE = 120;
    @Override
    public void runOpMode() throws InterruptedException {
        //driveTrain = new DriveTrain(this);
        drivetrain = new DriveTrainMecanum(hardwareMap,this);
        Trajectory trajectory = drivetrain.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();

        waitForStart();
        //while (opModeIsActive()) {
          //  driveTrain.driveStraight(.25, 120);
        //}
        drivetrain.followTrajectory(trajectory);
    }
}
