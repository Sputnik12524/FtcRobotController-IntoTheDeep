package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class Suspension {

    public DcMotorEx susMotorRight;
    public DcMotorEx susMotorLeft;
    LinearOpMode opMode;

    public static double SUS_SPEED = 1;
    public static double SUS_POS_MAX = 100;
    public static double SUS_POS_SECOND_LVL = 50;

    public Suspension (LinearOpMode opMode) {
        this.susMotorRight = opMode.hardwareMap.get(DcMotorEx.class, "SuspensionR");
        this.susMotorLeft = opMode.hardwareMap.get(DcMotorEx.class, "SuspensionL");
        this.susMotorLeft.setDirection(DcMotorEx.Direction.REVERSE);
        this.susMotorLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.susMotorRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.susMotorLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        this.susMotorRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        this.opMode = opMode;
    }

    public void moveUpStupid(double speed) {
        susMotorRight.setPower(speed);
        susMotorLeft.setPower(speed);
    }
    public void moveDownStupid(double speed) {
        susMotorRight.setPower(-speed);
        susMotorLeft.setPower(-speed);
    }
    public void moveStop() {
        susMotorRight.setPower(0);
        susMotorLeft.setPower(0);
    }

    public void moveUp(double speed) {
        while (susMotorRight.getCurrentPosition() < SUS_POS_MAX) {
            susMotorRight.setPower(speed);
            susMotorLeft.setPower(speed);
        }
        susMotorRight.setPower(0);
        susMotorLeft.setPower(0);
    }
    public void moveDown(double speed) {
        while (susMotorLeft.getCurrentPosition() > 0) {
            susMotorRight.setPower(-speed);
            susMotorLeft.setPower(-speed);
        }
        susMotorRight.setPower(0);
        susMotorLeft.setPower(0);
    }



}
