package org.firstinspires.ftc.teamcode.opmodes.test;

import static org.firstinspires.ftc.teamcode.modules.Suspension.SUS_SPEED;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.Suspension;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@TeleOp(name = "Modules Test Automatic", group = "Robot")
@Config
public class ModulesTestAutomatic extends LinearOpMode {

    public static double TARGET_LIFT = -25;

    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        Claw claw = new Claw(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        shoulder.shoulderPosition(0.1);
        shoulder.closeSh();
        intake.extensionPosition(Intake.EXT_POS_MIN);
        claw.openLift();

        lift.liftMotorPowerDriver.start();
        intake.samplesTaker.start();

        waitForStart();

        if (isStopRequested()) return;

        claw.closeLift();
        shoulder.openSh();
        sleep(450);
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
        sleep(1000);
        lift.setTarget(TARGET_LIFT);
        sleep(2500);
        lift.setTarget(0);
        sleep(2500);
        intake.needTake();
        sleep(1200);
        intake.needOuttake();
        sleep(1200);


        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
