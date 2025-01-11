package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto.works;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.roadrunner.driveTrainMecanum.TestDT;

@Autonomous (name = "RR AUTO SPECIMEN", group = "Robot")
public class AutoSpecimenRR extends LinearOpMode {
    private TestDT base;
    private Claw claw;
    private Shoulder shoulder;
    private Lift lift;

   // static final double drive_speed = .3;
   // static final double turn_speed = .3;
    //static final double side_speed = .25;
    @Override
    public void runOpMode() {
        base = new TestDT(hardwareMap, this);
        claw = new Claw(this);
        lift = new Lift(this);
        shoulder = new Shoulder(this);

        Trajectory trajectoryToSubmarine1 = base.trajectoryBuilder(new Pose2d())
                .forward(19)
                .build();
        Trajectory trajectoryToSubmarine2 = base.trajectoryBuilder(trajectoryToSubmarine1.end().plus(new Pose2d()))
                .back(14 , TestDT.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory trajectoryBackward = base.trajectoryBuilder(trajectoryToSubmarine2.end().plus(new Pose2d()))
                .forward(3, TestDT.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory trajectoryToObservationZone = base.trajectoryBuilder(trajectoryBackward.end().plus(new Pose2d(Math.toRadians(-105))))
                .forward(50, TestDT.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        shoulder.shoulderPosition(0);
        claw.closeSh();
        lift.resetZero();

        waitForStart();

        while(opModeIsActive()) {
            base.followTrajectory(trajectoryToSubmarine1);
            sleep(1500);
            base.turn(Math.toRadians(180));
            sleep(1000);
            shoulder.shoulderPosition(.5);
            sleep(1000);
            base.followTrajectory(trajectoryToSubmarine2);
            shoulder.shoulderPosition(.7);
            sleep(500);
            base.followTrajectory(trajectoryBackward);
            claw.openSh();
            sleep(1000);
            shoulder.shoulderPosition(0);
            sleep(500);
            base.turn(Math.toRadians(-120));
            sleep(1500);
            base.followTrajectory(trajectoryToObservationZone); //не работает
        }
    }
}
