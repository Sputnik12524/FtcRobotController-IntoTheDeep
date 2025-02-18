package org.firstinspires.ftc.teamcode.opmodes.test;


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
        while (opModeIsActive()) {

            if (gamepad1.dpad_up) {
                sl.shoulderPlus();
                sleep(5);
            }
            if (gamepad1.dpad_down) {
                sl.shoulderMinus();
                sleep(5);
            }

            telemetry.addData("Позиция сервомотора: ", sl.getPosition());
            telemetry.addLine("Управление плечом:");
            telemetry.addLine("X - вверх");
            telemetry.addLine("Y - вниз");
            telemetry.update();

        }


    }

}