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

@Autonomous(name="AutoBasket", group="Robot")
public class AutoBasket extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        Claw claw = new Claw(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);
        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-10,-57,Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        TrajectorySequence traj = driveTrain.trajectorySequenceBuilder(startPose)
                .back(19, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_HIGH_SPECIMEN_BEFORE);
                    telemetry.addLine("Здесь поднимется подъемник");
                    telemetry.update();
                })
                .waitSeconds(1.5)
                .back(14)
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_HIGH_SPECIMEN_AFTER);
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                    claw.openLift();
                    telemetry.addLine("Здесь откроется клешня");
                    telemetry.update();
                })
                .forward(4)
                .waitSeconds(1)
                .turn(Math.toRadians(-45))
                .forward(30)
                .splineTo(new Vector2d(-52,-40), 90)
                .turn(Math.toRadians(-45))
                .waitSeconds(10)
                .addDisplacementMarker(() -> {
                    intake.extensionPosition(0.5);
                    intake.brushIntake();
                    sleep(500);
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update();
                })
                //capturing yellow sample

                .back(3)
                //scoring to basket

                .turn(Math.toRadians(-20))
                .back(7)
                .waitSeconds(5)
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_HIGH_BASKET);
                    telemetry.addLine("Здесь поднимется подъемник с наклоненным плечом");
                    telemetry.update();
                    claw.openLift();
                    telemetry.addLine("Здесь откроется клешня");
                    telemetry.update();
                    sleep(1000);
                    lift.setTarget(lift.POS_LOWEST);
                })
                .splineTo(new Vector2d(-25,-9),0)
                .build();
        shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
        //здесь нужно поставить плечо в позицию, с которой скорим в корзины
        waitForStart();
        if(isStopRequested()) return;
        driveTrain.followTrajectorySequence(traj);
        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
