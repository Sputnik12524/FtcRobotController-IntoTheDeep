package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAutoBasket {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

                .setConstraints(52, 52, Math.toRadians(180), Math.toRadians(180), 13)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(32, 57, 0))
                        .turn(Math.toRadians(-180))
                        // TrajectorySequence trajectoryToBasket = driveTrain.trajectorySequenceBuilder(startPose)
                        .strafeRight(7)
                        .back(17)
                        .turn(Math.toRadians(30))
                        //.build();
                        // Trajectory trajectoryToSample1 = driveTrain.trajectoryBuilder(trajectoryToBasket.end().plus(new Pose2d(0,0, Math.toRadians(35))))
                        .forward(1)
                        //  .build();
                        // Trajectory trajectoryBack = driveTrain.trajectoryBuilder(trajectoryToSample1.end())
                        .back(1)
                        //.build();
                        // TrajectorySequence trajectoryToPark = driveTrain.trajectorySequenceBuilder(trajectoryBack.end())
                        .turn(Math.toRadians(45))
                        .forward(52)
                        .turn(Math.toRadians(105))
                        .back(13)
                        //.build();
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
