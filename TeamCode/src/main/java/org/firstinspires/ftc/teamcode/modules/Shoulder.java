package org.firstinspires.ftc.teamcode.modules;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Shoulder {

    private final Servo servoShoulder;
    private final LinearOpMode opMode;

    public static double SHOULDER_START = 0;
    public static double SHOULDER_SPECIMEN = 0.7;
    public static double SHOULDER_LOW_BACKET = 1;


    public Shoulder(LinearOpMode opMode) {
        this.opMode = opMode;
        this.servoShoulder = opMode.hardwareMap.servo.get("servoDoor");

    }
}