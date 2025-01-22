package org.firstinspires.ftc.teamcode.opmodes.auto.archive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.TestDT;

@Autonomous (name = "RR AUTO BASKET SPECIMEN", group = "Robot")
@Disabled
public class AutoSpecimenBasket extends LinearOpMode {

    static final double drive_speed = .3;
    static final double turn_speed = .3;
    static final double side_speed = .25;
    @Override
    public void runOpMode() {
        TestDT base = new TestDT(hardwareMap, this);
        Claw claw = new Claw(this);
        Lift lift = new Lift(this);
        Shoulder shoulder = new Shoulder(this);

        Pose2d startPose = new Pose2d(11,-46);
        base.setPoseEstimate(startPose);

        Trajectory trajectoryToSubmarine1 = base.trajectoryBuilder(new Pose2d())
                .forward(12)
                .build();
        Trajectory trajectoryToSubmarine2 = base.trajectoryBuilder(trajectoryToSubmarine1.end().plus(new Pose2d()))
                .forward(16 , TestDT.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory trajectoryBackward = base.trajectoryBuilder(trajectoryToSubmarine2.end().plus(new Pose2d()))
                .back(2, TestDT.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory trajectoryToNet = base.trajectoryBuilder(trajectoryBackward.end().plus(new Pose2d()))
                .forward(50, TestDT.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        claw.closeSh();
        lift.resetZero();
        waitForStart();
        while(opModeIsActive()) {
            base.followTrajectory(trajectoryToSubmarine1);
            shoulder.shoulderPosition(.3);
            sleep(1000);
            base.followTrajectory(trajectoryToSubmarine2);
            shoulder.shoulderPosition(.2);
            sleep(500);
            base.followTrajectory(trajectoryBackward);
            claw.openSh();
            sleep(1000);
            shoulder.shoulderPosition(0);
            sleep(500);
            //finished scoring specimen
            base.turn(Math.toRadians(105));
            sleep(1500);
            base.followTrajectory(trajectoryToNet);
        }
    }
}
