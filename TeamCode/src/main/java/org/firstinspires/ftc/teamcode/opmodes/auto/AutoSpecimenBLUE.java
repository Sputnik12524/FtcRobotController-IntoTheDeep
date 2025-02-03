package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "BLUE Auto Specimen", group = "Robot")
public class AutoSpecimenBLUE extends LinearOpMode {
    public Claw claw;
    public Shoulder shoulder;
    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum base = new DriveTrainMecanum(hardwareMap, this);
        claw = new Claw(this);
        Lift lift = new Lift(this);
        Intake in = new Intake(this);
        shoulder = new Shoulder(this);
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(9,-54, Math.toRadians(90));
        base.setPoseEstimate(startPose);
        base.imu.resetYaw();

        TrajectorySequence trajectoryToSubmarine1 = base.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.7);
                    lift.setTarget(-33);
                })
                .back(13, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(2)
                .back(12, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    shoulder.openSh();
                    shoulder.shoulderPosition(.75);
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(2)
                .forward(10)
                .addDisplacementMarker(() -> {
                    sleep(500);
                    shoulder.shoulderPosition(.1);
                })
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                    lift.setTarget(0);
                })
                .waitSeconds(1)
                .turn(Math.toRadians(-120))
                .waitSeconds(1)
                .back(34)
                .waitSeconds(1)
                .turn(Math.toRadians(45))
                .waitSeconds(1)
                .back(15, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    claw.closeLift();
                })
                .waitSeconds(1)
                .build();

        shoulder.shoulderPosition(0);
        shoulder.closeSh();
        in.extensionPosition(in.EXTENSION_MIN);
        waitForStart();

        if(isStopRequested());
        base.followTrajectorySequence(trajectoryToSubmarine1);

        lift.liftMotorPowerDriver.interrupt();
    }
}
