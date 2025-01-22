package org.firstinspires.ftc.teamcode.opmodes.tele;

import static org.firstinspires.ftc.teamcode.modules.Shoulder.INITIAL_POSITION;

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
    private final boolean stateX = false;
    private boolean stateLeftBumperDT = false;
    private boolean stateRightBumperDT = false;
    public static double HIGH_SH = .4;
    public static double MID_SH = 0;
    public static double SUB_SH = .7;
    public static double LOW_SH = .86;


    // Подъемник
    public static double LIFT_POWER_COEFFICIENT = 0.7;
    boolean bState;

    // Плечо

    // Клешни
    private boolean stateLeftBumper = false;
    private boolean stateRightBumper = false;



    @Override
    public void runOpMode() {
        DriveTrain dt = new DriveTrain(this);
        Lift lt = new Lift(this);
        Shoulder sl = new Shoulder(this);
        Claw cl = new Claw(this);
        cl.closeSh();
        cl.openLift();

        sl.shoulderPosition(INITIAL_POSITION);
        lt.resetZero();

        while (opModeInInit()) {   }
        waitForStart();
        while (opModeIsActive()) {




            // Управление колесной базой
            double main = gamepad1.left_stick_y;
            double side = gamepad1.right_stick_x;
            double rotate = (gamepad1.left_trigger - gamepad1.right_trigger);
            dt.setPower(main, side, rotate);

            if(gamepad1.left_bumper && !stateLeftBumperDT) {
                dt.switchSlowMode();
            }
            if (gamepad1.right_bumper && !stateRightBumperDT) {
                dt.switchReverse();
            }
            stateLeftBumperDT = gamepad1.left_bumper;
            stateRightBumperDT = gamepad1.right_bumper;

            // Управление подъемником
            double speed = gamepad2.right_stick_y;
            lt.kolxoz(speed * 0.5);
            // lt.setLiftMotorPower(speed * LIFT_POWER_COEFFICIENT);
            boolean stateB = gamepad2.b;
            boolean stateA = gamepad2.a;
            if (gamepad2.dpad_left && !stateB) {
                lt.resetZero();
            }
            if (gamepad2.dpad_right && !stateA) {
                lt.unlockLift();
            }


            // Управление плечо
             //по диапазону
            if (gamepad2.dpad_down) {
                sl.shoulderPlus();
                sleep(5);
            }
            if (gamepad2.dpad_up) {
                sl.shoulderMinus();
                sleep(5);
            }
             //по позициям
            if(gamepad2.y){
                sl.shoulderPosition(HIGH_SH); //highest (для корзины)
            } else if (gamepad2.b) {
                sl.shoulderPosition(MID_SH); //начальная позиция (внутри робота)
            } else if(gamepad2.a){
                cl.closeLift();
                sl.shoulderPosition(LOW_SH); //lowest (для взятия пробы)
            } else if(gamepad2.x){
                cl.closeLift();
                sl.shoulderPosition(SUB_SH); //lowest (для взятия пробы)
            }

            // Управление клешней.
            if (gamepad2.right_bumper && !stateRightBumper) {
                cl.switchPositionShoulder();
            }
            if (gamepad2.left_bumper && !stateLeftBumper) {
                cl.switchPositionLift();
            }
            stateLeftBumper = gamepad2.left_bumper;
            stateRightBumper = gamepad2.right_bumper;
            bState = gamepad2.b;

            // Телеметрия
            telemetry.addLine("УПРАВЛЕНИЕ");
            telemetry.addData("state open lift", cl.stateOpenLift);
            telemetry.addLine("1-ый геймпад:");
            telemetry.addLine("Левый/Правый стик - езда");
            telemetry.addLine("Левый/Правый триггер - повороты");
            telemetry.addLine("Правый бампер - Противоположное движение");
            telemetry.addLine("Левый бампер - Замедленное движение");
            telemetry.addLine("2-й геймпад:");
            telemetry.addLine("Правый стик - Подъемник");
            telemetry.addLine("B - Подъемник reset 0");
            telemetry.addLine("A - Подъемник Unlock");
            telemetry.addLine("Крестовина вверх - Плечо поднято");
            telemetry.addLine("Крестовина вниз  - Плечо опущено");
            telemetry.addLine("Крестовина влево - Плечо образец");
            telemetry.addLine("Правый бампер - Смена поз. клешни (плечо)");
            telemetry.addLine("Левый бампер - Смена поз. клешни (подъемник)");
            telemetry.addData("Lift Encoder Position: ", lt.getCurrentPosition());
            telemetry.addData("Lift Motor Speed: ", lt.getSpeed());
            telemetry.addData("Stick Position: ", gamepad2.right_stick_y);
            telemetry.addData("shoulder", sl.getPosition());
            telemetry.update();

        }
    }
}
