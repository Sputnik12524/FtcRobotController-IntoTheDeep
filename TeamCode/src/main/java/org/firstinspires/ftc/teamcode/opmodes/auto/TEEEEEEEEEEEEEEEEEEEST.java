package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "0 TEEEEEEEST", group = "Robot")
public class TEEEEEEEEEEEEEEEEEEEST extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrainMecanum base = new DriveTrainMecanum(hardwareMap, this);
        Claw cl  = new Claw(this);
        Lift lift = new Lift(this);
        Intake in = new Intake(this);
        Shoulder shoulder = new Shoulder(this);
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(-10, 56, Math.toRadians(90));
        base.setPoseEstimate(startPose);
        cl.closeLift();

        /**<?Белый комментарий*/

        TrajectorySequence trajectorySpecimen = base.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(-15, 50), Math.toRadians(90))

                .build();
        shoulder.shoulderPosition(0.1);
        shoulder.closeSh();
        in.extensionPosition(Intake.EXT_POS_MIN);

        waitForStart();

        if (isStopRequested());
        base.followTrajectorySequence(trajectorySpecimen);
        sleep(500);
        lift.liftMotorPowerDriver.interrupt();
    }
}
