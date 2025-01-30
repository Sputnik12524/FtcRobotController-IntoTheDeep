package org.firstinspires.ftc.teamcode.modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.modules.Claw;


@Config
public class Shoulder {

    private final Servo servoShoulder;
    private final Servo clawServoShoulder;

    //Позиции плечо
    public static double SHOULDER_MAX = 1;
    public static double SHOULDER_MIN = 0;
    public static double SHOULDER_STEP = 0.005;

    public static double POS_SH_BASKET = 0.5;
    public static double POS_SH_FOR_INTAKE = 0;

    public static double INITIAL_POSITION = 0.2;

    //Позиции клешни

    public static double CLAW_CLOSE = 0.055;
    public static double CLAW_OPEN = 0.26;
    public static double CLAW_HALF_OPEN = 0.6;

    public static double TIME = 350;
    public boolean stateOpenShoulder;

    //Многопоточность
    SampleShTaker sampleShTaker;


    public Shoulder(LinearOpMode opMode) {
        this.servoShoulder = opMode.hardwareMap.servo.get("servoShoulder");
        clawServoShoulder = opMode.hardwareMap.servo.get("ClawServoShoulder");
        servoShoulder.setDirection(Servo.Direction.REVERSE);

        this.sampleShTaker = new SampleShTaker();
    }

    //Методы плеча
    public void shoulderPlus() {
        if (servoShoulder.getPosition() < SHOULDER_MAX) {
            servoShoulder.setPosition(servoShoulder.getPosition() + SHOULDER_STEP);
        }
    }

    public void shoulderMinus() {
        if (servoShoulder.getPosition() > SHOULDER_MIN) {
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
        clawServoShoulder.setPosition(CLAW_HALF_OPEN);
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

    public void closeSh() {
        clawServoShoulder.setPosition(CLAW_CLOSE);
        stateOpenShoulder = false;
    }

    public void openSh() {
        clawServoShoulder.setPosition(CLAW_OPEN);
        stateOpenShoulder = true;

    }

    public void strongCloseSh() {
        clawServoShoulder.setPosition(0.05);
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
                    while (timer.milliseconds() < TIME) ;
                    clawServoShoulder.setPosition(POS_SH_BASKET);
                    needToBasketSh = false;
                }
            }

        }
    }
}