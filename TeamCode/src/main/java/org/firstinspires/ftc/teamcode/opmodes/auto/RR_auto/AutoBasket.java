package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.TestDT;

@Autonomous(name="AutoBasket", group="Robot")
public class AutoBasket extends LinearOpMode {

    private DriveTrainMecanum driveTrain;

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Pose2d startPose = new Pose2d(-10,-56,Math.toRadians(90));
        TrajectorySequence traj = driveTrain.trajectorySequenceBuilder(startPose)
                .forward(19, TestDT.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .turn(Math.toRadians(180))
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
                .turn(Math.toRadians(-55))
                .splineTo(new Vector2d(-53,-45), 90)
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
        //нужно поставить плечо в позицию, с которой скорим в корзины
        waitForStart();

        while(opModeIsActive() & !isStopRequested()) {
            driveTrain.followTrajectorySequence(traj);
        }
    }
}
