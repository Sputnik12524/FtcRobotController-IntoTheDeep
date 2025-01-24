package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Lift {
    private final DcMotorEx liftMotor;
    private final DigitalChannel magneticSensor;
    private boolean isOnLimits = false;
    private boolean unlockStatement = false;
    private final LinearOpMode aggregate;
    public static double Kp = 1;
    public static double Ki = 0;
    public static double Kd = 0;
    private double previousError;
    private double u;
    private double sError;
    private double target;
    public static double HIGH_POSITION = 3200;
    private boolean isStable;
    public LiftMotorPowerDriver liftMotorPowerDriver = new LiftMotorPowerDriver();

    public Lift(LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.get(DcMotorEx.class, "liftMotor");
        this.magneticSensor = opMode.hardwareMap.get(DigitalChannel.class, "magneticSensor");
        this.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.aggregate = opMode;
    }

    private double liftPos() {
        int stepsPerRevolution = 420;
        int D = 3;
        return D * Math.PI * liftMotor.getCurrentPosition() / stepsPerRevolution;
    }

    public class LiftMotorPowerDriver extends Thread {
        private final ElapsedTime timer = new ElapsedTime();

        public void run(double speed) {
            liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            timer.reset();

            while (!isInterrupted()) {
                double error = liftPos() - target;

                sError = sError + error * timer.seconds();
                double dError = error - previousError;

                liftMotor.setPower(error * Kp + sError * Ki + dError * Kd / timer.seconds());
                timer.reset();

                previousError = error;
                isOnLimits = (!magneticSensor.getState() && speed > 0) || (liftMotor.getCurrentPosition() >= HIGH_POSITION && speed < 0);
            }
        }
    }
    public void setTarget(double newTarget) {
        target = newTarget;
    }

    public double getTarget() {
        return target;
    }

    public double getSpeed() {
        return liftMotor.getPower();
    }

    public boolean isMagneting() {
        return !magneticSensor.getState();
    }

    public boolean isOnLimits() {
        return isOnLimits;
    }

    public void resetZero() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void unlockLift() {
        unlockStatement = !unlockStatement;
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (!unlockStatement) {
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {
            liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void motorUp(double speed) {
        while (liftMotor.getCurrentPosition() < 2700) {
            liftMotor.setPower(speed);
        }
        liftMotor.setPower(0);
    }

    public void motorDown(double speed) {
        while (liftMotor.getCurrentPosition() > 0) {
            liftMotor.setPower(speed);
        }
        liftMotor.setPower(0);
    }

    public void motorMove(double speed) {
        liftMotor.setPower(speed);
        aggregate.sleep(500);
    }

    public double getCurrentPosition() {
        return liftMotor.getCurrentPosition();
    }

    public void kolxoz(double speed) {
        liftMotor.setPower(speed);
    }

}
