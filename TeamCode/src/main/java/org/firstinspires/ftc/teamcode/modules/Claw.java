package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {

    private final Servo clawServoLift;
    private final LinearOpMode opMode;

    public static double CLAW_L_POS_OPEN = 0.072;
    public static double CLAW_L_POS_CLOSE = 0.3;
    public boolean stateOpenLift;


    public Claw(LinearOpMode opMode) {
        this.opMode = opMode;
        clawServoLift = opMode.hardwareMap.servo.get("ClawServoLift");
    }

    public void switchPositionLift() {
        if (!stateOpenLift) {
            clawServoLift.setPosition(CLAW_L_POS_OPEN);
            stateOpenLift = true;
        } else {
            clawServoLift.setPosition(CLAW_L_POS_CLOSE);
            stateOpenLift = false;
        }
    }

    public void openLift() {
        clawServoLift.setPosition(CLAW_L_POS_CLOSE);
        stateOpenLift = false;
    }
    public void closeLift() {
        clawServoLift.setPosition(CLAW_L_POS_OPEN);
        stateOpenLift = true;
    }
}

