package org.firstinspires.ftc.teamcode.modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.modules.Claw;


@Config
public class Shoulder {

    private final Servo servoShoulder;

    //Позиции плечо
    public static double SHOULDER_MAX = 1;
    public static double SHOULDER_MIN = 0;
    public static double SHOULDER_STEP = 0.005;

    public static double POS_SH_BASKET = 0.5;
    public static double POS_SH_FOR_INTAKE = 0;

    public static double INITIAL_POSITION = 0.2;

    public Shoulder(LinearOpMode opMode) {
        this.servoShoulder = opMode.hardwareMap.servo.get("servoShoulder");
        servoShoulder.setDirection(Servo.Direction.REVERSE);
    }

    public void shoulderPlus () {
        if (servoShoulder.getPosition() < SHOULDER_MAX) {
            servoShoulder.setPosition(servoShoulder.getPosition()+SHOULDER_STEP);
        }
    }
    public void shoulderMinus () {
        if (servoShoulder.getPosition() > SHOULDER_MIN) {
            servoShoulder.setPosition(servoShoulder.getPosition()-SHOULDER_STEP);
        }
    }
    public void shoulderPosition(double position){
        servoShoulder.setPosition(position);
    }

    public double getPosition() {
        return servoShoulder.getPosition();
    }

    public class SamplesTaker extends Thread {
        volatile boolean needSampleSh = false;
        private final ElapsedTime timer = new ElapsedTime();
        public void run () {
            while (!isInterrupted()) {
                if(needSampleSh) {

                    needSampleSh = false;
            }
        }

    }
}
}