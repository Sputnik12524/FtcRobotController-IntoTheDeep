package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@TeleOp(name = "MainTeleop", group = "Robot")
@Config
public class MainTeleOp extends LinearOpMode {

    // КБ
    private boolean stateX = false;
    private boolean stateLeftBumper = false;
    private boolean stateRightBumper = false;

    // Подъемник
    public static double LIFT_POWER_COEFFICIENT = 0.5;


    // Плечо

    // Клешня
    boolean btnState;


    @Override
    public void runOpMode() {
        DriveTrain dt = new DriveTrain(this);
        Lift lt = new Lift(this);
        Shoulder sl = new Shoulder(this);
        Claw cl = new Claw(this);


        while (opModeInInit()) {   }
        waitForStart();
        while (opModeIsActive()) {

            // Управление колесной базой
            double main = gamepad1.left_stick_y + gamepad1.right_stick_y;
            double side = -gamepad1.left_stick_x - gamepad1.right_stick_x;
            double rotate = (gamepad1.left_trigger - gamepad1.right_trigger);
            dt.setPower(main, side, rotate);

            if(gamepad1.left_bumper && !stateLeftBumper) {
                dt.switchReverse();
            }
            if (gamepad1.right_bumper && !stateRightBumper) {
                dt.switchSlowMode();
            }
            stateLeftBumper = gamepad1.left_bumper;
            stateX = gamepad2.x;

            // Управление подъемником
            double speed = gamepad2.right_stick_y;
            lt.setMotorPower(speed * LIFT_POWER_COEFFICIENT);

            // Управление плечом
            if (gamepad2.dpad_up) {
                sl.shoulderPlus();
                sleep(5);
            }
            if (gamepad2.dpad_down) {
                sl.shoulderMinus();
                sleep(5);
            }

            // Управление клешней
            if (gamepad2.right_bumper && !btnState) {
                cl.switchPosition();
            }
            btnState = gamepad2.b;

            // Телеметрия
            telemetry.addLine("УПРАВЛЕНИЕ");
            telemetry.addLine("КБ:");
            telemetry.addLine("Левый/Правый стик - езда");
            telemetry.addLine("Левый/Правый триггер - повороты");
            telemetry.addLine("Левый бампер - Противоположное движение");
            telemetry.addLine("Левый/Правый триггер - Замедленное движение");
            telemetry.addLine("2-й геймпад:");
            telemetry.addLine("Правый стик - Подъемник");
            telemetry.addLine("Крестовина вверх/вниз - Плечо");
            telemetry.addLine("Правый бампер - Смена позиции клешни");
            telemetry.update();

        }
    }


}
