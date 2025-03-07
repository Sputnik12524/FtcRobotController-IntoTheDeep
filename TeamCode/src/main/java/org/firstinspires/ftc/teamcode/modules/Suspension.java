package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class Suspension {

    public DcMotor susMotorRight, susMotorLeft;
    LinearOpMode opMode;

    public static double SUS_SPEED = 1;

    public Suspension (LinearOpMode opMode) {
        this.susMotorRight = opMode.hardwareMap.get(DcMotorEx.class, "SuspensionR");
        this.susMotorLeft = opMode.hardwareMap.get(DcMotorEx.class, "SuspensionL");
        this.opMode = opMode;
    }

    public void MoveUp (double speed) {
        susMotorRight.setPower(speed);
        susMotorLeft.setPower(speed);
    }
    public void MoveDown (double speed) {
        susMotorRight.setPower(-speed);
        susMotorLeft.setPower(-speed);
    }



}
