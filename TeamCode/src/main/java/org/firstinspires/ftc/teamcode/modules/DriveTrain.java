package org.firstinspires.ftc.teamcode.modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
@Config
public class DriveTrain {
    public static DcMotor leftFront;
    public static DcMotor rightFront;
    public static DcMotor leftBack;
    public static DcMotor rightBack;
    public static IMU imu;

    static final double PI = Math.PI;
    static final double WHEEL_DIAMETER = 10.1; // wheel diameter given in centimeters
    static final double PULSES = 537.7; //GoBILDA Yellow Jacket 19.2:1

    double distance(double centimeter) {
        return PULSES / (PI*WHEEL_DIAMETER) * centimeter;
    }
    private double multiplier = 1;
    private LinearOpMode aggregate;

    public DriveTrain(LinearOpMode aggregate) {
        leftFront = aggregate.hardwareMap.get(DcMotor.class, "left_front");
        leftBack = aggregate.hardwareMap.get(DcMotor.class, "left_back");
        rightBack = aggregate.hardwareMap.get(DcMotor.class, "right_back");
        rightFront = aggregate.hardwareMap.get(DcMotor.class, "right_front");

        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoFacingDirection, usbFacingDirection);

        imu = aggregate.hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        imu.resetYaw();

        this.aggregate = aggregate;
    }

    public void driveStraight(double DRIVE_SPEED, double distance){
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setPower(DRIVE_SPEED);
        rightFront.setPower(DRIVE_SPEED);
        leftBack.setPower(DRIVE_SPEED);
        rightBack.setPower(DRIVE_SPEED);

        while(aggregate.opModeIsActive() && Math.abs(leftFront.getCurrentPosition()) < distance(distance));

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        aggregate.sleep(500);
    }

    public void turn(double TURN_SPEED,double degrees){
        aggregate.telemetry.addData("angle", imu.getRobotYawPitchRollAngles());
        aggregate.telemetry.update();
        leftFront.setPower(TURN_SPEED);
        rightFront.setPower(-TURN_SPEED);
        leftBack.setPower(TURN_SPEED);
        rightBack.setPower(-TURN_SPEED);
        imu.resetYaw();
        while (aggregate.opModeIsActive() && Math.abs(getHeading()) < degrees);
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        aggregate.sleep(500);
    }

    public void setPower (double main, double side, double rotation) {
        leftFront.setPower(multiplier * (main + side + rotation));
        leftBack.setPower(multiplier * (main - side + rotation));
        rightFront.setPower(multiplier * (main - side - rotation));
        rightBack.setPower(multiplier * (main + side - rotation));
    }

    public void switchSlowMode() {
        if (Math.abs(multiplier) > 0.5) {
            multiplier /= 2;
        } else {
            multiplier *= 2;
        }
    }

    public void switchReverse() {
        multiplier = -multiplier;
    }

    public void side(double SIDE_SPEED, double SideDistance){
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setPower(-SIDE_SPEED);
        rightFront.setPower(SIDE_SPEED);
        leftBack.setPower(SIDE_SPEED);
        rightBack.setPower(-SIDE_SPEED);

        while(aggregate.opModeIsActive() && Math.abs(leftFront.getCurrentPosition()) < distance(SideDistance));

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        aggregate.sleep(500);
    }
    public void stop(){
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        aggregate.sleep(100);
    }

    public double getHeading() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }
}
