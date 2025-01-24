package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;

@Autonomous(name="AutoBasket", group="Robot")
public class AutoBasket extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);

        Pose2d startPose = new Pose2d(-10,-57,Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

        TrajectorySequence traj = driveTrain.trajectorySequenceBuilder(startPose)
                .back(19, driveTrain.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        driveTrain.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    telemetry.addLine("Здесь поднимется подъемник");
                    telemetry.update();
                })
                .waitSeconds(1)
                .back(14)
                .addDisplacementMarker(() -> {
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
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
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update();
                })
                //capturing yellow sample

                .back(3)
                .addDisplacementMarker(() -> {
                    telemetry.addLine("Здесь поднимется подъемник с наклоненным плечом");
                    telemetry.update();
                })
                //scoring to basket

                .turn(Math.toRadians(-20))
                .waitSeconds(5)
                .addDisplacementMarker(() -> {
                    telemetry.addLine("Здесь откроется клешня");
                    telemetry.update();
                })
                .splineTo(new Vector2d(-25,-9),0)
                .build();
        //здесь нужно поставить плечо в позицию, с которой скорим в корзины
        waitForStart();
        if(isStopRequested()) return;
        driveTrain.followTrajectorySequence(traj);

    }
}
