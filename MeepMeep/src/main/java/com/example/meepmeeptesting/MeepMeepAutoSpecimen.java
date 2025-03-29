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
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-10, 56, 0))
                        .turn(Math.toRadians(-90))
                        .addDisplacementMarker(() -> {
                        })
                        .forward(10)
                        .forward(15)
                        .addDisplacementMarker(() -> {
                        })
                        .waitSeconds(1)
                        .back(10)
                        .waitSeconds(0.5)
                        .turn(Math.toRadians(60))
                        .back(38)
                        .turn(Math.toRadians(-60))
                        .back(2)
                        .addDisplacementMarker(() -> {
                        })
                        .waitSeconds(1)
                        .forward(10)
                        .turn(Math.toRadians(-110))
                        .back(35)
                        .turn(Math.toRadians(-70))
                        .back(8)
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> {
                        })
                        .splineTo(new Vector2d(-52, 55), Math.toRadians(180))
                        .waitSeconds(1)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}