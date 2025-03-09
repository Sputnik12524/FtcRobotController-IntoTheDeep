package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.modules.*;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "2 RED Auto Basket", group = "Robot")
public class AutoBasketRED extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        new Claw(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-10, -57, Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        shoulder.shoulderPosition(0.1);
        shoulder.strongCloseSh();
        intake.extensionPosition(Intake.EXT_POS_MIN);

        TrajectorySequence trajectoryToBasket = driveTrain.trajectorySequenceBuilder(startPose)
                .strafeRight(7)
                .back(37)
                .turn(Math.toRadians(30))
                .build();
        Trajectory trajectoryToSample1 = driveTrain.trajectoryBuilder(trajectoryToBasket.end()).forward(2).build();
        Trajectory trajectoryBack = driveTrain.trajectoryBuilder(trajectoryToSample1.end()).back(2).build();
        TrajectorySequence trajectoryToPark = driveTrain.trajectorySequenceBuilder(trajectoryBack.end())
                .turn(Math.toRadians(45))
                .forward(52)
                .turn(Math.toRadians(105))
                .back(13)
                .build();
        intake.extensionPosition(.05);

        waitForStart();

        if (isStopRequested()) return;

        driveTrain.followTrajectorySequence(trajectoryToBasket);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(2000);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
        sleep(1000);
        shoulder.openSh();
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        sleep(1000);
        lift.setTarget(0);
        sleep(1000);

        driveTrain.turn(Math.toRadians(45));
        intake.needTake();
        driveTrain.followTrajectory(trajectoryToSample1);
        intake.needOuttake();
        driveTrain.followTrajectory(trajectoryBack);
        lift.setTarget(Lift.POS_HIGH_BASKET);
        sleep(2000);
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
