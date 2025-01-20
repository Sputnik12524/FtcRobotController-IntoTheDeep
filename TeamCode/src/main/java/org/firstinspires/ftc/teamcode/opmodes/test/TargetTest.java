package org.firstinspires.ftc.teamcode.opmodes.test;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.modules.Lift;

@TeleOp(name = "LiftTargetTest", group = "robot")
public class TargetTest extends LinearOpMode {



    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        lift.liftMotorPowerDriver.start();
        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.x) {
                lift.setTarget(-10);
            }
            if (gamepad1.y) {
                lift.setTarget(-25);
            }

        }
        lift.liftMotorPowerDriver.interrupt();
    }
}