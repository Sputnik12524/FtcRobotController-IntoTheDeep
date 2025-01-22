package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
/*
This opmode uses simple Drive Train module (not Drive Train Mecanum with RoadRunner)
It's just a test program, and if it works, I will try to add RR methods
 */
@TeleOp(name = "Field Centric Drive", group="Robot")
@Config
//@Disabled
public class TestFieldCentricDrive extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrain drive = new DriveTrain(this);
        waitForStart();
        while (opModeIsActive()){
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            if(gamepad1.left_bumper){
                DriveTrain.imu.resetYaw();
            }

            double heading = DriveTrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double powerX = (x * Math.cos(heading) + (-y * Math.sin(heading)));
            double powerY = (x * Math.sin(heading) + y * Math.cos(heading));

            drive.setPower(powerY,powerX,rotate);
        }
    }
}
