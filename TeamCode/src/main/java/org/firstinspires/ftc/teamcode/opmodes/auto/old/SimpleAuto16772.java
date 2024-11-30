package org.firstinspires.ftc.teamcode.opmodes.auto.old;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous (name="SimpleAuto16772", group = "Robot")
@Disabled
public class SimpleAuto16772 extends LinearOpMode {
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private IMU imu = null;
    private ElapsedTime timer = null;
    static final double SPEED = 0.3;
    static final double TURN_SPEED = 0.2;


    @Override
    public void runOpMode(){
        timer = new ElapsedTime();
        timer.reset();
        leftFront = hardwareMap.get(DcMotor.class,"left_front");
        leftBack = hardwareMap.get(DcMotor.class,"left_back");
        rightFront = hardwareMap.get(DcMotor.class,"right_front");
        rightBack = hardwareMap.get(DcMotor.class,"right_back");

        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu = hardwareMap.get(IMU.class,"imu");
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        imu.resetYaw();
        waitForStart();
        while (opModeIsActive()) {
            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(500); //довозим пробу

            leftFront.setPower(SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(SPEED);
            sleep(500); //отъезжаем назад
            leftFront.setPower(-TURN_SPEED);
            leftBack.setPower(-TURN_SPEED);
            rightFront.setPower(TURN_SPEED);
            rightBack.setPower(TURN_SPEED);
            while (opModeIsActive() && Math.abs(getHeading()) <= 90); //поворот

            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(3000); //едем вперед

            imu.resetYaw();
            leftFront.setPower(TURN_SPEED);
            leftBack.setPower(TURN_SPEED);
            rightFront.setPower(-TURN_SPEED);
            rightBack.setPower(-TURN_SPEED);
            while (opModeIsActive() && Math.abs(getHeading()) <= 85); //поворот

            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(400);
            imu.resetYaw(); //сбиваем пробу

            leftFront.setPower(TURN_SPEED);
            leftBack.setPower(TURN_SPEED);
            rightFront.setPower(-TURN_SPEED);
            rightBack.setPower(-TURN_SPEED);
            while (opModeIsActive() && Math.abs(getHeading()) <= 82); //поворачиваемся к зоне сетей

            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(2600); //едем в зону сетей
            leftFront.setPower(-SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(-SPEED);
            sleep(350); //проезжаем вбок
            leftFront.setPower(SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(SPEED);
            sleep(2400); //едем к пробам

            leftFront.setPower(-SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(-SPEED);
            sleep(350); //проезжаем вбок к пробе
            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(2400); //едем вперед в зону сетей

            leftFront.setPower(-SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(-SPEED);
            sleep(350); //вбок

            leftFront.setPower(SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(SPEED);
            sleep(2400); //едем вперед к пробам

            leftFront.setPower(-SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(-SPEED);
            sleep(400); //вбок

            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(2400); //едем обратно

            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            sleep(5000);
        }
    }
    public double getHeading(){
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }
}
