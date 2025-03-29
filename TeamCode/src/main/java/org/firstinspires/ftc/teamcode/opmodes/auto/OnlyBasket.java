package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "1 ONLY Basket", group = "Robot")
public class OnlyBasket extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        new Claw(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-13, 57, Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        shoulder.shoulderPosition(0.1);
        shoulder.closeSh();
        intake.extensionPosition(Intake.EXT_POS_MIN);
        TrajectorySequence trajectoryToBasket = driveTrain.trajectorySequenceBuilder(startPose)
                .strafeRight(8)
                .forward(13)
                .turn(Math.toRadians(35))
                .build();
        TrajectorySequence trajectoryToPark = driveTrain.trajectorySequenceBuilder(trajectoryToBasket.end())
                .turn(Math.toRadians(45))
                .back(52)
                .turn(Math.toRadians(105))
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
        shoulder.openSh(); ///#НеБойсяПж
        sleep(1000);
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);
        sleep(1000);
        lift.setTarget(0);
        sleep(1500);
        driveTrain.followTrajectorySequence(trajectoryToPark);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
        sleep(1000);
        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
