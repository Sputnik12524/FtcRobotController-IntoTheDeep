package org.firstinspires.ftc.teamcode.modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Shoulder {

    private final Servo servoShoulder;
    private final Servo clawServoShoulder;

    //Позиции плечо
    public static double SH_POS_MAX = 1;
    public static double SH_POS_MIN = 0;
    public static double SHOULDER_STEP = 0.005;

    public static double SH_POS_TO_BASKET = .81;// 0.5561; upper

    public static double SH_POS_TO_BASKET_AUTO = .87;
    public static double SH_POS_TO_INTAKE = 0;

    public static double SH_POS_INIT = 0.2;

    //Позиции клешни

    public static double CLAW_POS_CLOSE = 0.49;
    public static double CLAW_POS_OPEN = 0.65;
    public static double CLAW_POS_HALF_OPEN = 0.6;
    public static double CLAW_STRONG_CLOSED = .5;

    public static double CLOSING_TIME = 250;
    public boolean stateOpenShoulder;

    //Многопоточность
    public final SampleShTaker sampleShTaker;


    public Shoulder(LinearOpMode opMode) {
        this.servoShoulder = opMode.hardwareMap.servo.get("servoShoulder");
        clawServoShoulder = opMode.hardwareMap.servo.get("ClawServoShoulder");
        servoShoulder.setDirection(Servo.Direction.REVERSE);

        this.sampleShTaker = new SampleShTaker();
    }

    //Методы плеча
    public void shoulderPlus() {
        if (servoShoulder.getPosition() < SH_POS_MAX) {
            servoShoulder.setPosition(servoShoulder.getPosition() + SHOULDER_STEP);
        }
    }

    public void shoulderMinus() {
        if (servoShoulder.getPosition() > SH_POS_MIN) {
            servoShoulder.setPosition(servoShoulder.getPosition() - SHOULDER_STEP);
        }
    }

    public void shoulderPosition(double position) {
        servoShoulder.setPosition(position);
    }

    public double getPosition() {
        return servoShoulder.getPosition();
    }

    //Методы клешни

    public void halfOpenSh() {
        clawServoShoulder.setPosition(CLAW_POS_HALF_OPEN);
    }

    public void switchPositionShoulder() {
        if (!stateOpenShoulder) {
            clawServoShoulder.setPosition(CLAW_POS_OPEN);
            stateOpenShoulder = true;
        } else {
            clawServoShoulder.setPosition(CLAW_POS_CLOSE);
            stateOpenShoulder = false;
        }
    }

    public void closeSh() {
        clawServoShoulder.setPosition(CLAW_POS_CLOSE);
        stateOpenShoulder = false;
    }

    public void openSh() {
        clawServoShoulder.setPosition(CLAW_POS_OPEN);
        stateOpenShoulder = true;

    }

    public void strongCloseSh() {
        clawServoShoulder.setPosition(CLAW_STRONG_CLOSED);
    }

    public void setClawPosition(double position) {
        clawServoShoulder.setPosition(position);
    }
    //Многопоточность

    public void needToBasketSh() {
        sampleShTaker.needToBasketSh = true;
    }

    public class SampleShTaker extends Thread {
        volatile boolean needToBasketSh = false;
        private final ElapsedTime timer = new ElapsedTime();

        public void run() {
            while (!isInterrupted()) {
                if (needToBasketSh) {
                    closeSh();
                    timer.reset();
                    while (timer.milliseconds() < CLOSING_TIME) ;
                    servoShoulder.setPosition(SH_POS_TO_BASKET);
                    needToBasketSh = false;
                }
            }

        }
    }
}