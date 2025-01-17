package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.TestDT;

@Autonomous(name = "Auto Specimen", group = "Robot")
public class AutoSpecimen extends LinearOpMode {
    private TestDT driveTrain;
    //lift

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new TestDT(hardwareMap,this);
        Pose2d startPose = new Pose2d(10,-56,Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);

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
                .splineTo(new Vector2d(52,-53),-90)
                .turn(Math.toRadians(30))
                .waitSeconds(10)
                .addDisplacementMarker(() -> {
                    telemetry.addLine("Здесь клешня на каретке возьмет образец");
                    telemetry.update();
                })
                //here will be capturing of the specimen

                .back(3)
                .turn(Math.toRadians(60))
                .back(46)
                .turn(Math.toRadians(-60))
                .waitSeconds(10)
                .addDisplacementMarker(() -> {
                    telemetry.addLine("Здесь мы зацепим специмен (lift down)");
                    telemetry.update();
                })
                //here will be scoring of the specimen

                .forward(3)
                .splineTo(new Vector2d(52,-53),0)
                .turn(Math.toRadians(90))
                .waitSeconds(1)
                .build();

        waitForStart();

        while (opModeIsActive() & !isStopRequested()){
            driveTrain.followTrajectorySequence(traj);
        }
    }
}
