package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;

@TeleOp(name="TeleOp Test", group="Robot")

public class TeleOpSimple extends LinearOpMode {

    private DriveTrain driveTrain;
    private boolean stateX = false;
    private boolean stateLeftBumper = false;
    private boolean stateRightBumper = false;

    @Override
    public void runOpMode() {
        driveTrain = new DriveTrain(this);
        waitForStart();
        while (opModeIsActive()) {
            // управление кб
            double main = gamepad1.left_stick_y;
            double side = -gamepad1.left_stick_x * 1.1;
            double rotate = (gamepad1.left_trigger - gamepad1.right_trigger);
            double den = Math.max(Math.abs(main) + Math.abs(side) + Math.abs(rotate), 1);
            driveTrain.setPower(main / den, side / den, rotate / den);

            if(gamepad1.left_bumper && !stateLeftBumper) {
                driveTrain.switchReverse();
            }
            if (gamepad1.right_bumper && !stateRightBumper) {
                driveTrain.switchSlowMode();
            }
            stateLeftBumper = gamepad1.left_bumper;
            stateX = gamepad2.x;

            telemetry.addData("main", main);
            telemetry.addData("side", side);
            telemetry.addData("rotate", rotate);
            telemetry.update();
        }
    }
}