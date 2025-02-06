package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Auto RED Specimen + Basket", group = "Robot")
public class AutoBasketRED extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-10, -57, Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        shoulder.shoulderPosition(0.1);
        shoulder.strongCloseSh();

        TrajectorySequence traj = driveTrain.trajectorySequenceBuilder(startPose)
                .strafeLeft(5)
                .back(34)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
                    lift.setTarget(Lift.POS_HIGH_BASKET);
                    sleep(3000);
                })
                .waitSeconds(2)
                .turn(Math.toRadians(30))
                .addDisplacementMarker(() -> {
                    shoulder.openSh();
                    sleep(500);
                })
                .waitSeconds(3)
                .forward(6)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_LOWEST);
                    sleep(1000);
                })
                // здесь код со скорингом предзагруженной пробы
                .turn(Math.toRadians(100))
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                    intake.extensionPosition(0.5);
                    intake.brushIntake();
                    sleep(500);
                    intake.extensionPosition(0.05);
                    intake.flipPosition(Intake.FLIP_OUTTAKE);
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update();
                })
                //capturing yellow sample
                .turn(Math.toRadians(-100))
                .waitSeconds(2)
                .back(5)
                //scoring to basket
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.POS_SH_FOR_INTAKE);
                    shoulder.closeSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
                    lift.setTarget(Lift.POS_HIGH_BASKET);
                })
                .waitSeconds(5)
                .addTemporalMarker(5,() -> {
                    shoulder.openSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_LOWEST);
                    shoulder.shoulderPosition(0);
                })
                .forward(5)
                .waitSeconds(3)
                .turn(Math.toRadians(100))
                .addDisplacementMarker(() -> {
                    intake.extensionPosition(0.5);
                    intake.brushIntake();
                    sleep(500);
                    intake.extensionPosition(0.05);
                    intake.flipPosition(Intake.FLIP_OUTTAKE);
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update();
                })
                .turn(Math.toRadians(-100))
                .back(5)
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.POS_SH_FOR_INTAKE);
                    shoulder.closeSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
                    lift.setTarget(Lift.POS_HIGH_BASKET);
                })
                .waitSeconds(5)
                .addTemporalMarker(5,() -> {
                    shoulder.openSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_LOWEST);
                    shoulder.shoulderPosition(0);
                })
                .splineTo(new Vector2d(-25, -9), Math.toRadians(0))
                .build();

        intake.extensionPosition(.05);
        waitForStart();
        if (isStopRequested()) return;
        driveTrain.followTrajectorySequence(traj);
        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
