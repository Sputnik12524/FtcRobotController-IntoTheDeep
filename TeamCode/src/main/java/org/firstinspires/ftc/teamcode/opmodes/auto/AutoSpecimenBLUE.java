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

        Pose2d startPose = new Pose2d(9, -54, Math.toRadians(90));
        base.setPoseEstimate(startPose);
        base.imu.resetYaw();
        claw.openLift();

        TrajectorySequence trajectorySpecimen = base.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.7);
                    lift.setTarget(-33);
                })
                .back(13, DriveTrainMecanum.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(2)
                .back(12, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    shoulder.openSh();
                    shoulder.shoulderPosition(.75);
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(1)
                .forward(5)
                .addDisplacementMarker(() -> {
                    sleep(500);
                    shoulder.shoulderPosition(.1);
                })
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    lift.setTarget(0);
                })
                .build();
        TrajectorySequence trajectoryCaptureSecondSpecimen = base.trajectorySequenceBuilder(trajectorySpecimen.end())
                .waitSeconds(1)
                .turn(Math.toRadians(-120))
                .back(34)
                .waitSeconds(1)
                .turn(Math.toRadians(-60))
                .back(3, DriveTrainMecanum.getVelocityConstraint(15,
                                DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    claw.closeLift();
                })
                .forward(5)
                .build();
        TrajectorySequence trajectoryScoringSecondSpecimen = base.trajectorySequenceBuilder(trajectoryCaptureSecondSpecimen.end())
                .waitSeconds(1)
                .turn(Math.toRadians(-120))
                .back(34)
                .turn(Math.toRadians(-90))
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE);
                })
                .back(8)//wasn't tested
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_HIGH_SPECIMEN_AFTER);
                    claw.openLift();
                })
                .forward(5)
                .waitSeconds(0.5)
                .build();
        //если не успеем паркануться, то перенести лифт таргет 0 в предыдущую траджектори, а следующую убрать
        TrajectorySequence trajectoryEnd = base.trajectorySequenceBuilder(trajectoryScoringSecondSpecimen.end())
                .addDisplacementMarker(() -> {
                    lift.setTarget(0);
                })
                .waitSeconds(1)
                .turn(Math.toRadians(45))
                .forward(40)
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
        base.followTrajectorySequence(trajectoryEnd);
        sleep(1000);
        lift.liftMotorPowerDriver.interrupt();
    }
}
