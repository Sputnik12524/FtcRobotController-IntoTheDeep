package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "1+1 Auto Specimen", group = "Robot")
public class Auto2Specimen extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum base = new DriveTrainMecanum(hardwareMap, this);
        Claw cl = new Claw(this);
        Lift lift = new Lift(this);
        Intake in = new Intake(this);
        Shoulder shoulder = new Shoulder(this);
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(10, 56, Math.toRadians(90));
        base.setPoseEstimate(startPose);
        cl.openLift();

        TrajectorySequence trajectorySpecimen = base.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> lift.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE))
                .forward(28)
                .build();
        shoulder.shoulderPosition(0.1);
        shoulder.closeSh();
        in.extensionPosition(Intake.EXT_POS_MIN);

        waitForStart();

        if (isStopRequested()) return;
        base.followTrajectorySequence(trajectorySpecimen);
        sleep(500);
        lift.setTarget(Lift.POS_HIGH_SPECIMEN_AFTER);
        sleep(1000);
        cl.closeLift();
        sleep(1000);
        base.followTrajectorySequence(base.trajectorySequenceBuilder(base.getPoseEstimate())
                .addDisplacementMarker(() -> lift.setTarget(0))
                .back(10)
                .waitSeconds(0.5)
                .build());
        base.followTrajectorySequence(base.trajectorySequenceBuilder(base.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(50, 60, Math.toRadians(270)))
                .forward(10)
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    cl.openLift();
                    sleep(700);
                    lift.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE);
                })
                .build());
        sleep(500);
        base.followTrajectorySequence(base.trajectorySequenceBuilder(base.getPoseEstimate())
                .back(10)
                .turn(Math.toRadians(180))
                .strafeLeft(50)
                .forward(20)
                .build());
        lift.setTarget(-27);
        sleep(1200);
        cl.closeLift();
        base.followTrajectorySequence(base.trajectorySequenceBuilder(base.getPoseEstimate())
                .addDisplacementMarker(() -> lift.setTarget(0))
                .back(10)
                .lineTo(new Vector2d(50,60))
                .build());
        lift.liftMotorPowerDriver.interrupt();
    }
}
