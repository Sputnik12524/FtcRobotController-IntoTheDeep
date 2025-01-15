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
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-10, -56, 0))
                        .turn(Math.toRadians(90))
                        .forward(19)
                        .waitSeconds(1)
                        .turn(Math.toRadians(180))
                        .waitSeconds(1)
                        .back(14)
                        .waitSeconds(1)
                        .forward(5)
                        .waitSeconds(1)
                        .turn(Math.toRadians(-55))
                        .splineTo(new Vector2d(-53,-45), 90)
                        .turn(Math.toRadians(-45))
                        .waitSeconds(10) //capturing yellow sample
                        .back(3)
                        .waitSeconds(10)
                       // .turn(Math.toRadians(45))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
