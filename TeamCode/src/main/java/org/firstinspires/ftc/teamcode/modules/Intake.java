package org.firstinspires.ftc.teamcode.modules;


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
    private final CRServo brushServoRight;
    private final CRServo brushServo;

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


    public static double EXT_POS_MAX = 0.4;
    public static double EXT_POS_MIN = 0.09;

    public static double EXTENSION_STEP = 0.005;
    public static double EXT_SPEED_COEF = 8;
    public static double EXT_POS_INIT = 0.1;

    public static double FLIP_POS_FOR_TAKE = 0.1;
    public static double FLIP_POS_FOR_OUTTAKE = 0.72;
    public static double FLIP_TIME = 350;
    public static final double SPEED_BRUSH = 1;

    public Intake(LinearOpMode opMode) {
        this.flipServoLeft = opMode.hardwareMap.servo.get("flipServoL");
        this.flipServoRight = opMode.hardwareMap.servo.get("flipServoR");
        this.extensionServoLeft = opMode.hardwareMap.servo.get("extServoL");
        this.extensionServoRight = opMode.hardwareMap.servo.get("extServoR");
        this.brushServoLeft = opMode.hardwareMap.crservo.get("brushServoL");
        this.brushServoRight = opMode.hardwareMap.crservo.get("brushServoR");
        this.brushServo = opMode.hardwareMap.crservo.get("brushServo");
        this.colorSensor = opMode.hardwareMap.get(NormalizedColorSensor.class, "sensor_color");
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
        if ((extensionServoRight.getPosition() < EXT_POS_MAX && k > 0)
                || (extensionServoRight.getPosition() > EXT_POS_MIN && k < 0)) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition() + (EXTENSION_STEP * k * EXT_SPEED_COEF));
            extensionServoRight.setPosition(extensionServoRight.getPosition() + (EXTENSION_STEP * k * EXT_SPEED_COEF));
        }
    }

    public void extensionPosition(double position) {
        extensionServoLeft.setPosition(position);
        extensionServoRight.setPosition(position);
    }

    public void extensionMinus() {
        if (extensionServoRight.getPosition() > EXT_POS_MIN) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition() - EXTENSION_STEP);
            extensionServoRight.setPosition(extensionServoRight.getPosition() - EXTENSION_STEP);
        }
    }

    public void extensionPlus() {
        if (extensionServoRight.getPosition() < EXT_POS_MAX) {
            extensionServoLeft.setPosition(extensionServoLeft.getPosition() + EXTENSION_STEP);
            extensionServoRight.setPosition(extensionServoRight.getPosition() + EXTENSION_STEP);
        }
    }

    public double getFlipPositionR() {
        return flipServoRight.getPosition();
    }

    public double getFlipPositionL() {
        return flipServoLeft.getPosition();
    }

    public double getExtensionPositionR() {
        return extensionServoRight.getPosition();
    }

    public double getExtensionPositionL() {
        return extensionServoLeft.getPosition();
    }


    public void needTake() {
        samplesTaker.needTake = true;
    }

    public void needOuttake() {
        samplesTaker.needOuttake = true;
    }

    public class SamplesTaker extends Thread {
        volatile boolean needTake = false;
        volatile boolean needOuttake = false;

        private final ElapsedTime timer = new ElapsedTime();

        public void run() {
            while (!isInterrupted()) {
                if (needOuttake) {
                    flipPosition(FLIP_POS_FOR_OUTTAKE);
                    brushIntake();
                    timer.reset();
                    while (timer.milliseconds() < FLIP_TIME) ;
                    brushStop();
                    extensionPosition(EXT_POS_MIN);
                    needOuttake = false;
                }
                if (needTake) {
                    flipPosition(FLIP_POS_FOR_TAKE);
                    timer.reset();
                    while (timer.milliseconds() < FLIP_TIME) ;
                    extensionPosition(EXT_POS_MAX);
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

    public double getHue() {
        return hsvValues[0];
    }

    public double getSaturation() {
        return hsvValues[1];
    }

    public double getValue() {
        return hsvValues[2];
    }
}
