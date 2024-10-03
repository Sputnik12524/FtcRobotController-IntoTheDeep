package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {

    private final Servo ClawServo;
    private final LinearOpMode opMode;

    public static double CLAW_CLOSE = 1;
    public static double CLAW_HALF_OPEN = 0.5;
    public static double CLAW_OPEN = 0;


    public Claw(LinearOpMode opMode) {
        this.opMode = opMode;
        ClawServo = opMode.hardwareMap.servo.get("ClawServo");

    }

    public void close() {
        ClawServo.setPosition(CLAW_CLOSE);
    }
    public void halfOpen() {
        ClawServo.setPosition(CLAW_HALF_OPEN);
    }
    public void open() {
        ClawServo.setPosition(CLAW_OPEN);
    }
}
