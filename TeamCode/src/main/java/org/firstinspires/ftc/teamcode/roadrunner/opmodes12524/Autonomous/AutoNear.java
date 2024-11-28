package org.firstinspires.ftc.teamcode.roadrunner.opmodes12524.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
@Autonomous(name = "Auto Near Park", group = "Robot")
@Config
public class AutoNear extends LinearOpMode {
    private DriveTrain driveTrain;
    //private DriveTrainMecanum drivetrain;
    //public static double DISTANCE = 70;
    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrain(this);
        //drivetrain = new DriveTrainMecanum(hardwareMap,this);
        /*
                Trajectory trajectory = drive.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();
         */
        waitForStart();
        driveTrain.driveStraight(.25, 70);
        //drivetrain.followTrajectory(trajectory);
    }
}
