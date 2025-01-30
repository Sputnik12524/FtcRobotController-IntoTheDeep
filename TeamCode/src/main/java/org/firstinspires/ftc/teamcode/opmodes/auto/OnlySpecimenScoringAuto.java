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
                .forward(19, DriveTrainMecanum.getVelocityConstraint(25,DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.5);
                })
                .turn(Math.toRadians(180))
                .waitSeconds(1)
                .back(19)
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.7);
                })
                .forward(4)
                .addDisplacementMarker( () -> {
                    shoulder.openSh();
                })
                .waitSeconds(1)
                .turn(Math.toRadians(-120))
                .waitSeconds(1)
                .back(50)
                .waitSeconds(1)
                .addDisplacementMarker( () -> {
                    shoulder.shoulderPosition(0);
                })
                .build();

        shoulder.shoulderPosition(0);
        shoulder.closeSh();
        lift.resetZero();

        waitForStart();

        if (isStopRequested());
        base.followTrajectorySequence(trajectoryToSubmarine1);
    }
}
