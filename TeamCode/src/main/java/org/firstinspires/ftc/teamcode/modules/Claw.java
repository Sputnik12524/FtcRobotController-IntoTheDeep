package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {

    private final Servo clawServo;
    private final LinearOpMode opMode;

    public static double CLAW_CLOSE = 0.735;
    public static double CLAW_HALF_OPEN = 0.6;
    public static double CLAW_OPEN = 0.5;

    public boolean stateOpen;


    public Claw(LinearOpMode opMode) {
        this.opMode = opMode;
        clawServo = opMode.hardwareMap.servo.get("ClawServo");

    }

    public void close() {
        clawServo.setPosition(CLAW_CLOSE);
        stateOpen = false;
    }

    public void halfOpen() {
        clawServo.setPosition(CLAW_HALF_OPEN);
    }
    public void open() {
        clawServo.setPosition(CLAW_OPEN);
        stateOpen = true;
    }
    public void switchPosition() {
        if (!stateOpen) {
            clawServo.setPosition(CLAW_OPEN);
            stateOpen = true;
        } else {
            clawServo.setPosition(CLAW_CLOSE);
            stateOpen = false;
        }
    }
}
