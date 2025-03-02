package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAutoSpecimenBasket {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

                .setConstraints(52, 52, Math.toRadians(180), Math.toRadians(180), 13)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(12, 57, 0))
                        .turn(Math.toRadians(90))
                        .addDisplacementMarker(() -> {
                            //shoulder.shoulderPosition(.7);
                            //lift.setTarget(-33);
                        })
                        .back(13)//, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        //  DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .waitSeconds(2)
                        .back(12)//, DriveTrainMecanum.getVelocityConstraint(7, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        //DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .addDisplacementMarker(() -> {
                            /*shoulder.openSh();
                            shoulder.shoulderPosition(.75);
                            telemetry.addLine("Здесь опустится подъемник");
                            telemetry.update();*/
                        })
                        .waitSeconds(2)
                        .forward(10)
                        .addDisplacementMarker(() -> {
                            /*sleep(500);
                            shoulder.shoulderPosition(.1);*/
                        })
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            // lift.setTarget(0);
                        })
                        .waitSeconds(4)
                       // .turn(Math.toRadians(65))
                        // TrajectorySequence trajectoryFirstSample = driveTrain.trajectorySequenceBuilder(trajectorySpecimen.end())
                        .forward(5)
                        //.turn(Math.toRadians())
                        .splineTo(new Vector2d(52, 52), Math.toRadians(90))
                        .turn(Math.toRadians(-195))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            /*intake.extensionPosition(0.5);
                            intake.brushIntake();
                            sleep(500);
                            intake.extensionPosition(0.05);
                            intake.flipPosition(Intake.FLIP_OUTTAKE);
                            telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                            telemetry.update();*/
                        })
                        //capturing yellow sample
                        .turn(Math.toRadians(-25))
                        .waitSeconds(2)
                        .back(5)
                        //scoring to basket
                        .addDisplacementMarker(() -> {
                            //shoulder.shoulderPosition(Shoulder.POS_SH_FOR_INTAKE);
                            //shoulder.closeSh();
                        })
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                            //shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
                            //lift.setTarget(Lift.POS_HIGH_BASKET);
                        })
                        .waitSeconds(3)
                        .addTemporalMarker(5, () -> {
                            // shoulder.openSh();
                        })
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                            //lift.setTarget(Lift.POS_LOWEST);
                            //shoulder.shoulderPosition(0);
                        })
                        .forward(5)
                        .waitSeconds(3)
                        //TrajectorySequence trajectorySecondSample = driveTrain.trajectorySequenceBuilder(trajectoryFirstSample.end())
                        .turn(Math.toRadians(60))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                   /* intake.extensionPosition(0.5);
                    intake.brushIntake();
                    sleep(500);
                    intake.extensionPosition(0.05);
                    intake.flipPosition(Intake.FLIP_OUTTAKE);
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update();*/
                        })
                        .turn(Math.toRadians(-60))
                        .back(5)
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            //shoulder.shoulderPosition(Shoulder.POS_SH_FOR_INTAKE);
                            //shoulder.closeSh();
                        })
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                            //shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
                            //lift.setTarget(Lift.POS_HIGH_BASKET);
                        })
                        .waitSeconds(5)
                        .addTemporalMarker(5, () -> {
                            // shoulder.openSh();
                        })
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                            //lift.setTarget(Lift.POS_LOWEST);
                            //shoulder.shoulderPosition(0);
                        })
                        .forward(46)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
