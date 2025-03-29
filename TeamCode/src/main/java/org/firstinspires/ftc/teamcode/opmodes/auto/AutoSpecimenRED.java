package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "RED Auto Specimen", group = "Robot")
public class AutoSpecimenRED extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum base = new DriveTrainMecanum(hardwareMap, this);
        Claw claw = new Claw(this);
        Lift lift = new Lift(this);
        Intake in = new Intake(this);
        Shoulder shoulder = new Shoulder(this);
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-9, 54, Math.toRadians(90));
        base.setPoseEstimate(startPose);
        claw.closeLift();
        TrajectorySequence trajectorySpecimen = base.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.72);
                    lift.setTarget(-33);
                })
                .back(10, DriveTrainMecanum.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .back(15, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    shoulder.openSh();
                    shoulder.shoulderPosition(.75);
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(1)
                .forward(5)
                .addDisplacementMarker(() -> shoulder.shoulderPosition(.1))
                .waitSeconds(0.5)
                .addDisplacementMarker(() -> lift.setTarget(0)) //statement lambda was replaced with expression lambda
                .build();
        TrajectorySequence trajectoryCaptureSecondSpecimen = base.trajectorySequenceBuilder(trajectorySpecimen.end())
                .splineTo(new Vector2d(-52, 59), Math.toRadians(90))
                .forward(3)
                .addDisplacementMarker(() -> {
                    claw.openLift();
                    sleep(1000);
                    lift.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE);
                })
                .build();
        TrajectorySequence trajectoryScoringSecondSpecimen = base.trajectorySequenceBuilder(trajectoryCaptureSecondSpecimen.end())
                .waitSeconds(1)
                .back(10)
                .turn(Math.toRadians(70))
                .back(46)
                .turn(Math.toRadians(-70))
                .back(5.5, DriveTrainMecanum.getVelocityConstraint(15,
                                DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_HIGH_SPECIMEN_AFTER);
                    sleep(1000);
                    claw.closeLift();
                })
                .splineTo(new Vector2d(-52, 55), Math.toRadians(180))
                .addDisplacementMarker(() -> lift.setTarget(0))
                .waitSeconds(1)
                .build();
        shoulder.shoulderPosition(0.1);
        shoulder.closeSh();
        in.extensionPosition(Intake.EXT_POS_MIN);

        waitForStart();

        if (isStopRequested()) return;
        base.followTrajectorySequence(trajectorySpecimen);
        sleep(500);
        base.followTrajectorySequence(trajectoryCaptureSecondSpecimen);
        sleep(500);
        base.followTrajectorySequence(trajectoryScoringSecondSpecimen);
        sleep(500);
        sleep(1000);
        lift.liftMotorPowerDriver.interrupt();
    }
}
