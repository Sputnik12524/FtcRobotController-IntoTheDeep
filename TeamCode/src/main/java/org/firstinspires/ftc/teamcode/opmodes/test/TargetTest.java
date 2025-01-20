package org.firstinspires.ftc.teamcode.opmodes.test;


import static org.firstinspires.ftc.teamcode.modules.Lift.POS_LOW_BASKET;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.modules.Lift;

@TeleOp(name = "LiftTargetTest", group = "robot")
@Config
public class TargetTest extends LinearOpMode {

    public static double POSITION_1 = -10;
    public static double POSITION_2 = -25; //-79 --> MAX POSITION!!!!!!!
    // -30 low basket
    // -4 с борта берем
    // -15 low specimen целимся / -2 устанавливаем
    // -47 high specimen перед подвешиванием / -35 установливаем
    // -70 high basket
    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        lift.liftMotorPowerDriver.start();
        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.x) {
                lift.setTarget(POSITION_1);
            }
            if (gamepad1.y) {
                lift.setTarget(POSITION_2);
            }

        }
        lift.liftMotorPowerDriver.interrupt();
    }
}