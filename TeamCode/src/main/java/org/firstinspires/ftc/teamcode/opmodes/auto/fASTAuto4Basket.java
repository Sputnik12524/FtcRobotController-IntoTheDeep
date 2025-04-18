package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "0 FAST 4 Auto Basket", group = "Robot")
public class fASTAuto4Basket extends LinearOpMode {

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
                    shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
                })
                .strafeRight(12)
                .forward(18)
                .turn(Math.toRadians(76))
                .build();

        intake.extensionPosition(.05);

        waitForStart();

        if (isStopRequested()) return;

        driveTrain.followTrajectorySequence(trajectoryToBasket);
        shoulder.openSh();
        sleep(450);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        lift.setTarget(0);

        intake.needTake();
        sleep(650);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .back(3)
                .build());
        intake.needOuttake();
        sleep(300);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .forward(3)
                .build());
        driveTrain.turn(Math.toRadians(-17));
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(450);
        shoulder.closeSh();
        sleep(100);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        sleep(950);
        shoulder.openSh();
        sleep(450);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        lift.setTarget(0);

        driveTrain.turn(Math.toRadians(45));
        intake.needTake();
        sleep(650);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .back(3.5)
                .build());
        intake.needOuttake();
        sleep(300);
        driveTrain.followTrajectorySequence(driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .forward(3.5)
                .build());
        driveTrain.turn(Math.toRadians(-39));
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(450);
        shoulder.closeSh();
        sleep(100);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        sleep(950);
        shoulder.openSh();
        sleep(450);
        shoulder.shoulderPosition(0.42);
        sleep(500);
        lift.setTarget(0);
        sleep(1000);

        driveTrain.turn(Math.toRadians(67));
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
        sleep(450);
        shoulder.closeSh();
        sleep(100);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        sleep(950);
        shoulder.openSh();
        sleep(450);
        shoulder.shoulderPosition(0.69);
        sleep(400);
        lift.setTarget(0);
        sleep(1600);

        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
