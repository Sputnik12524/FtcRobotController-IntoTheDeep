package org.firstinspires.ftc.teamcode.modules.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Shoulder;

@TeleOp(name = "ShoulderTest", group = "teleop")
public class TeleopShoulder extends LinearOpMode {


    @Override
    public void runOpMode() {
        Shoulder sl = new Shoulder(this);


        while (opModeInInit()) {

        }
        waitForStart();
        while (opModeIsActive()){

            if (gamepad1.x) {
                sl.shoulderPlus();
                sleep(5);
            }
            if (gamepad1.y) {
                sl.shoulderMinus();
                sleep(5);
            }


        }
    }

}