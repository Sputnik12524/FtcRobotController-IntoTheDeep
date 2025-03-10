package org.firstinspires.ftc.teamcode.opmodes.test;

import static org.firstinspires.ftc.teamcode.modules.Suspension.SUS_SPEED;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.modules.Suspension;

@Config
@TeleOp(name = "SUSTest", group = "teleop")
public class TeleOpSuspension extends LinearOpMode {


    @Override
    public void runOpMode() {
        Suspension sp = new Suspension(this);
        waitForStart();
        while (opModeIsActive()) {

            ///Suspension
            if (gamepad1.dpad_up) {
                sp.MoveUp(SUS_SPEED);
            } else if (gamepad1.dpad_down) {
                sp.MoveDown(SUS_SPEED);
            } else sp.MoveStop();

        }
    }
}