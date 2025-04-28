package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(52, 52, Math.toRadians(223), Math.toRadians(223), 12)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(10, 56, 0))
                        .turn(Math.toRadians(-90))
                        .forward(28)
                        .back(10)
                        .waitSeconds(0.5)
                        .lineToLinearHeading(new Pose2d(50,60, Math.toRadians(-90)))
                        .waitSeconds(1)
                        .lineToLinearHeading(new Pose2d(10,40, Math.toRadians(90)))

                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
