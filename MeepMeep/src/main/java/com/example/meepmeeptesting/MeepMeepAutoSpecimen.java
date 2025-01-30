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
                        .back(10)
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(1)
                        .back(11)
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(3)
                        .forward(4)
                        .waitSeconds(1)
                        .splineTo(new Vector2d(40, -40), Math.toRadians(90))
                        //выдвижение + захват
                        .back(14)
                        .waitSeconds(5)
                        .addDisplacementMarker(() -> {

                        })
                        .forward(3)
                        .splineTo(new Vector2d(10,-40), Math.toRadians(-90))
                        .back(7)
                        .waitSeconds(10)
                        .addDisplacementMarker(() -> {

                        })
                        .forward(5)
                        .splineTo(new Vector2d(52, -53), Math.toRadians(0))
                        .turn(Math.toRadians(90))
                        .waitSeconds(1)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
