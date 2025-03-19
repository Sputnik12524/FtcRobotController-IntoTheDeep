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
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-10, 56, 0))
                        .turn(Math.toRadians(90))
                        .addDisplacementMarker(() -> {
                            /// shoulder.shoulderPosition(.72);
                            /// lift.setTarget(-33);
                        })
                        .back(10)/*, DriveTrainMecanum.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))*/
                        .back(15)/*, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))*/
                        .addDisplacementMarker(() -> {
                            /*shoulder.openSh();
                            shoulder.shoulderPosition(.75);
                            telemetry.addLine("Здесь опустится подъемник");
                            telemetry.update();*/
                        })
                        ///.waitSeconds(1)
                        .forward(5)
                        ///   .addDisplacementMarker(() -> shoulder.shoulderPosition(.1))
                        .waitSeconds(0.5)
                        /// .addDisplacementMarker(() -> lift.setTarget(0)) //statement lambda was replaced with expression lambda
                        ///    .build();
                        //  TrajectorySequence trajectoryCaptureSecondSpecimen = base.trajectorySequenceBuilder(trajectorySpecimen.end())
                        .splineTo(new Vector2d(-52, 59), Math.toRadians(90))
                        .forward(3)
                        .addDisplacementMarker(() -> {
                            // claw.closeLift();
                            // sleep(1000);
                            // lift.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE);
                        })
                        //.build();
                        // TrajectorySequence trajectoryScoringSecondSpecimen = base.trajectorySequenceBuilder(trajectoryCaptureSecondSpecimen.end())
                        .waitSeconds(1)
                        .back(10)
                        .turn(Math.toRadians(70))
                        .back(46)
                        .turn(Math.toRadians(-70))
                        .back(5.5)/*,DriveTrainMecanum.getVelocityConstraint(15,
                         DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                         DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))**/
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> {
                            /// lift.setTarget(Lift.POS_HIGH_SPECIMEN_AFTER);
                            /// sleep(1000);
                            /// claw.openLift();
                        })
                        .splineTo(new Vector2d(-52,55), Math.toRadians(180))
//.addDisplacementMarker(() -> lift.setTarget(0))
                        .waitSeconds(1)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}