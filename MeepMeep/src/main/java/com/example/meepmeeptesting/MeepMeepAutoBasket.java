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
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(12, 57, 0))
                        .turn(Math.toRadians(-180))
                        .strafeLeft(5)
                        .back(34)
                        .addDisplacementMarker(() -> {
                            /*shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
                            lift.setTarget(Lift.POS_HIGH_BASKET);
                            sleep(3000);*/
                        })
                        .waitSeconds(2)
                        .turn(Math.toRadians(30))
                        .addDisplacementMarker(() -> {
                            //shoulder.openSh();
                            //sleep(500);
                        })
                        .waitSeconds(3)
                        .forward(6)
                        .addDisplacementMarker(() -> {
                           // lift.setTarget(Lift.POS_LOWEST);
                           // sleep(1000);
                        })
                      //  .build();
       // TrajectorySequence trajectorySecondSample = driveTrain.trajectorySequenceBuilder(trajectoryFirstSample.end())
                .turn(Math.toRadians(90))
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                   /* intake.extensionPosition(0.5);
                    intake.brushIntake();
                    sleep(500);
                    intake.extensionPosition(0.05);
                    intake.flipPosition(Intake.FLIP_OUTTAKE);
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update(); */
                })
                //capturing yellow sample
                .turn(Math.toRadians(-90))
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
                .waitSeconds(5)
                .addTemporalMarker(5,() -> {
                   // shoulder.openSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    //lift.setTarget(Lift.POS_LOWEST);
                    //shoulder.shoulderPosition(0);
                })
                //.build();
        //TrajectorySequence trajectoryThirdSample = driveTrain.trajectorySequenceBuilder(trajectorySecondSample.end())
                .forward(5)
                .waitSeconds(3)
                .turn(Math.toRadians(100))
                .addDisplacementMarker(() -> {
                    /*intake.extensionPosition(0.5);
                    intake.brushIntake();
                    sleep(500);
                    intake.extensionPosition(0.05);
                    intake.flipPosition(Intake.FLIP_OUTTAKE);
                    telemetry.addLine("Здесь выдвинется выдвижение, и мы захватим желтую пробу");
                    telemetry.update();*/
                })
                .turn(Math.toRadians(-100))
                .back(5)
                .waitSeconds(3)
                .addDisplacementMarker(() -> {
                   // shoulder.shoulderPosition(Shoulder.POS_SH_FOR_INTAKE);
                    //shoulder.closeSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    //shoulder.shoulderPosition(Shoulder.POS_SH_BASKET);
                    //lift.setTarget(Lift.POS_HIGH_BASKET);
                })
                .waitSeconds(5)
                .addTemporalMarker(5,() -> {
                    //shoulder.openSh();
                })
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    //lift.setTarget(Lift.POS_LOWEST);
                    //shoulder.shoulderPosition(0);
                })
                        .turn(Math.toRadians(35))
                .forward(46)
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
