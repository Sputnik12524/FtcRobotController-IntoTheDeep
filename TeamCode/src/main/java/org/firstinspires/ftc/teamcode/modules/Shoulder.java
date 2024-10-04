package org.firstinspires.ftc.teamcode.modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Shoulder {

    private final Servo servoShoulder;

    public double shoulderPosition = 0.5;
    public static double SHOULDER_MAX = 1;
    public static double SHOULDER_MIN = 0;
    public static double SHOULDER_STEP = 0.005;


    public Shoulder(LinearOpMode opMode) {
        this.servoShoulder = opMode.hardwareMap.servo.get("servoShoulder");

    }

    public void shoulderPlus () {
        if (shoulderPosition > SHOULDER_MAX) {
            shoulderPosition = SHOULDER_MAX;
        }
        shoulderPosition += SHOULDER_STEP;
        servoShoulder.setPosition(shoulderPosition);
    }
    public void shoulderMinus () {
        if (shoulderPosition < SHOULDER_MIN) {
            shoulderPosition = SHOULDER_MIN;
        }
        shoulderPosition -= SHOULDER_STEP;
        servoShoulder.setPosition(shoulderPosition);
    }
}