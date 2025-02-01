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
        shoulder.strongCloseSh();
        shoulder.shoulderPosition(0.1);

        TrajectorySequence trajectory = driveTrain.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.7);
                    lift.setTarget(-32);
                })
                .back(13, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(2)
                .back(12, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.75);
                    shoulder.openSh();
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(2)
                .forward(6)
                .addDisplacementMarker(() -> {
                    sleep(500);
                    shoulder.shoulderPosition(.1);
                })
                .waitSeconds(1)
                //here code for scoring specimen finishes
                .turn(Math.toRadians(65))
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_SIDE);
                    sleep(100);
                })
                .forward(30)
                .turn(Math.toRadians(125), 15, DriveConstants.MAX_ANG_ACCEL)
                //выдвижение + захват
                .back(20,
                        DriveTrainMecanum.getVelocityConstraint(10,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    claw.closeLift();
                    sleep(1000);
                    lift.setTarget(lift.POS_HIGH_SPECIMEN_BEFORE);
                    telemetry.addLine("Здесь клешня на каретке возьмет образец");
                    telemetry.update();
                })
                .forward(5)
                .waitSeconds(1)
                .turn(Math.toRadians(150))
                .build();

        waitForStart();
        if (isStopRequested()) return;
        driveTrain.followTrajectorySequence(trajectory);
        lift.liftMotorPowerDriver.interrupt();
    }
}
