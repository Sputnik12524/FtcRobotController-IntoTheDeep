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
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-9, 54, 0))
                                .turn(Math.toRadians(90))
                                .addDisplacementMarker(() -> {
                                    // shoulder.shoulderPosition(.7);
                                    // lift.setTarget(-33);
                                })
                                .back(13)//, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                //DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL)) -9, 41 y
                                .waitSeconds(2)
                                .back(12)//, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                //DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL)) -9, 29
                                .addDisplacementMarker(() -> {
                           /* shoulder.openSh();
                            shoulder.shoulderPosition(.75);
                            telemetry.addLine("Здесь опустится подъемник");
                            telemetry.update();*/
                                })
                                .waitSeconds(2)
                                .forward(10) //-9, 39
                                .addDisplacementMarker(() -> {
                                    //sleep(500);
                                    //shoulder.shoulderPosition(.1);
                                })
                                .waitSeconds(3)
                                .addDisplacementMarker(() -> {
                                    //lift.setTarget(0);
                                })
                                // .build();
                                // TrajectorySequence trajectoryCaptureSecondSpecimen = base.trajectorySequenceBuilder(trajectorySpecimen.end())
                                .waitSeconds(1)
                                .turn(Math.toRadians(-120))
                                .waitSeconds(1)
                                .back(34) //-40, 55
                                .waitSeconds(1)
                                .turn(Math.toRadians(-60))
                                .waitSeconds(1)
                                .back(5)//, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                //DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL)) -40,60
                                .waitSeconds(1)
                                .addDisplacementMarker(() -> {
                                    //claw.closeLift();
                                })
                                .waitSeconds(1)
                                .forward(5)
                                .build());

                        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
