package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.TestDT;

@Autonomous(name = "Near Park", group = "Robot")
@Config
public class AutoNear2 extends LinearOpMode {
    public static final double DISTANCE = 70;
    @Override
    public void runOpMode() throws InterruptedException {
        //driveTrain = new DriveTrain(this);
        //private DriveTrain driveTrain;
        TestDT drivetrain = new TestDT(hardwareMap, this);
        Trajectory trajectory = drivetrain.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE, TestDT.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        waitForStart();
        sleep(3500);
        drivetrain.followTrajectory(trajectory);
    }
}
