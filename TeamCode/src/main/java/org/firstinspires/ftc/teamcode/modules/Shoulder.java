package org.firstinspires.ftc.teamcode.modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Shoulder {

    private final Servo servoShoulder;

    public static double SHOULDER_MAX = 1;
    public static double SHOULDER_MIN = 0;
    public static double SHOULDER_STEP = 0.005;


    public Shoulder(LinearOpMode opMode) {
        this.servoShoulder = opMode.hardwareMap.servo.get("servoShoulder");

    }

    public void shoulderPlus () {
        if (servoShoulder.getPosition() < SHOULDER_MAX) {
            servoShoulder.setPosition(servoShoulder.getPosition()+SHOULDER_STEP);
        }
    }
    public void shoulderMinus () {
        if (servoShoulder.getPosition() > SHOULDER_MIN) {
            servoShoulder.setPosition(servoShoulder.getPosition()-SHOULDER_STEP);
        }
    }
    public double getPosition() {
        return servoShoulder.getPosition();
    }
}