package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {

    private final Servo clawServoShoulder;
    private final Servo clawServoLift;
    private final LinearOpMode opMode;

    public static double CLAW_CLOSE = 0.735;
    public static double CLAW_HALF_OPEN = 0.6;
    public static double CLAW_OPEN = 0.5;

    public boolean stateOpenShoulder;
    public boolean stateOpenLift;


    public Claw(LinearOpMode opMode) {
        this.opMode = opMode;
        clawServoShoulder = opMode.hardwareMap.servo.get("ClawServoShoulder");
        clawServoLift = opMode.hardwareMap.servo.get("ClawServoLift");

    }

    public void halfOpenSh() {
        clawServoShoulder.setPosition(CLAW_HALF_OPEN);
    }
    public void halfOpenLift() {
        clawServoLift.setPosition(CLAW_HALF_OPEN);
    }

    public void switchPositionShoulder() {
        if (!stateOpenShoulder) {
            clawServoShoulder.setPosition(CLAW_OPEN);
            stateOpenShoulder = true;
        } else {
            clawServoShoulder.setPosition(CLAW_CLOSE);
            stateOpenShoulder = false;
        }
    }
    public void switchPositionLift() {
        if (!stateOpenLift) {
            clawServoLift.setPosition(CLAW_OPEN);
            stateOpenLift = true;
        } else {
            clawServoLift.setPosition(CLAW_CLOSE);
            stateOpenLift = false;
        }
    }



    public void closeSh() {
        clawServoShoulder.setPosition(CLAW_CLOSE);
        stateOpenShoulder = false;
    }
    public void openSh() {
        clawServoShoulder.setPosition(CLAW_OPEN);
        stateOpenShoulder = true;
    }
}
