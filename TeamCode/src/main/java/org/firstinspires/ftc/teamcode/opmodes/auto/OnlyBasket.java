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

@Autonomous(name="1 ONLY Basket", group="Robot")
public class OnlyBasket extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        Claw claw = new Claw(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-13,57,Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        shoulder.shoulderPosition(0.1);
        shoulder.strongCloseSh();
        intake.extensionPosition(intake.EXTENSION_MIN);

        TrajectorySequence traj = driveTrain.trajectorySequenceBuilder(startPose)
                .strafeRight(3)
                .back(34)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(shoulder.POS_SH_BASKET);
                    lift.setTarget(lift.POS_HIGH_BASKET);
                    sleep(3000);
                })
                .waitSeconds(2)
                .turn(Math.toRadians(30))
                .addDisplacementMarker(() -> {
                    shoulder.openSh();
                    sleep(500);
                })
                .waitSeconds(6)
                .forward(5)
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_LOWEST);
                    sleep(1000);

                })
                .build();
        intake.extensionPosition(.05);
        waitForStart();
        if(isStopRequested()) return;
        driveTrain.followTrajectorySequence(traj);
        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
