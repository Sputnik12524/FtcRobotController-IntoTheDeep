package org.firstinspires.ftc.teamcode.modules.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Shoulder;

@TeleOp(name = "ShoulderTest", group = "teleop")
public class TeleopShoulder extends LinearOpMode {

    private double shoulderPosition = 0;


    @Override
    public void runOpMode() {
        Shoulder sl = new Shoulder(this);


        while (opModeInInit()) {

        }
        waitForStart();
        while (opModeIsActive()){

            if (shoulderPosition > 1) {
                shoulderPosition = 1;
            }
            if (shoulderPosition < 0) {
                shoulderPosition = 0;
            }

            if (gamepad1.x) {
                shoulderPosition += 0.005;
                sleep(5);
            }
            if (gamepad1.y) {
                shoulderPosition -= 0.005;
                sleep(5);
            }







        }
    }

}