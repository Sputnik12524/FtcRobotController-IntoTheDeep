package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {

    private final Servo ClawServo;
    private final LinearOpMode opMode;

    public static double CLAW_CLOSE = 0;
    public static double CLAW_HALF_OPEN = 0.5;
    public static double CLAW_OPEN = 1;

    public static boolean StateOpen;


    public Claw(LinearOpMode opMode) {
        this.opMode = opMode;
        ClawServo = opMode.hardwareMap.servo.get("ClawServo");

    }

    public void close() {
        ClawServo.setPosition(CLAW_CLOSE);
        StateOpen = false;
    }

    public void halfOpen() {
        ClawServo.setPosition(CLAW_HALF_OPEN);
    }
    public void open() {
        ClawServo.setPosition(CLAW_OPEN);
        StateOpen = true;
    }
    public void switchPosition() {
        if (StateOpen = false) {
            ClawServo.setPosition(CLAW_OPEN);
        } else {
            ClawServo.setPosition(CLAW_CLOSE);
        }
    }
}
