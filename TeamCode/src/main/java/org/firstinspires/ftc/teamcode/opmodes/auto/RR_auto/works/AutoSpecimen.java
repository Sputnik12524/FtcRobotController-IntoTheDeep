package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto.works;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.base_packages.drive.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.driveTrainMecanum.DriveTrainMecanum;

@Autonomous(name = "Auto Specimen", group = "Robot")
public class AutoSpecimen extends LinearOpMode {
    private DriveTrainMecanum driveTrain;
    //lift

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrainMecanum(hardwareMap,this);
        Pose2d startPose = new Pose2d(10,-56,0);
        driveTrain.setPoseEstimate(startPose);

        TrajectorySequence traj = driveTrain.trajectorySequenceBuilder(startPose)
                .turn(Math.toRadians(90))
                .forward(19)
                .waitSeconds(1)
                .turn(Math.toRadians(180))
                .waitSeconds(1)
                .back(14)
                .waitSeconds(1)
                .forward(4)
                .waitSeconds(1)
                .splineTo(new Vector2d(52,-53),-90)
                .turn(Math.toRadians(30))
                .waitSeconds(10)

                //here will be capturing of the specimen

                .back(3)
                .turn(Math.toRadians(60))
                .back(46)
                .turn(Math.toRadians(-60))
                .waitSeconds(10)

                //here will be scoring of the specimen

                .forward(3)
                .splineTo(new Vector2d(52,-53),0)
                .turn(Math.toRadians(90))
                .waitSeconds(1)
                .build();

        waitForStart();

        while (opModeIsActive() & !isStopRequested()){
            driveTrain.followTrajectorySequence(traj); //was configured via the MeepMeep
        }
    }
}
