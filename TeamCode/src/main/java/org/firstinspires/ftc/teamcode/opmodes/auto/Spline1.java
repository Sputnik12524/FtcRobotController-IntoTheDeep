package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "1. Simple", group = "Robot")
@Config
public class Spline1 extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);

        Pose2d startPose = new Pose2d(32, 57, Math.toRadians(90));
        driveTrain.setPoseEstimate(startPose);


        TrajectorySequence trajectoryToBasket = driveTrain.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(30,56),0).build();

        waitForStart();

        if (isStopRequested()) return;

        driveTrain.followTrajectorySequence(trajectoryToBasket);
    }
}
