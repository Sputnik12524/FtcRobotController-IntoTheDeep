package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(52, 52, Math.toRadians(223), Math.toRadians(223), 12)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-38, 57, Math.toRadians(-90)))
                        .waitSeconds(3)
                        .turn(Math.toRadians(-45))
                        .splineTo(new Vector2d(-40,1),Math.toRadians(-90))
                        .turn(Math.toRadians(-45))
                        .splineTo(new Vector2d(-38,57), Math.toRadians(-90)) //сделать отдельной траекторией с true

                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
