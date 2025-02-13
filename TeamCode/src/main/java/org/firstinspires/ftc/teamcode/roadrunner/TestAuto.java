package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrainMecanum;

public class TestAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d beginPose = new Pose2d(0,0,0);
        DriveTrainMecanum driveTrainMecanum = new DriveTrainMecanum(hardwareMap,beginPose);
        TrajectoryActionBuilder traj = driveTrainMecanum.actionBuilder(beginPose)
                .turn(Math.toRadians(90)).lineToX(52);
    }
}
