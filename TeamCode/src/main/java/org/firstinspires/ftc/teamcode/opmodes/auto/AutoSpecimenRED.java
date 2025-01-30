package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;

@Autonomous(name = "Auto RED Specimen", group = "Robot")
public class AutoSpecimenRED extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        Claw claw = new Claw(this);
        Shoulder shoulder = new Shoulder(this);
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(10, -57, Math.toRadians(-90));
        driveTrain.setPoseEstimate(startPose);
        claw.strongCloseSh();
        shoulder.shoulderPosition(0.1);

        TrajectorySequence trajectory = driveTrain.trajectorySequenceBuilder(startPose)
                .back(10,
                        DriveTrainMecanum.getVelocityConstraint(DriveConstants.MAX_VEL,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_LOW_SPECIMEN_BEFORE);
                    shoulder.shoulderPosition(.425);
                    telemetry.addLine("Здесь поднимется подъемник");
                    telemetry.update();
                })
                .waitSeconds(1)
                .back(11)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(0.65);
                    claw.openLift();
                    telemetry.addLine("Здесь опустится подъемник");
                     telemetry.update();
                })
                .waitSeconds(3)
                .forward(4)
                .waitSeconds(1)
                .splineTo(new Vector2d(40, -40), Math.toRadians(90))
                //выдвижение + захват
                .turn(Math.toRadians(25))
                .back(14,
                        DriveTrainMecanum.getVelocityConstraint(25,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(5)
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_SIDE);
                    sleep(100);
                    claw.closeLift();
                    sleep(1000);
                    lift.setTarget(lift.POS_HIGH_SPECIMEN_BEFORE);
                    telemetry.addLine("Здесь клешня на каретке возьмет образец");
                    telemetry.update();
                })
                .forward(3)
                .splineTo(new Vector2d(10,-40), Math.toRadians(-90))
                .back(7)
                .waitSeconds(5)
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_HIGH_SPECIMEN_AFTER);
                    telemetry.addLine("Здесь мы зацепим специмен (lift down)");
                    telemetry.update();
                    sleep(1000);
                    claw.openLift();
                })
                .forward(5)
                .splineTo(new Vector2d(52, -53), Math.toRadians(0))
                .turn(Math.toRadians(90))
                .waitSeconds(1)
                .build();

        waitForStart();
        if (isStopRequested()) return;
        driveTrain.followTrajectorySequence(trajectory);
        lift.liftMotorPowerDriver.interrupt();
    }
}
