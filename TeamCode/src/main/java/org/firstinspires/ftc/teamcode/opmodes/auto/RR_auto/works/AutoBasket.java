package org.firstinspires.ftc.teamcode.opmodes.auto.RR_auto.works;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.base_packages.drive.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.driveTrainMecanum.DriveTrainMecanum;
@Autonomous(name="AutoBasket", group="Robot")
public class AutoBasket extends LinearOpMode {

    private DriveTrainMecanum driveTrain;
    //lift

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Pose2d startPose = new Pose2d(-10,-56,0);

        TrajectorySequence traj = driveTrain.trajectorySequenceBuilder(startPose)
                .turn(Math.toRadians(90))
                .forward(19)
                .waitSeconds(1)
                .turn(Math.toRadians(180))
                .waitSeconds(1)
                .back(14)
                .waitSeconds(1)
                .forward(5)
                .waitSeconds(1)
                .turn(Math.toRadians(-55))
                .splineTo(new Vector2d(-53,-45), 90)
                .turn(Math.toRadians(-45))
                .waitSeconds(10)

                //capturing yellow sample

                .back(3)

                //scoring to basket

                .waitSeconds(10)
                .build();
        waitForStart();

        while(opModeIsActive() & !isStopRequested()) {
            driveTrain.followTrajectorySequence(traj);
        }
    }
}
