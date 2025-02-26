package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;

@TeleOp(name = "Field Centric Drive", group="Robot")
@Config
public class TestFieldCentricDrive extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveTrain drive = new DriveTrain(this);
        waitForStart();
        while (opModeIsActive()){
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rotate = gamepad1.left_trigger - gamepad1.right_trigger;

            if(gamepad1.left_bumper){
                drive.imu.resetYaw();
            }

            double heading = drive.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double powerX = (x * Math.cos(heading) + (-y * Math.sin(heading)));
            double powerY = (x * Math.sin(heading) + y * Math.cos(heading));

            drive.setPower(powerY, powerX, rotate);
        }
    }
}
