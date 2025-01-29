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
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-10, -57, 0))
                        .turn(Math.toRadians(-90))
                        .back(19)
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(1.5)
                        .back(14)
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {

                        })
                        .forward(4)
                        .waitSeconds(1)
                        .turn(Math.toRadians(-45))
                       // .forward(30)
                        .splineTo(new Vector2d(-52,-40), 90)
                        .turn(Math.toRadians(-45))
                        .waitSeconds(10)
                        .addDisplacementMarker(() -> {

                        })
                        .back(3)
                        .turn(Math.toRadians(-20))
                        .back(7)
                        .waitSeconds(5)
                        .forward(3)
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> {

                        })
                        .splineTo(new Vector2d(-25,-9),0)

                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
