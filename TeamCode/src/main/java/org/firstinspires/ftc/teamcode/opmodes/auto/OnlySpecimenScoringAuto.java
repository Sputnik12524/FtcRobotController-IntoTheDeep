package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;

@Autonomous (name = "Only Specimen", group = "Robot")
public class OnlySpecimenScoringAuto extends LinearOpMode {
    private Claw claw;
    private Shoulder shoulder;

    @Override
    public void runOpMode() {
        DriveTrainMecanum base = new DriveTrainMecanum(hardwareMap, this);
        claw = new Claw(this);
        Lift lift = new Lift(this);
        shoulder = new Shoulder(this);

        Pose2d startPose = new Pose2d(9,-54, Math.toRadians(90));
        base.setPoseEstimate(startPose);

        TrajectorySequence trajectoryToSubmarine1 = base.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.7);
                    lift.setTarget(-32);
                })
                .back(10, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(2)
                .waitSeconds(1.5)
                .back(18, DriveTrainMecanum.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    shoulder.openSh();
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(2)
                .forward(4)
                .addDisplacementMarker(() -> {
                    sleep(500);
                    shoulder.shoulderPosition(.1);
                })
                .waitSeconds(1)
                .build();

        shoulder.shoulderPosition(0);
        shoulder.closeSh();
        lift.resetZero();

        waitForStart();

        if (isStopRequested());
        base.followTrajectorySequence(trajectoryToSubmarine1);
    }
}
