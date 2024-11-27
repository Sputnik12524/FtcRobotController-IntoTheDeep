package org.firstinspires.ftc.teamcode.modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Intake {

    private final CRServo brushServoLeft;
    private final CRServo  brushServoRight;
    private final Servo flipServoLeft;
    private final Servo flipServoRight;
    private final Servo extensionServoLeft;
    private final Servo extensionServoRight;


    public static double EXTENSION_MAX = 1;
    public static double EXTENSION_MIN = 0;
    public static double EXTENSION_STEP = 0.005;
    public static double  FLIP_INTAKE = 0.5;
    public static double  FLIP_OUTTAKE = 0;

    public static double SPEED_BRUSH = 1;



    public Intake(LinearOpMode opMode) {
        this.flipServoLeft = opMode.hardwareMap.servo.get("flipServoL");
        this.flipServoRight = opMode.hardwareMap.servo.get("flipServoR");
        this.extensionServoLeft = opMode.hardwareMap.servo.get("extServoL");
        this.extensionServoRight = opMode.hardwareMap.servo.get("extServoR");
        this.brushServoLeft = opMode.hardwareMap.crservo.get("brushServoL");

        this.brushServoRight = opMode.hardwareMap.crservo.get("brushServoR");
        this.brushServoLeft.setDirection(CRServo.Direction.REVERSE);
        this.flipServoLeft.setDirection(Servo.Direction.REVERSE);
        this.extensionServoLeft.setDirection(Servo.Direction.REVERSE);

    }
    public void brushIntake() {
        brushServoLeft.setPower(SPEED_BRUSH);
        brushServoRight.setPower(SPEED_BRUSH);
    }
    public void brushOuttake() {
        brushServoLeft.setPower(-SPEED_BRUSH);
        brushServoRight.setPower(-SPEED_BRUSH);
    }
    public void flipPosition(double position) {
        flipServoLeft.setPosition(position);
        flipServoRight.setPosition(position);
    }
    public void extensionPlus() {
        if (extensionServoRight.getPosition() < EXTENSION_MAX) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition()+EXTENSION_STEP);
            extensionServoRight.setPosition(extensionServoRight.getPosition()+EXTENSION_STEP);
        }
    }

    public void extUpdatePosition(double k) {
        if ((extensionServoRight.getPosition() < EXTENSION_MAX && k>0)
                || (extensionServoRight.getPosition() > EXTENSION_MIN && k<0)) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition()+(EXTENSION_STEP * k));
            extensionServoRight.setPosition(extensionServoRight.getPosition()+(EXTENSION_STEP * k));
        }
    }

    public void extensionMinus() {
        if (extensionServoRight.getPosition() > EXTENSION_MIN) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition()-EXTENSION_STEP);
            extensionServoRight.setPosition(extensionServoRight.getPosition()-EXTENSION_STEP);
        }
    }

    public void brushStop() {
        brushServoLeft.setPower(0);
        brushServoRight.setPower(0);
    }

    public double getFlipPosition() {
        return flipServoRight.getPosition();
    }
    public double getExtensionPositionR() {
        return extensionServoRight.getPosition();
    }
    public double getExtensionPositionL() {
        return extensionServoLeft.getPosition();
    }



}
