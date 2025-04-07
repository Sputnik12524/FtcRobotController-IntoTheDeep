package org.firstinspires.ftc.teamcode.opmodes.auto.disabled;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;

@Autonomous (name = "Only Specimen", group = "Robot")
@Disabled
public class OnlySpecimen extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum base = new DriveTrainMecanum(hardwareMap, this);
        Claw cl = new Claw(this);
        Lift lift = new Lift(this);
        Intake in = new Intake(this);
        Shoulder shoulder = new Shoulder(this);
        lift.liftMotorPowerDriver.start();
        Pose2d startPose = new Pose2d(9,-54, Math.toRadians(90));
        base.setPoseEstimate(startPose);

        TrajectorySequence trajectoryToSubmarine1 = base.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> lift.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE))
                .forward(13, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .forward(12, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    lift.setTarget(Lift.POS_HIGH_SPECIMEN_AFTER);
                    cl.closeLift();
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(2)
                .back(10)
                .waitSeconds(2)
                .addDisplacementMarker(() -> lift.setTarget(0))
                .waitSeconds(2)
                .turn(Math.toRadians(65))
                .back(34)

                .build();

        shoulder.shoulderPosition(0.1);
        shoulder.closeSh();
        in.extensionPosition(Intake.EXT_POS_MIN);
        cl.openLift();

        waitForStart();

        if (isStopRequested()) return;
        base.followTrajectorySequence(trajectoryToSubmarine1);

        lift.liftMotorPowerDriver.interrupt();
    }
}
