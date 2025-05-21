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

@TeleOp(name = "Modules Test Using Gamepad", group = "Robot")
@Config
public class ModulesTestManual extends LinearOpMode {
    private boolean stateLeftBumper = false;
    private boolean stateRightBumper = false;

    public static double TARGET_LIFT = -25;

    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        Claw claw = new Claw(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);
        Suspension suspension = new Suspension(this);

        intake.samplesTaker.start();
        lift.liftMotorPowerDriver.start();
        shoulder.shoulderPosition(Shoulder.SH_POS_INIT);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.right_bumper && !stateRightBumper) {
                shoulder.switchPositionShoulder();
            }
            if (gamepad1.left_bumper && !stateLeftBumper) {
                claw.switchPositionLift();
            }
            stateLeftBumper = gamepad2.left_bumper;
            stateRightBumper = gamepad2.right_bumper;

            if (gamepad1.x) lift.setTarget(TARGET_LIFT);

            if (gamepad1.a) {
                intake.brushIntake();
            } else if (gamepad1.b) {
                intake.brushOuttake();
            } else {
                intake.brushStop();
            }
            if (gamepad1.dpad_up) intake.needTake();
            if (gamepad1.dpad_down) intake.needOuttake();

            if (gamepad1.dpad_right) suspension.moveUpStupid(SUS_SPEED);
            else if (gamepad1.dpad_left) suspension.moveDownStupid(SUS_SPEED);
            else suspension.moveStop();


        }
        intake.samplesTaker.interrupt();
        lift.liftMotorPowerDriver.interrupt();
    }
}
