package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrainMecanum;

@Autonomous(name = "test", group = "Robot")
public class TestAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d beginPose = new Pose2d(0, 0, 0);
        MecanumDrive driveTrainMecanum = new MecanumDrive(hardwareMap, beginPose);
        waitForStart();
        if (isStopRequested()) return;
        Actions.runBlocking(
                driveTrainMecanum.actionBuilder(new Pose2d(0, 0, 0))
                        .turn(Math.PI / 2)
                        .lineToY(15)
                        .build());

    }
}
