package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;

@TeleOp(name="Simple TeleOp Test", group="Robot")
public class TeleOpSimple extends LinearOpMode {

    private boolean stateLeftBumper = false;

    @Override
    public void runOpMode() {
        DriveTrain driveTrain = new DriveTrain(this);
        waitForStart();
        while (opModeIsActive()) {
            // управление кб
            double main = -gamepad1.left_stick_y;
            double side = gamepad1.left_stick_x;
            double rotate = (gamepad1.left_trigger - gamepad1.right_trigger);
           // double den = Math.max(Math.abs(main) + Math.abs(side) + Math.abs(rotate), 1);
            driveTrain.setPower(main, side, rotate);

            if(gamepad1.left_bumper && !stateLeftBumper) {
                driveTrain.switchReverse();
            }
            boolean stateRightBumper = false;
            if (gamepad1.right_bumper && !stateRightBumper) {
                driveTrain.switchSlowMode();
            }
            stateLeftBumper = gamepad1.left_bumper;
            boolean stateX = gamepad2.x;

            telemetry.addData("main", main);
            telemetry.addData("side", side);
            telemetry.addData("rotate", rotate);
            telemetry.update();
        }
    }
}