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
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(16, 54, 0))
                        .turn(Math.toRadians(90))
                        .addDisplacementMarker(() -> {
                            //shoulder.shoulderPosition(.7);
                            //lift.setTarget(-33);
                        })
                        .back(13)//, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                //DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .back(12)//, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                               // DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .addDisplacementMarker(     () -> {
                         //   shoulder.openSh();
                           // shoulder.shoulderPosition(.75);
                            //telemetry.addLine("Здесь опустится подъемник");
                            //telemetry.update();
                        })
                        .waitSeconds(2)
                        .forward(25)
                        .addDisplacementMarker(() -> {
                           // sleep(500);
                            //shoulder.shoulderPosition(.1);
                        })
                        .splineTo(new Vector2d(54, 55), Math.toRadians(55))

                       // .addDisplacementMarker(() -> lift.setTarget(0))
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
