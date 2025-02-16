package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "BLUE Auto Specimen", group = "Robot")
public class AutoSpecimenBLUE extends LinearOpMode {
    public Claw claw;
    public Shoulder shoulder;

    @Override
    public void runOpMode() {
        DriveTrainMecanum base = new DriveTrainMecanum(hardwareMap, this);
        claw = new Claw(this);
        Lift lift = new Lift(this);
        Intake in = new Intake(this);
        shoulder = new Shoulder(this);
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-9, 54, Math.toRadians(90));
        base.setPoseEstimate(startPose);
        base.imu.resetYaw();
        claw.openLift();

        TrajectorySequence trajectorySpecimen = base.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.7);
                    lift.setTarget(-34);
                })
                .back(13, DriveTrainMecanum.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .back(12, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    shoulder.openSh();
                    shoulder.shoulderPosition(.75);
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                //.waitSeconds(1)
                .forward(5)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.1);
                })
                .waitSeconds(0.5)
                .addDisplacementMarker(() -> lift.setTarget(0)) //statement lambda was replaced with expression lambda
                .build();
        TrajectorySequence trajectoryCaptureSecondSpecimen = base.trajectorySequenceBuilder(trajectorySpecimen.end())
                .turn(Math.toRadians(-120))
                .back(34)
                .turn(Math.toRadians(-50))
                .back(12, DriveTrainMecanum.getVelocityConstraint(4,
                                DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .forward(0.7)
                .addDisplacementMarker(() -> {
                    claw.closeLift();
                    sleep(1000);
                    lift.setTarget(-65);
                })
                .build();
        TrajectorySequence trajectoryScoringSecondSpecimen = base.trajectorySequenceBuilder(trajectoryCaptureSecondSpecimen.end())
                .waitSeconds(1)
                .forward(5)
                .turn(Math.toRadians(-135))
                .back(45)
                .turn(Math.toRadians(-50))
                .back(7,DriveTrainMecanum.getVelocityConstraint(15,
                                DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    lift.setTarget(-60);
                    claw.openLift();
                })
                .forward(6)
                .addDisplacementMarker(() -> {
                    lift.setTarget(0);
                })
                .waitSeconds(1)
                .build();
        shoulder.shoulderPosition(0.1);
        shoulder.closeSh();
        in.extensionPosition(Intake.EXTENSION_MIN);
        waitForStart();

        if (isStopRequested()) ;
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
