package org.firstinspires.ftc.teamcode.opmodes.auto.archive;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;
@Autonomous(name = "Auto Near Park", group = "Robot")
@Config
@Disabled
public class AutoNPark extends LinearOpMode {
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
        while (opModeIsActive()) {
            driveTrain.driveStraight(.25, 70);
        }
        //drivetrain.followTrajectory(trajectory);
    }
}
