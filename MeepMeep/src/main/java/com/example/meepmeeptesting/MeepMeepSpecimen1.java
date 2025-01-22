package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepSpecimen1 {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

                .setConstraints(52, 52, Math.toRadians(180), Math.toRadians(180), 13)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(9, -54, Math.toRadians(180)))
                        .waitSeconds(5)
                        /*
                        .turn(Math.toRadians(180))
                        .waitSeconds(1)
                         */

                        .back(19)
                        /*.forward(5)
                        .waitSeconds(1)
                        .turn(Math.toRadians(180))
                        .waitSeconds(1)
                        .back(19)
                        .waitSeconds(1)
                        .forward(4)
                        .waitSeconds(1)
                        .turn(Math.toRadians(-120))
                        .waitSeconds(1)
                        .back(40)
                        .waitSeconds(1)
                        .waitSeconds(1)*/
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
