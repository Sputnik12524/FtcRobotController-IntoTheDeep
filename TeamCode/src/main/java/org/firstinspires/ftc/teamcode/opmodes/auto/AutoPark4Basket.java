package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.modules.*;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "4+Park  BLUE Auto Basket", group = "Robot")
public class AutoPark4Basket extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(32, 57, Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        shoulder.shoulderPosition(0.1);
        shoulder.closeSh();
        intake.extensionPosition(Intake.EXT_POS_MIN);

        TrajectorySequence trajectoryToBasket = driveTrain.trajectorySequenceBuilder(startPose)
                .strafeRight(8)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_HIGH_BASKET);
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
                })
                .forward(10)
                .turn(Math.toRadians(35))
                .build();
        TrajectorySequence trajectoryToSample1 = driveTrain.trajectorySequenceBuilder(trajectoryToBasket.end().plus(new Pose2d(0, 0, Math.toRadians(35))))
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
                    sleep(500);
                    shoulder.closeSh();
                    sleep(500);
                })
                .turn(Math.toRadians(-80))
                .build();
        TrajectorySequence trajectoryToSample2 = driveTrain.trajectorySequenceBuilder(trajectoryToBasket.end().plus(new Pose2d(0, 0, Math.toRadians(35))))
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
                    sleep(500);
                    shoulder.closeSh();
                    sleep(500);
                })
                .turn(Math.toRadians(-110))

                .build();
        TrajectorySequence trajectoryBack = driveTrain.trajectorySequenceBuilder(trajectoryToSample1.end())
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
                    sleep(500);
                    shoulder.closeSh();
                    sleep(500);
                })
                .turn(Math.toRadians(-110))

                .build();
        TrajectorySequence trajectoryToPark = driveTrain.trajectorySequenceBuilder(trajectoryBack.end())
                .back(45)
                .turn(Math.toRadians(110))
                .forward(13)
                .build();
        intake.extensionPosition(.05);

        waitForStart();

        if (isStopRequested()) return;

        driveTrain.followTrajectorySequence(trajectoryToBasket);
        sleep(1000);
        shoulder.openSh();
        sleep(100);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        sleep(1000);
        lift.setTarget(0);
        sleep(1000);

        driveTrain.turn(Math.toRadians(80));
        sleep(100);
        intake.needTake();
        sleep(1000);
        intake.needOuttake();
        sleep(2000);

        driveTrain.followTrajectorySequence(trajectoryToSample1);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1000);
        shoulder.shoulderPosition(.65);
        sleep(1000);
        shoulder.openSh();
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        sleep(1000);
        lift.setTarget(0);

        driveTrain.turn(Math.toRadians(110));
        sleep(100);
        intake.needTake();
        sleep(1000);
        intake.needOuttake();
        sleep(2000);
        driveTrain.followTrajectorySequence(trajectoryBack);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1000);
        shoulder.shoulderPosition(.65);
        sleep(1500);
        shoulder.openSh();
        sleep(1000);
        shoulder.shoulderPosition(0.42);
        sleep(1000);
        lift.setTarget(0);


        driveTrain.followTrajectorySequence(trajectoryToPark);
        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
