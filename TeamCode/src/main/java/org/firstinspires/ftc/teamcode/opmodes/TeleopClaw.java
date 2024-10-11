package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Claw;

@TeleOp(name = "Claw", group = "Test")
public class TeleopClaw extends LinearOpMode {

    @Override
    public void runOpMode() {
        Claw cl = new Claw(this);

        while (opModeInInit()) {

        }
        waitForStart();
        while (opModeIsActive()){
            if (gamepad2.x) {
                cl.close();
            }
            if (gamepad2.a) {
                cl.halfOpen();
            }
            if (gamepad2.y) {
                cl.open();
            }
            if (gamepad2.b) {
                cl.switchPosition();
            }
        }




    }
}