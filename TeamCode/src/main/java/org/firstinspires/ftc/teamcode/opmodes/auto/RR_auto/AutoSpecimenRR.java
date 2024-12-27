package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.modules12524.TestDT;

@Autonomous (name = "RR AUTO SPECIMEN", group = "Robot")
public class AutoSpecimenRR extends LinearOpMode {
    private TestDT base;
    private Claw claw;
    private Shoulder shoulder;
    private Lift lift;

    static final double drive_speed = .3;
    static final double turn_speed = .3;
    static final double side_speed = .25;
    @Override
    public void runOpMode() {
        base = new TestDT(hardwareMap, this);
        claw = new Claw(this);
        lift = new Lift(this);
        shoulder = new Shoulder(this);

        Trajectory trajectoryToSubmarine1 = base.trajectoryBuilder(new Pose2d())
                .forward(12)
                .build();
        Trajectory trajectoryToSubmarine2 = base.trajectoryBuilder(trajectoryToSubmarine1.end().plus(new Pose2d()))
                .forward(46)
                .build();
        Trajectory trajectoryBackward = base.trajectoryBuilder(trajectoryToSubmarine2.end().plus(new Pose2d()))
                .back(2)
                .build();
        Trajectory trajectoryToObservationZone = base.trajectoryBuilder(trajectoryBackward.end().plus(new Pose2d()))
                .forward(85)
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
            base.turn(115);
            sleep(500);
            base.followTrajectory(trajectoryToObservationZone);

        }
    }
}
