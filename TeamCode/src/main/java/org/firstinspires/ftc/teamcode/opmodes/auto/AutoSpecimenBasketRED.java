package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;

@Autonomous(name="RED Auto Specimen + Basket", group="Robot")
public class AutoSpecimenBasketRED extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-10,-57,Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        shoulder.shoulderPosition(0.1);
        shoulder.strongCloseSh();

        TrajectorySequence trajectorySpecimen = driveTrain.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.7);
                    lift.setTarget(-33);
                })
                .back(13, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(2)
                .back(12, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(     () -> {
                    shoulder.openSh();
                    shoulder.shoulderPosition(.75);
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(2)
                .forward(25)
                .addDisplacementMarker(() -> {
                    sleep(500);
                    shoulder.shoulderPosition(.1);
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> lift.setTarget(0))
                .splineTo(new Vector2d(-54, -55), Math.toRadians(55))
                .build();
<<<<<<< HEAD

        Trajectory trajectoryToSample1 = driveTrain.trajectoryBuilder
                        (trajectorySpecimen.end().plus(new Pose2d(0,0, Math.toRadians(35))))
                .forward(1)
                .build();
        Trajectory trajectoryBack = driveTrain.trajectoryBuilder(trajectoryToSample1.end()).back(1).build();
        TrajectorySequence trajectoryToPark = driveTrain.trajectorySequenceBuilder(trajectoryBack.end())
                .turn(Math.toRadians(45))
                .forward(52)
                .turn(Math.toRadians(105))
                .back(13)
=======
        TrajectorySequence trajectoryFirstSample = driveTrain.trajectorySequenceBuilder(trajectorySpecimen.end())
                .forward(34)
                .turn(Math.toRadians(-45))
                .splineTo(new Vector2d(-52,-40), Math.toRadians(90))
                .turn(Math.toRadians(-25))
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                    intake.extensionPosition(0.5);
                    intake.brushIntake();
                    sleep(500);
                    intake.extensionPosition(0.05);
                    intake.flipPosition(Intake.FLIP_POS_FOR_OUTTAKE);
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update();
                })
                //capturing yellow sample
                .turn(Math.toRadians(-100))
                .waitSeconds(2)
                .back(5)
                //scoring to basket
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
                    shoulder.closeSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
                    lift.setTarget(Lift.POS_HIGH_BASKET);
                })
                .waitSeconds(5)
                .addTemporalMarker(5, shoulder::openSh)
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_LOWEST);
                    shoulder.shoulderPosition(0);
                })
                .forward(5)
                .waitSeconds(3)
                .build();
        TrajectorySequence trajectorySecondSample = driveTrain.trajectorySequenceBuilder(trajectoryFirstSample.end())
                .turn(Math.toRadians(100))
                .addDisplacementMarker(() -> {
                    intake.extensionPosition(0.5);
                    intake.brushIntake();
                    sleep(500);
                    intake.extensionPosition(0.05);
                    intake.flipPosition(Intake.FLIP_POS_FOR_OUTTAKE);
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update();
                })
                .turn(Math.toRadians(-100))
                .back(5)
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
                    shoulder.closeSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
                    lift.setTarget(Lift.POS_HIGH_BASKET);
                })
                .waitSeconds(5)
                .addTemporalMarker(5, shoulder::openSh)
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_LOWEST);
                    shoulder.shoulderPosition(0);
                })
                .splineTo(new Vector2d(-25, -9), Math.toRadians(0))
>>>>>>> teleop
                .build();

        intake.extensionPosition(.05);
        waitForStart();
        if (isStopRequested()) return;
        driveTrain.followTrajectorySequence(trajectorySpecimen);
        sleep(1000);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(2000);
        shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
        sleep(1000);
        shoulder.openSh();
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.INITIAL_POSITION);
        sleep(1000);
        lift.setTarget(0);
        sleep(1000);

        driveTrain.turn(Math.toRadians(35));
        sleep(1000);
        intake.needTake();
        sleep(1000);
        driveTrain.followTrajectory(trajectoryToSample1);
        sleep(1000);
        intake.needOuttake();

        driveTrain.followTrajectory(trajectoryBack);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(2000);
        shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
        sleep(1000);
        shoulder.openSh();
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.INITIAL_POSITION);
        sleep(1000);
        lift.setTarget(0);

        driveTrain.followTrajectorySequence(trajectoryToPark);

        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
