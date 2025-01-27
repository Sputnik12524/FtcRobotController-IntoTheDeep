package org.firstinspires.ftc.teamcode.modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Intake {

    private final CRServo brushServoLeft;
    private final CRServo  brushServoRight;
    private final CRServo  brushServo;

    private final Servo flipServoLeft;
    private final Servo flipServoRight;
    private final Servo extensionServoLeft; //0.06
    private final Servo extensionServoRight; //0.06

    public final SamplesTaker samplesTaker;

    public static double EXTENSION_MAX = 0.6;
    public static double EXTENSION_MIN = 0.05;

    public static double EXTENSION_STEP = 0.005;
    public static double EXT_K = 0.6;
    public static double EXT_START_POS = 0.065;

    public static double  FLIP_INTAKE = 0.67;
    public static double  FLIP_OUTTAKE = 0;
    public static double FLIP_TIME = 250;
    public static double SPEED_BRUSH = 1;

    public Intake(LinearOpMode opMode) {
        this.flipServoLeft = opMode.hardwareMap.servo.get("flipServoL");
        this.flipServoRight = opMode.hardwareMap.servo.get("flipServoR");
        this.extensionServoLeft = opMode.hardwareMap.servo.get("extServoL");
        this.extensionServoRight = opMode.hardwareMap.servo.get("extServoR");
        this.brushServoLeft = opMode.hardwareMap.crservo.get("brushServoL");
        this.brushServoRight = opMode.hardwareMap.crservo.get("brushServoR");
        this.brushServo = opMode.hardwareMap.crservo.get("brushServo");

        this.brushServoLeft.setDirection(CRServo.Direction.REVERSE);
        this.brushServoRight.setDirection(CRServo.Direction.FORWARD);
        this.brushServo.setDirection(CRServo.Direction.REVERSE);
        this.flipServoLeft.setDirection(Servo.Direction.REVERSE);
        this.extensionServoRight.setDirection(Servo.Direction.REVERSE);

        this.samplesTaker = new SamplesTaker();
    }

    public void brushIntake() {
        brushServoLeft.setPower(SPEED_BRUSH);
        brushServoRight.setPower(SPEED_BRUSH);
        brushServo.setPower(SPEED_BRUSH);
    }

    public void brushOuttake() {
        brushServoLeft.setPower(-SPEED_BRUSH);
        brushServoRight.setPower(-SPEED_BRUSH);
        brushServo.setPower(-SPEED_BRUSH);
    }

    public void brushStop() {
        brushServoLeft.setPower(0);
        brushServoRight.setPower(0);
        brushServo.setPower(0);
    }

    public void flipPosition(double position) {
        flipServoLeft.setPosition(position);
        flipServoRight.setPosition(position);
    }

    public void extUpdatePosition(double k) {
        if ((extensionServoRight.getPosition() < EXTENSION_MAX && k>0)
                || (extensionServoRight.getPosition() > EXTENSION_MIN && k<0)) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition()+(EXTENSION_STEP * k * EXT_K));
            extensionServoRight.setPosition(extensionServoRight.getPosition()+(EXTENSION_STEP * k * EXT_K));
        }
    }

    public void extensionPosition(double position) {
        extensionServoLeft.setPosition(position);
        extensionServoRight.setPosition(position);
    }

    public void extensionMinus() {
        if (extensionServoRight.getPosition() > EXTENSION_MIN) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition()-EXTENSION_STEP);
            extensionServoRight.setPosition(extensionServoRight.getPosition()-EXTENSION_STEP);
        }
    }

    public void extensionPlus() {
        if (extensionServoRight.getPosition() < EXTENSION_MAX) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition()+EXTENSION_STEP);
            extensionServoRight.setPosition(extensionServoRight.getPosition()+EXTENSION_STEP);
        }
    }

    public double getFlipPositionR() { return flipServoRight.getPosition(); }
    public double getFlipPositionL() { return flipServoLeft.getPosition(); }
    public double getExtensionPositionR() {
        return extensionServoRight.getPosition();
    }
    public double getExtensionPositionL() {
        return extensionServoLeft.getPosition();
    }


    public void needTake () {
        samplesTaker.needTake = true;
    }

    public class SamplesTaker extends Thread {
        boolean needTake = false;

        private final ElapsedTime timer = new ElapsedTime();
        public void run () {
            while (!isInterrupted()) {
                if (needTake) {
                    flipPosition(FLIP_INTAKE);
                    brushIntake();
                    timer.reset();
                    while (timer.milliseconds() < FLIP_TIME );
                    brushStop();
                    extensionPosition(EXTENSION_MIN);
                    needTake = false;
                }
            }
        }

    }
}
