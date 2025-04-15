package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@TeleOp(name = "Claw", group = "Test")
public class TeleopClaw extends LinearOpMode {

    boolean btnStateSh;
    boolean btnStateLift;

    @Override
    public void runOpMode() {
        Claw cl = new Claw(this);
        Shoulder sl = new Shoulder(this);

        waitForStart();
        while (opModeIsActive()){

            if (gamepad1.y) {
                sl.openSh();
            }
            if (gamepad1.x) {
                sl.closeSh();
            }
            if (gamepad2.y) {
                cl.closeLift();
            }
            if (gamepad2.x) {
                cl.openLift();
            }

            if (gamepad1.right_bumper && !btnStateSh) {
                sl.switchPositionShoulder();
            }
            if (gamepad2.right_bumper && !btnStateLift) {
                cl.switchPositionLift();
            }
            btnStateSh = gamepad1.left_bumper;
            btnStateLift = gamepad2.right_bumper;

            telemetry.addData("Значение клешни на плече: ", sl.stateOpenShoulder);
            telemetry.addData("Значение клешни на подъемнике: ", cl.stateOpenLift);
            telemetry.addLine("Управление: (1 - Sh)(2 - Lt)");
            telemetry.addLine(" Y - открытое состояни (плечо)");
            telemetry.addLine(" Х - закрытое состояние");
            telemetry.addLine("Правый бампер - Смена позиции");
            telemetry.update();
        }







    }
}