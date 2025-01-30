package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Claw {

    private final Servo clawServoLift;
    private final LinearOpMode opMode;

    public static double CLAW_OPEN_LIFT = 0.45;
    public static double CLAW_CLOSE_LIFT = 0.145;
    public boolean stateOpenLift;


    public Claw(LinearOpMode opMode) {
        this.opMode = opMode;
        clawServoLift = opMode.hardwareMap.servo.get("ClawServoLift");
    }

    public void switchPositionLift() {
        if (!stateOpenLift) {
            clawServoLift.setPosition(CLAW_OPEN_LIFT);
            stateOpenLift = true;
        } else {
            clawServoLift.setPosition(CLAW_CLOSE_LIFT);
            stateOpenLift = false;
        }
    }

    public void closeLift() {
        clawServoLift.setPosition(CLAW_CLOSE_LIFT);
        stateOpenLift = false;
    }
    public void openLift() {
        clawServoLift.setPosition(CLAW_OPEN_LIFT);
        stateOpenLift = true;
    }
    public void strongCloseSh() {
        clawServoLift.setPosition(0.05);
    }
}

