package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
//Need test
@Autonomous(name = "Far Park", group = "Robot")
@Config
public class AutoFar2 extends LinearOpMode {
    public static final double DISTANCE = 45;
    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum drivetrain = new DriveTrainMecanum(hardwareMap, this);
        Trajectory trajectory = drivetrain.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE,
                        DriveTrainMecanum.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        waitForStart();
        sleep(2700);
        drivetrain.followTrajectory(trajectory);
    }
}
