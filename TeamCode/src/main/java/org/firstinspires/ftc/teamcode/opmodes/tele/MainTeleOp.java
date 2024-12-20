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

    private boolean stateRightBumper = false;



    // Подъемник
    public static double LIFT_POWER_COEFFICIENT = 0.7;
    boolean bState;

    // Плечо

    // Клешни
    private boolean stateLeftBumper = false;



    @Override
    public void runOpMode() {
        DriveTrain dt = new DriveTrain(this);
        Lift lt = new Lift(this);
        Shoulder sl = new Shoulder(this);
        Claw cl = new Claw(this);
        cl.closeSh();

        sl.shoulderPosition(1);
        lt.resetZero();

        while (opModeInInit()) {   }
        waitForStart();
        while (opModeIsActive()) {




            // Управление колесной базой
            double main = -gamepad1.left_stick_y;
            double side = -gamepad1.right_stick_x;
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
           /* double speed = -gamepad2.right_stick_y;
            lt.setLiftMotorPower(speed * LIFT_POWER_COEFFICIENT);

            if (gamepad2.left_bumper && !stateLeftBumper) {
                lt.unlockLift();
            } --------
            if (gamepad2.b && !btnState) {
               lt.resetZero();
            } */


            // Управление плечом
            if (gamepad2.dpad_up) {
                sl.shoulderPlus();
                sleep(5);
            }
            if (gamepad2.dpad_down) {
                sl.shoulderMinus();
                sleep(5);
            }

            if(gamepad2.y){
                sl.shoulderPosition(.59); //highest
               /* if (sl.getPosition() == 0 || sl.getPosition() == 1) {
                    sl.shoulderPosition(.555);
                } else {
                    sl.shoulderPosition(1);
                }*/
            } else if (gamepad2.x) {
                sl.shoulderPosition(.4); //level 1 and specimen
            } else if(gamepad2.a){
                sl.shoulderPosition(.1289); //lowest
            }

            // Управление клешней
            if (gamepad2.right_bumper && !stateRightBumper) {
                cl.switchPositionShoulder();
            }
            if (gamepad2.right_bumper && !stateLeftBumper) {
                cl.switchPositionLift();
            }
            stateLeftBumper = gamepad2.left_bumper;
            stateRightBumper = gamepad2.right_bumper;
            bState = gamepad2.b;

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
            telemetry.addLine("X - Плечо вверх");
            telemetry.addLine("Y - Плечо вниз");
            telemetry.addLine("Правый бампер - Смена позиции клешни");
            telemetry.addData("Lift Encoder Position: ", lt.getCurrentPosition());
            telemetry.addData("Lift Motor Speed: ", lt.getSpeed());
            telemetry.addData("Stick Position: ", gamepad2.right_stick_y);
            telemetry.addData("shoulder", sl.getPosition());
            telemetry.update();

        }
    }


}
