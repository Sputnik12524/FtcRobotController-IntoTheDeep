package org.firstinspires.ftc.teamcode.modules;


import android.graphics.Color;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Config
public class Intake {
    public enum Color {
        RED, BLUE, YELLOW, NONE
    }

    private final CRServo brushServoLeft;
    private final CRServo  brushServoRight;
    private final CRServo  brushServo;

    private final Servo flipServoLeft;
    private final Servo flipServoRight;
    private final Servo extensionServoLeft; //0.06
    private final Servo extensionServoRight; //0.06

    public final SamplesTaker samplesTaker;

    NormalizedColorSensor colorSensor;
    public static double BLUE_MAX = 280;
    public static double BLUE_MIN = 210;
    public static double YELLOW_MAX = 110;
    public static double YELLOW_MIN = 60;
    public static float GAIN = 2;
    private final float[] hsvValues = new float[3]; // 0 - Оттенок Hue / 1 - Насыщенность Saturation / 2 - Яркость Value


    public static double EXTENSION_MAX = 0.6;
    public static double EXTENSION_MIN = 0.05;

    public static double EXTENSION_STEP = 0.005;
    public static double EXT_K = 8;
    public static double EXT_START_POS = 0.065;

    public static double  FLIP_INTAKE = 0.12;
    public static double  FLIP_OUTTAKE = 0.8;
    public static double FLIP_TIME = 350;
    public static final double SPEED_BRUSH = 1;

    public static double FLIP_INTAKE_NEARBY = 0.08;

    public Intake(LinearOpMode opMode) {
        this.flipServoLeft = opMode.hardwareMap.servo.get("flipServoL");
        this.flipServoRight = opMode.hardwareMap.servo.get("flipServoR");
        this.extensionServoLeft = opMode.hardwareMap.servo.get("extServoL");
        this.extensionServoRight = opMode.hardwareMap.servo.get("extServoR");
        this.brushServoLeft = opMode.hardwareMap.crservo.get("brushServoL");
        this.brushServoRight = opMode.hardwareMap.crservo.get("brushServoR");
        this.brushServo = opMode.hardwareMap.crservo.get("brushServo");
        this.colorSensor = opMode.hardwareMap.get(NormalizedColorSensor.class ,"sensor_color");
        colorSensor.setGain(GAIN);

        this.brushServoLeft.setDirection(CRServo.Direction.REVERSE);
        this.brushServoRight.setDirection(CRServo.Direction.FORWARD);
        this.brushServo.setDirection(CRServo.Direction.REVERSE);
        this.flipServoRight.setDirection(Servo.Direction.REVERSE);
        this.extensionServoRight.setDirection(Servo.Direction.REVERSE);

        this.samplesTaker = new SamplesTaker();
    }

    public void brushIntake() {
        brushServoLeft.setPower(-SPEED_BRUSH);
        brushServoRight.setPower(-SPEED_BRUSH);
        brushServo.setPower(-SPEED_BRUSH);
    }

    public void brushOuttake() {
        brushServoLeft.setPower(SPEED_BRUSH);
        brushServoRight.setPower(SPEED_BRUSH);
        brushServo.setPower(SPEED_BRUSH);
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
    public void needOuttake () { samplesTaker.needOuttake = true; }

    public class SamplesTaker extends Thread {
        volatile boolean needTake = false;
        volatile boolean needOuttake = false;

        private final ElapsedTime timer = new ElapsedTime();
        public void run () {
            while (!isInterrupted()) {
                if (needOuttake) {
                    flipPosition(FLIP_OUTTAKE);
                    brushIntake();
                    timer.reset();
                    while (timer.milliseconds() < FLIP_TIME );
                    brushStop();
                    extensionPosition(EXTENSION_MIN);
                    needOuttake = false;
                }
                if (needTake) {
                    flipPosition(FLIP_INTAKE);
                    timer.reset();
                    while (timer.milliseconds() < FLIP_TIME );
                    extensionPosition(EXTENSION_MAX);
                    needTake = false;
                }
            }
        }

    }

    public Color getColorSample() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        android.graphics.Color.colorToHSV(colors.toColor(), hsvValues);
        if (hsvValues[1] >= 0.5) {
            if (hsvValues[0] <= BLUE_MAX && hsvValues[0] >= BLUE_MIN) {
                return Color.BLUE;
            } else if (hsvValues[0] <= YELLOW_MAX && hsvValues[0] >= YELLOW_MIN) {
                return Color.YELLOW;
            } else return Color.RED;
        }
        return Color.NONE;
    }
    public double getHue() { return hsvValues[0]; }
    public double getSaturation() { return hsvValues[1]; }
    public double getValue() { return  hsvValues[2]; }
}
