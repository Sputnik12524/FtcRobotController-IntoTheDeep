package org.firstinspires.ftc.teamcode.opmodes.tele;

import static org.firstinspires.ftc.teamcode.modules.Intake.FLIP_INTAKE;
import static org.firstinspires.ftc.teamcode.modules.Intake.FLIP_OUTTAKE;
import static org.firstinspires.ftc.teamcode.modules.Shoulder.INITIAL_POSITION;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@TeleOp(name = "MainTeleop", group = "Robot")
@Config
public class MainTeleOp extends LinearOpMode {

    // КБ
    private final boolean stateX = false;
    private boolean stateLeftBumperDT = false;
    private boolean stateRightBumperDT = false;


    // Подъемник
    public static double LIFT_POWER_COEFFICIENT = 0.7;
    boolean bState;

    private boolean statusDpadLeftBefore = false;
    private int posLift = 0;

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
        Intake in = new Intake(this);

        sl.closeSh();
        cl.openLift();

        lt.liftMotorPowerDriver.start();
        in.samplesTaker.start();

        sl.shoulderPosition(INITIAL_POSITION);
        in.extensionPosition(Intake.EXT_START_POS);
        lt.resetZero();

        while (opModeInInit()) {
        }
        waitForStart();
        while (opModeIsActive()) {
            // Управление колесной базой
            double main = gamepad1.left_stick_y;
            double side = gamepad1.right_stick_x;
            double rotate = (gamepad1.left_trigger - gamepad1.right_trigger);
            dt.setPower(main, side, rotate);

            if (gamepad1.left_bumper && !stateLeftBumperDT) {
                dt.switchSlowMode();
            }
            if (gamepad1.right_bumper && !stateRightBumperDT) {
                dt.switchReverse();
            }
            stateLeftBumperDT = gamepad1.left_bumper;
            stateRightBumperDT = gamepad1.right_bumper;

            // Управление подъемником
            if (gamepad2.dpad_left && !statusDpadLeftBefore) {
                posLift = (posLift + 1) % 2;
            }
            statusDpadLeftBefore = gamepad2.dpad_down;
            switch (posLift) {
                case (0):
                    if (gamepad2.y) {
                        lt.setTarget(Lift.POS_LOW_BASKET);
                    }
                    if (gamepad2.x) {
                        lt.setTarget(Lift.POS_HIGH_BASKET);
                    }
                    if (gamepad2.a) {
                        lt.setTarget(Lift.POS_FOR_INTAKE);
                    }
                    break;
                case (1):
                    if (gamepad2.y) {
                        lt.setTarget(Lift.POS_LOW_SPECIMEN_BEFORE);
                    }
                    if (gamepad2.x) {
                        lt.setTarget(Lift.POS_LOW_SPECIMEN_AFTER);
                    }
                    if (gamepad2.a) {
                        lt.setTarget(Lift.POS_HIGH_SPECIMEN_BEFORE);
                    }
                    if (gamepad2.b) {
                        lt.setTarget(Lift.POS_HIGH_SPECIMEN_AFTER);
                    }
                    if (gamepad2.dpad_right) {
                        lt.setTarget(Lift.POS_SIDE);
                    }
                    break;
            }


            // Управление плечо
            // по позициям
            if (gamepad2.dpad_right) {
                sl.shoulderPosition(Shoulder.POS_SH_FOR_INTAKE); //начальная позиция (внутри робота)
            } else if (gamepad2.dpad_up) {
                cl.closeLift();
                sl.shoulderPosition(Shoulder.POS_SH_BASKET); //lowest (для взятия пробы)
            }

            // Управление клешней.
            if (gamepad2.right_bumper && !stateRightBumper) {
                sl.switchPositionShoulder();
            }
            if (gamepad2.left_bumper && !stateLeftBumper) {
                cl.switchPositionLift();
            }
            stateLeftBumper = gamepad2.left_bumper;
            stateRightBumper = gamepad2.right_bumper;
            bState = gamepad2.b;


            //  Управление захватом
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
                in.flipPosition(FLIP_INTAKE);
            }
            if (gamepad1.x) {
                in.flipPosition(FLIP_OUTTAKE);
            }
            //выдвижение
            in.extUpdatePosition(-gamepad1.right_stick_y); //с помощью стика
            //многопоточность
            if (gamepad1.right_stick_button) {
                in.needTake();
            }

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
            telemetry.addData("Lift Motor Speed: ", lt.getPower());
            telemetry.addData("Stick Position: ", gamepad2.right_stick_y);
            telemetry.addData("shoulder", sl.getPosition());
            telemetry.update();

        }
    }
}
