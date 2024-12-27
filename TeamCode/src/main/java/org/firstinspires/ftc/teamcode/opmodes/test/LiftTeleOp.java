package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Lift;

@TeleOp(name="LiftTeleOp: Test", group="robot")

public class LiftTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        Lift.SetLiftMotorPower setLiftMotorPower = lift.setLiftMotorPower;
        setLiftMotorPower.start();

        waitForStart();

        while (opModeIsActive()) {

            boolean stateB = gamepad2.b;

            //double speed = gamepad2.right_stick_y;
            if (gamepad2.b) {
                lift.resetZero();
            }

            if (gamepad2.a) {
                lift.unlockLift();
            }

            setLiftMotorPower.run(-gamepad2.right_stick_y);


            telemetry.addData("encoder position: ", lift.getCurrentPosition());
            telemetry.addData("joystick speed: ", gamepad2.right_stick_y);
            telemetry.addData("magnit state: ", lift.isMagneting());
            telemetry.addData("is on limits: ", lift.isOnLimits());
            telemetry.update();
        }
    }
}
