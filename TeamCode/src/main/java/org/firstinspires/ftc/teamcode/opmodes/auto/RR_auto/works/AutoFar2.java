package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto.works;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.roadrunner.driveTrainMecanum.TestDT;
//Need test
@Autonomous(name = "A Far Park", group = "Robot")
@Config
public class AutoFar2 extends LinearOpMode {
    //private DriveTrain driveTrain;
    private TestDT drivetrain;
    public static double DISTANCE = 90;
    @Override
    public void runOpMode() throws InterruptedException {
        //driveTrain = new DriveTrain(this);
           drivetrain = new TestDT(hardwareMap,this);
        Trajectory trajectory = drivetrain.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE,
                        TestDT.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        waitForStart();
        //while (opModeIsActive()) {
          //  driveTrain.driveStraight(.25, 120);
        //}
        sleep(3500);
        drivetrain.followTrajectory(trajectory);
    }
}
