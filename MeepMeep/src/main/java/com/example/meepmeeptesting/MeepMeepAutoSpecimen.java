package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAutoSpecimen {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(10, -56, 0))
                        .turn(Math.toRadians(-90))
                        .addDisplacementMarker(() -> {
                           /* shoulder.shoulderPosition(.7);
                            lift.setTarget(-33); */
                        })
                        .back(13) //, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .waitSeconds(2)
                        .back(12) //, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .addDisplacementMarker(() -> {
                          /*  shoulder.openSh();
                            shoulder.shoulderPosition(.75);
                            telemetry.addLine("Здесь опустится подъемник");
                            telemetry.update(); */
                        })
                        .waitSeconds(2)
                        .forward(10)
                        .addDisplacementMarker(() -> {
                           // sleep(500);
                           // shoulder.shoulderPosition(.1);
                        })
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            //lift.setTarget(0);
                        })
                        .waitSeconds(1)
                        .turn(Math.toRadians(-110))
                        .waitSeconds(1)
                        .back(34)
                        .waitSeconds(1)
                        .turn(Math.toRadians(-70))
                        .back(5) //, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> {
                            //claw.closeLift();
                        })
                        .waitSeconds(1)
                        .forward(5)
                        .turn(Math.toRadians(-110))
                        .back(34)
                        .turn(Math.toRadians(-70))
                        .back(5)
                        .addDisplacementMarker(() -> {
                            //lift.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE)
                        })
                        .addTemporalMarker(3, () -> {
                            //claw.openLift();
                        })
                        .forward(5)
                        .splineTo(new Vector2d(52, -53), Math.toRadians(0))
                        .turn(Math.toRadians(90))
                        .addDisplacementMarker(() -> {
                           // lift.setTarget(0);
                        })
                        .waitSeconds(1)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
