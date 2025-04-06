package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.modules.*;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "4+Park BLUE Auto Basket", group = "Robot")
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
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_HIGH_BASKET);
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
                })
                .strafeRight(13)
                .forward(17)
                .turn(Math.toRadians(80))
                .build();

        intake.extensionPosition(.05);

        waitForStart();

        if (isStopRequested()) return;

        driveTrain.followTrajectorySequence(trajectoryToBasket);
        shoulder.openSh();
        sleep(500);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        lift.setTarget(0);

        intake.needTake();
        sleep(600);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .back(3)
                .build());
        intake.needOuttake();
        sleep(500);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .forward(3)
                .build());
        driveTrain.turn(Math.toRadians(-20));
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(500);
        shoulder.closeSh();
        sleep(200);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1600);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        sleep(990);
        shoulder.openSh();
        sleep(500);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        lift.setTarget(0);

        driveTrain.turn(Math.toRadians(44));
        intake.needTake();
        sleep(600);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .back(3)
                .build());
        intake.needOuttake();
        sleep(500);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .forward(3)
                .build());
        driveTrain.turn(Math.toRadians(-41));
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(500);
        shoulder.closeSh();
        sleep(200);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1600);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        sleep(990);
        shoulder.openSh();
        sleep(500);
        shoulder.shoulderPosition(0.42);
        sleep(1000);
        lift.setTarget(0);
        sleep(1000);

        driveTrain.turn(Math.toRadians(64));
        intake.needTake();
        sleep(600);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .back(5)
                .build());
        intake.needOuttake();
        sleep(500);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .forward(5)
                .build());
        driveTrain.turn(Math.toRadians(-65));
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(500);
        shoulder.closeSh();
        sleep(200);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1600);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        sleep(990);
        shoulder.openSh();
        sleep(500);
        shoulder.shoulderPosition(0.69);
        sleep(500);
        lift.setTarget(0);
        sleep(1100);

        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
