package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Shoulder;

@Autonomous(name = "test", group = "Autonomous")
public class TestAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d beginPose = new Pose2d(-10, -57, Math.toRadians(90));
        MecanumDrive driveTrainMecanum = new MecanumDrive(hardwareMap, beginPose);
        Shoulder shoulder = new Shoulder(this);
        TrajectoryActionBuilder trajectoryAction = driveTrainMecanum.actionBuilder(beginPose)
                .lineToY(0.005);
        ///.splineTo(new Vector2d(48,48), Math.PI/2);
        ///.lineToX(24);
        waitForStart();
        if (isStopRequested()) return;
        Action action = trajectoryAction.build();
        Actions.runBlocking(new SequentialAction(action, shoulder.shoulderMoveAuto()));

    }
}
