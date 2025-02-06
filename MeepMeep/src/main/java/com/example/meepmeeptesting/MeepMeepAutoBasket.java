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
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-12, -57, 0))
                        // .turn(Math.toRadians(-90))
                        .strafeLeft(5)
                        .back(34)
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(2)
                        .turn(Math.toRadians(30))
                        .addDisplacementMarker(() -> {

                        })
                        .waitSeconds(3)
                        .forward(6)
                        .addDisplacementMarker(() -> {

                        })
                        // здесь код со скорингом предзагруженной пробы
                        .turn(Math.toRadians(100))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {

                        })
                        //capturing yellow sample
                        .turn(Math.toRadians(-100))
                        .waitSeconds(2)
                        .back(5)
                        //scoring to basket
                        .addDisplacementMarker(() -> {

                        })
                        .forward(5)
                        .waitSeconds(3)
                        .turn(Math.toRadians(100))
                        .addDisplacementMarker(() -> {

                        })
                        .turn(Math.toRadians(-100))
                        .back(5)
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {

                        })
                        .splineTo(new Vector2d(-25,-9),Math.toRadians(0))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
