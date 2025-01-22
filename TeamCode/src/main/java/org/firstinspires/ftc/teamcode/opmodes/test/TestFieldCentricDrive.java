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
            double straight = -gamepad1.left_stick_y;
            double side = gamepad1.left_stick_x * 1.1;
            double rotate = gamepad1.right_stick_x;

            if(gamepad1.left_bumper){
                DriveTrain.imu.resetYaw();
            }

            double botHeading = DriveTrain.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double rotX = side * Math.cos(-botHeading) - straight * Math.sin(-botHeading);
            double rotY = side * Math.sin(-botHeading) + straight * Math.cos(-botHeading);

            rotX *= 1.1;

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rotate), 1);
            double fLPow = (rotY+rotX+rotate) / denominator;
            double bLPow = (rotY-rotX+rotate) / denominator;
            double fRPow = (rotY-rotX-rotate) / denominator;
            double bRPow = (rotY+rotX-rotate) / denominator;

            DriveTrain.leftFront.setPower(fLPow);
            DriveTrain.leftBack.setPower(bLPow);
            DriveTrain.rightFront.setPower(fRPow);
            DriveTrain.rightBack.setPower(bRPow);
        }
    }

}
