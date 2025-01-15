package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto.works;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.base_packages.drive.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.driveTrainMecanum.TestDT;

@Autonomous (name = "Only Specimen", group = "Robot")
public class OnlySpecimenScoringAuto extends LinearOpMode {
    private TestDT base;
    private Claw claw;
    private Shoulder shoulder;
    private Lift lift;

    @Override
    public void runOpMode() {
        base = new TestDT(hardwareMap, this);
        claw = new Claw(this);
        lift = new Lift(this);
        shoulder = new Shoulder(this);

        Pose2d startPose = new Pose2d(9,-54, Math.toRadians(90));

        TrajectorySequence trajectoryToSubmarine1 = base.trajectorySequenceBuilder(new Pose2d())
                .forward(19)
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
                    claw.openSh();
                })
                .waitSeconds(1)
                .turn(Math.toRadians(-120))
                .waitSeconds(1)
                .back(50)
                .waitSeconds(1)
                .addDisplacementMarker( () -> {
                    shoulder.shoulderPosition(0);
                })
                .waitSeconds(100)
                .build();

       /* Trajectory trajectoryToSubmarine2 = base.trajectoryBuilder(trajectoryToSubmarine1.end().plus(new Pose2d(Math.toRadians(180))))
                .back(19 , TestDT.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory trajectoryBackward = base.trajectoryBuilder(trajectoryToSubmarine2.end())
                .forward(3, TestDT.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory trajectoryToObservationZone = base.trajectoryBuilder(trajectoryBackward.end().plus(new Pose2d(Math.toRadians(-120))))
                .forward(50, TestDT.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        TestDT.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build(); */
        shoulder.shoulderPosition(0);
        claw.closeSh();
        lift.resetZero();

        waitForStart();

        while(opModeIsActive()) {
            base.followTrajectorySequence(trajectoryToSubmarine1);

        /*    base.followTrajectory(trajectoryToSubmarine1);
            sleep(1000);
            base.turn(Math.toRadians(180));
            sleep(1500);
            shoulder.shoulderPosition(.5);
            sleep(1000);
            base.followTrajectory(trajectoryToSubmarine2);
            shoulder.shoulderPosition(.7);
            sleep(500);
            base.followTrajectory(trajectoryBackward);
            claw.openSh();
            sleep(1000);
            shoulder.shoulderPosition(0);
            sleep(500);
            base.turn(Math.toRadians(-120));
            sleep(1500);
            base.followTrajectory(trajectoryToObservationZone); //не работает */
        }
    }
}
