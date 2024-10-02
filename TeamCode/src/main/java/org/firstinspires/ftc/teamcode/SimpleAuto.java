package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous (name="SimpleAuto1", group = "Robot")
public class SimpleAuto extends LinearOpMode {
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private IMU imu = null;
    private ElapsedTime timer = null;
    static final double SPEED = 0.2;
    static final double TURN_SPEED = 0.25;


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
            sleep(2000);
            //straight
            leftFront.setPower(SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(SPEED);
            sleep(1000);
            leftFront.setPower(-TURN_SPEED);
            leftBack.setPower(-TURN_SPEED);
            rightFront.setPower(TURN_SPEED);
            rightBack.setPower(TURN_SPEED);
            while (opModeIsActive() && Math.abs(getHeading()) <= 90);

            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(3000);


            imu.resetYaw();
            leftFront.setPower(TURN_SPEED);
            leftBack.setPower(TURN_SPEED);
            rightFront.setPower(-TURN_SPEED);
            rightBack.setPower(-TURN_SPEED);
            while (opModeIsActive() && Math.abs(getHeading()) <= 90);
            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(200);
            imu.resetYaw();
            leftFront.setPower(-TURN_SPEED);
            leftBack.setPower(-TURN_SPEED);
            rightFront.setPower(TURN_SPEED);
            rightBack.setPower(TURN_SPEED);
            while (opModeIsActive() && Math.abs(getHeading()) <= 90);
            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(2000);
            leftFront.setPower(SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(SPEED);
            sleep(200);
            leftFront.setPower(SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(SPEED);
            sleep(2000);
            leftFront.setPower(SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(SPEED);
            sleep(200);
            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(2000);
            leftFront.setPower(SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(SPEED);
            sleep(200);
            leftFront.setPower(SPEED);
            leftBack.setPower(SPEED);
            rightFront.setPower(SPEED);
            rightBack.setPower(SPEED);
            sleep(2000);
            leftFront.setPower(SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(SPEED);
            sleep(200);
            leftFront.setPower(-SPEED);
            leftBack.setPower(-SPEED);
            rightFront.setPower(-SPEED);
            rightBack.setPower(-SPEED);
            sleep(2000);
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            sleep(2000);
               //base.driveStraight(.25, 180); //Проезжаем вперед 3 плитки
           // base.turn(.2, -90);//Поворачиваемся налево
           // base.driveStraight(.25, 40);//Проезжаем вперед
            //base.turn(.2, -90);//Поворачиваемся налево
            //base.driveStraight(.25, 130);//Едем вперед до зоны сетей, сбивая крайнюю пробу
           // base.side(-.2, 5);//Проезжаем вбок
            //base.driveStraight(-.25, 130);//задом едем к пробам
           // base.side(-.2, 15);//проезжаем боком к центральной пробе
            //base.driveStraight(.25, 130);//отвозим ее в зону сетей
          //  base.side(-.2, 5);//Проезжаем вбок
            //base.driveStraight(-.25, 130);//едем к пробам
            //base.side(-.2,20);//проезжаем вбок
           // base.driveStraight(.25,140);//едем парковаться
            //base.driveStraight(0,0);//останавливаемся
            sleep(100);
        }
    }
    public double getHeading(){
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }
}
