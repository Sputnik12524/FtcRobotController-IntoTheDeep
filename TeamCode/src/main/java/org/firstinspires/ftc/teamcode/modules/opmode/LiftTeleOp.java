package org.firstinspires.ftc.teamcode.modules.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Lift;

@TeleOp(name="LiftTeleOp: Test", group="robot")

public class LiftTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        waitForStart();

        while (opModeIsActive()) {
            double speed = gamepad2.right_stick_y;

            lift.setMotorPower(speed);
        }
    }
}
