package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {

    private final Servo clawServo;
    private final Servo specimenClaw;
    private final LinearOpMode opMode;

    public static double CLAW_CLOSE = 0.740;
    public static double CLAW_HALF_OPEN = 0.6;
    public static double CLAW_OPEN = 0.5;

    public boolean stateOpen;
    public boolean stateOpenS;


    public Claw(LinearOpMode opMode) {
        this.opMode = opMode;
        clawServo = opMode.hardwareMap.servo.get("ClawServo");
        specimenClaw = opMode.hardwareMap.servo.get("SpecimenClaw");
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

    public void switchPositionS(){
        if(!stateOpenS) {
            specimenClaw.setPosition(CLAW_OPEN);
            stateOpenS = true;
        } else {
            clawServo.setPosition(CLAW_CLOSE);
            stateOpenS = false;
        }
    }

    public void setPosition(double value) {
        clawServo.setPosition(value);
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
