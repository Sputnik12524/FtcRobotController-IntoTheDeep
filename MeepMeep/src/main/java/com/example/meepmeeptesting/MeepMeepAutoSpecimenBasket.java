package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAutoSpecimenBasket {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

                .setConstraints(52, 52, Math.toRadians(180), Math.toRadians(180), 13)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(12, 57, 0))
                        .turn(Math.toRadians(90))
                        .addDisplacementMarker(() -> {
                        })
                        .back(13)//, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        //  DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .waitSeconds(2)
                        .back(12)//, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        //DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .addDisplacementMarker(() -> {
                        })
                        .waitSeconds(2)
                        .forward(10)
                        .addDisplacementMarker(() -> {
                        })
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            // lift.setTarget(0);
                        })
                        .waitSeconds(4)
                       // .turn(Math.toRadians(65))
                        // TrajectorySequence trajectoryFirstSample = driveTrain.trajectorySequenceBuilder(trajectorySpecimen.end())
                        .forward(5)
                        //.turn(Math.toRadians())
                        .splineTo(new Vector2d(52, 52), Math.toRadians(90))
                        .turn(Math.toRadians(-195))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                        })
                        //capturing yellow sample
                        .turn(Math.toRadians(-25))
                        .waitSeconds(2)
                        .back(5)
                        //scoring to basket
                        .addDisplacementMarker(() -> {
                        })
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                        })
                        .waitSeconds(3)
                        .addTemporalMarker(5, () -> {
                            // shoulder.openSh();
                        })
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                        })
                        .forward(5)
                        .waitSeconds(3)
                        //TrajectorySequence trajectorySecondSample = driveTrain.trajectorySequenceBuilder(trajectoryFirstSample.end())
                        .turn(Math.toRadians(60))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                        })
                        .turn(Math.toRadians(-60))
                        .back(5)
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                        })
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                        })
                        .waitSeconds(5)
                        .addTemporalMarker(5, () -> {
                            // shoulder.openSh();
                        })
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                        })
                        .forward(46)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
