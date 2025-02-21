package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "OpModeUpdateTest", group = "teleop")
public class TeleOpUpdateTest extends LinearOpMode {

    private final ElapsedTime timerOpMode = new ElapsedTime();
    @Override
    public void runOpMode() {

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Время цикла:", timerOpMode.milliseconds());
            timerOpMode.reset();
            telemetry.update();
        }
    }
}
