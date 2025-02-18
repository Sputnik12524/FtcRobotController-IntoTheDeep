package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;

@Autonomous(name="Auto RED Basket", group="Robot")
public class AutoBasketRED extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        Claw claw = new Claw(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-10,-57,Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        shoulder.shoulderPosition(0.1);
        shoulder.strongCloseSh();

        TrajectorySequence traj = driveTrain.trajectorySequenceBuilder(startPose)
                .back(10, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_LOW_SPECIMEN_BEFORE);
                    shoulder.shoulderPosition(.65);
                    telemetry.addLine("Здесь поднимется подъемник");
                    telemetry.update();
                })
                .waitSeconds(1.5)
                .back(17)
                .addDisplacementMarker(() -> {
                    sleep(100);
                   // shoulder.shoulderPosition(.65);
                    shoulder.openSh();
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(4)
                .forward(4)
                .addDisplacementMarker(() -> {
                    sleep(1000);
                    shoulder.shoulderPosition(.1);
                })
                .waitSeconds(1)
                .turn(Math.toRadians(-45))
                .splineTo(new Vector2d(-52,-40), Math.toRadians(90))
                .turn(Math.toRadians(-25))
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

                .back(4)
                //scoring to basket

                .back(7)
                .waitSeconds(5)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(Shoulder.POS_SH_FOR_INTAKE);
                    shoulder.closeSh();
                    shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
                    sleep(1000);
                    lift.setTarget(Lift.POS_HIGH_BASKET);
                    telemetry.addLine("Здесь поднимется подъемник с наклоненным плечом");
                    telemetry.update();
                    sleep(500);
                    claw.openLift();
                    telemetry.addLine("Здесь откроется клешня");
                    telemetry.update();
                    sleep(500);
                })
                .forward(3)
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_SIDE);
                    shoulder.shoulderPosition(0.4);
                })
                .splineTo(new Vector2d(-25,-9),Math.toRadians(0))
                .build();
        intake.extensionPosition(.05);
        waitForStart();
        if(isStopRequested()) return;
        driveTrain.followTrajectorySequence(traj);
        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
