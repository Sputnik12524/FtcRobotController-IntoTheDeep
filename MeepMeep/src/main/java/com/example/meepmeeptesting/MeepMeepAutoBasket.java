package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAutoBasket {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

                .setConstraints(52, 52, Math.toRadians(180), Math.toRadians(180), 13)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-10, 57, 0))
                        .turn(Math.toRadians(90))
                        .back(10)
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(1.5)
                        .back(17)
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(4)
                        .forward(4)
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(1)
                        .turn(Math.toRadians(-45))
                        .splineTo(new Vector2d(-52,40), Math.toRadians(90))
                        .turn(Math.toRadians(-25))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {

                        })
                        //capturing yellow sample

                        .back(4)
                        //scoring to basket

                        .back(7)
                        .waitSeconds(5)
                        .addDisplacementMarker(() -> {

                        })
                        .forward(3)
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> {

                        })
                        .splineTo(new Vector2d(-25,9),Math.toRadians(0))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
