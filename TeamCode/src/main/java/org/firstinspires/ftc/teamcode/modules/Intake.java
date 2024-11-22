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


    public static double  EXTENSION_INTAKE = 0.5;
    public static double  EXTENSION_OUTTAKE = 0;
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
    public void extensionPosition(double position) {
        extensionServoLeft.setPosition(position);
        extensionServoRight.setPosition(position);
    }
    public void brushStop() {
        brushServoLeft.setPower(0);
        brushServoRight.setPower(0);
    }


}
