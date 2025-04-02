package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.modules.*;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
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
                .strafeRight(14)
                .forward(15)
                .turn(Math.toRadians(55))
                .build();
        TrajectorySequence trajectoryToSample1 = driveTrain.trajectorySequenceBuilder(trajectoryToBasket.end())
                .back(2)
                .build();

        TrajectorySequence trajectoryBack = driveTrain.trajectorySequenceBuilder(trajectoryToBasket.end())
                .forward(-1)
                .build();
 ;
        TrajectorySequence trajectoryToPark = driveTrain.trajectorySequenceBuilder(trajectoryBack.end())
                .back(45)
                .turn(Math.toRadians(110))
                .forward(13)
                .build();
        intake.extensionPosition(.05);

        waitForStart();

        if (isStopRequested()) return;

        driveTrain.followTrajectorySequence(trajectoryToBasket);
        shoulder.openSh();
        sleep(500);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        lift.setTarget(0);
        sleep(600);

        driveTrain.turn(Math.toRadians(38));
        sleep(100);
        intake.needTake();
        sleep(1000);
        intake.needOuttake();
        sleep(1500);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(300);
        shoulder.setClawPosition(.46);
        sleep(1000);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(100);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        driveTrain.turn(Math.toRadians(-38));
        sleep(100);
        shoulder.openSh();
        sleep(200);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);

        lift.setTarget(0);

        driveTrain.turn(Math.toRadians(61));
        sleep(100);
        intake.needTake();
        sleep(1000);
        intake.needOuttake();
        sleep(1500);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(300);
        shoulder.setClawPosition(.46);
        sleep(1000);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(100);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        driveTrain.turn(Math.toRadians(-61));
        sleep(100);
        shoulder.openSh();
        sleep(200);
        shoulder.shoulderPosition(0.42);
        sleep(1000);
        lift.setTarget(0);
        sleep(1000);

        driveTrain.followTrajectorySequence(trajectoryToSample1);

        driveTrain.turn(Math.toRadians(78));
        sleep(100);
        intake.needTake();
        sleep(1000);
        intake.needOuttake();
        sleep(1500);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(300);
        shoulder.setClawPosition(.46);
        sleep(1000);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(100);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET_AUTO);
        driveTrain.turn(Math.toRadians(-78));
        sleep(100);
        shoulder.openSh();
        sleep(200);
        shoulder.shoulderPosition(0.42);
        sleep(1000);
        lift.setTarget(0);
        sleep(1000);

        /*
        Here will be third sample capturing and dropping
         */

        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
