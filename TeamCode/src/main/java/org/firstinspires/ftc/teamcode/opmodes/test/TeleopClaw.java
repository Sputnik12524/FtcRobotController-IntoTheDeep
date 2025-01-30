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

        while (opModeInInit()) {

        }
        waitForStart();
        while (opModeIsActive()){

            if (gamepad2.a) {
                sl.halfOpenSh();
            }

            if (gamepad2.left_bumper && !btnStateSh) {
                sl.switchPositionShoulder();
            }
            if (gamepad2.right_bumper && !btnStateLift) {
                cl.switchPositionLift();
            }
            btnStateSh = gamepad2.left_bumper;
            btnStateLift = gamepad2.right_bumper;

            telemetry.addData("Значение клешни на плече: ", sl.stateOpenShoulder);
            telemetry.addData("Значение клешни на подъемнике: ", cl.stateOpenLift);
            telemetry.addLine("Управление:");
            telemetry.addLine(" А - Полуоткрытое состояние (плечо)");
            telemetry.addLine(" Х - Полуоткрытое состояние (подъемник)");
            telemetry.addLine("Левый бампер - Смена позиции (плечо)");
            telemetry.addLine("Правый бампер - Смена позиции (подъемник)");
            telemetry.update();
        }







    }
}