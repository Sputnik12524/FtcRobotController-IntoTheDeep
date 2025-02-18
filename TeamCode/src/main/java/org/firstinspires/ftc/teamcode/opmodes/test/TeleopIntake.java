package org.firstinspires.ftc.teamcode.opmodes.test;

import static org.firstinspires.ftc.teamcode.modules.Intake.FLIP_INTAKE;
import static org.firstinspires.ftc.teamcode.modules.Intake.FLIP_OUTTAKE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Intake;

@TeleOp(name = "IntakeTest", group = "Robot")
public class TeleopIntake extends LinearOpMode {

    private double k = 0.5;


    @Override
    public void runOpMode() {
        Intake in = new Intake(this);
        in.samplesTaker.start();
        in.extensionPosition(Intake.EXT_START_POS);

        waitForStart();
        while (opModeIsActive()) {

            //щетка
            if (gamepad1.a) {
                in.brushIntake();
            }
            else if (gamepad1.b) {
                in.brushOuttake();
            } else {
                in.brushStop();
            }

            //переворот
            if (gamepad1.y) {
                in.flipPosition(FLIP_OUTTAKE);
            }
            if (gamepad1.x) {
                in.flipPosition(FLIP_INTAKE);
            }

            //выдвижение
            in.extUpdatePosition(-gamepad1.right_stick_y * k); //с помощью стика

            if (gamepad1.dpad_up) { //с помощью крестовины
                in.extensionPlus();
            }
            if (gamepad1.dpad_down) {
                in.extensionMinus();
            }

             //многопоточность
            if (gamepad1.right_bumper) {
                in.needTake();
            }

            telemetry.addData("Позиция сервомотора переворота (RIGHT): ", in.getFlipPositionR());
            telemetry.addData("Позиция сервомотора переворота (LEFT): ", in.getFlipPositionL());
            telemetry.addData("Позиция сервомотора выдвижения (RIGHT): ", in.getExtensionPositionR());
            telemetry.addData("Позиция сервомотора выдвижения (LEFT): ", in.getExtensionPositionL());
            telemetry.addLine("Управление:");
            telemetry.addLine("Щетка - принятие(A), выброс(B)");
            telemetry.addLine("Переворот - принятие(Y), отдатие(X)");
            telemetry.addLine("Выдвижение - Правый стик/крестовина: вверх/вниз");
            telemetry.addLine("Многопоточность - правый бампер");

            telemetry.update();



        }
      in.samplesTaker.interrupt();
    }
}
