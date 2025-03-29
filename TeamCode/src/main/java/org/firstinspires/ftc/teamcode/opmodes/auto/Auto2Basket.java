package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "1 + 1 Auto Basket", group = "Robot")
public class Auto2Basket extends LinearOpMode {

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
        shoulder.setClawPosition(.46);
        intake.extensionPosition(Intake.EXT_POS_MIN);

        TrajectorySequence trajectoryToBasket = driveTrain.trajectorySequenceBuilder(startPose)
                .strafeRight(8)
                .forward(13)
                .turn(Math.toRadians(35))
                .build();
        Trajectory trajectoryToSample1 = driveTrain.trajectoryBuilder
                        (trajectoryToBasket.end().plus(new Pose2d(0,0, Math.toRadians(35))))
                .back(1)
                .build();
        Trajectory trajectoryBack = driveTrain.trajectoryBuilder(trajectoryToSample1.end()).forward(1).build();
        TrajectorySequence trajectoryToPark = driveTrain.trajectorySequenceBuilder(trajectoryBack.end())
                .back(45)
                .turn(Math.toRadians(110))
                .forward(13)
                .build();
        intake.extensionPosition(.05);

        waitForStart();

        if (isStopRequested()) return;

        driveTrain.followTrajectorySequence(trajectoryToBasket);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
        sleep(1000);
        shoulder.openSh();
        sleep(100);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        sleep(1000);
        lift.setTarget(0);
        sleep(1000);

        driveTrain.turn(Math.toRadians(47));
        sleep(100);
        intake.needTake();
        sleep(1000);
        intake.needOuttake();
        sleep(2000);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_INTAKE);
        sleep(500);
        shoulder.setClawPosition(.46);
        sleep(500);
        driveTrain.turn(Math.toRadians(-47));
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
        sleep(1000);
        shoulder.openSh();
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        sleep(1000);
        lift.setTarget(0);

        driveTrain.followTrajectorySequence(trajectoryToPark);
        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
