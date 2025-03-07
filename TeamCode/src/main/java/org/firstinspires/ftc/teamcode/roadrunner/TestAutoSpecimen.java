package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@Autonomous(name="Test Auto Specimen", group = "Autonomous")
public class TestAutoSpecimen extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d beginPose = new Pose2d(10,57,Math.PI);
        DriveTrainMecanum driveTrainMecanum = new DriveTrainMecanum(hardwareMap,beginPose);
        Shoulder shoulder = new Shoulder(this);


        TrajectoryActionBuilder trajectoryToSubmersible = driveTrainMecanum.actionBuilder(beginPose)
                .lineToY(-40);
        TrajectoryActionBuilder trajectoryToObservation = driveTrainMecanum
                .actionBuilder(new Pose2d(10,17,Math.PI))
                .lineToY(27)
                .turn(Math.toRadians(65))
                .lineToX(-52);

        waitForStart();

        Action act1 = trajectoryToSubmersible.build();
        Action act2 = trajectoryToObservation.build();

        Actions.runBlocking(new SequentialAction
                (new ParallelAction
                        (act1, shoulder.shoulderMoveAuto()), act2
                ));

    }
}
