package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Lift {
    public final DcMotorEx liftMotor;
    public final DigitalChannel magneticSensor;

    private boolean isOnLimits = false;
    private boolean unlockStatement = false;
    private final LinearOpMode aggregate;
    public static double Kp = 0.05;
    public static double Ki = 0;
    public static double Kd = 0;
    private double error, previousError, u;
    private double sError, dError = 0;
    private double limits;
    private double target = 0; //target = -79 --> MAX POSITION!!!!!!!

    public static double POS_LOWEST = 0;
    public static double POS_HIGHEST = -79; //Самая высокая позиция, выше нельзя!
    public static double POS_LOW_BASKET = -30;
    public static double POS_HIGH_BASKET = -70;
    public static double POS_SIDE = -4; // Берем с борта
    public static double POS_LOW_SPECIMEN_BEFORE = -15; // Целимся для установки
    public static double POS_LOW_SPECIMEN_AFTER = -2; // Устанавливаем образец
    public static double POS_HIGH_SPECIMEN_BEFORE = -47; // Целимся для установки
    public static double POS_HIGH_SPECIMEN_AFTER = -35; // Устанавливаем образец

    private boolean isStable;
    public LiftMotorPowerDriver liftMotorPowerDriver = new LiftMotorPowerDriver();

    public Lift(LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.get(DcMotorEx.class, "liftMotor");
        this.magneticSensor = opMode.hardwareMap.get(DigitalChannel.class, "magneticSensor");
        this.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.aggregate = opMode;
    }

    private double liftPos() {
        int stepsPerRevolution = 420;
        int D = 3;
        return (D * Math.PI * liftMotor.getCurrentPosition() / stepsPerRevolution) * (79.0 / 75.0);
    }

    public class LiftMotorPowerDriver extends Thread {
        private final ElapsedTime timer = new ElapsedTime();

        @Override
        public void run() {
            liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            timer.reset();

            while (!isInterrupted()) {
                error = target - liftPos();

                sError = sError + error * timer.seconds();
                dError = error - previousError;
                limits = error * Kp + sError * Ki + dError * Kd / timer.seconds();

                limits(limits);
                timer.reset();


                previousError = error;
                FtcDashboard.getInstance().getTelemetry().addData("error:", error);
                FtcDashboard.getInstance().getTelemetry().addData("previousError:", previousError);
                FtcDashboard.getInstance().getTelemetry().addData("sError:", sError);
                FtcDashboard.getInstance().getTelemetry().addData("dError:", dError);
                FtcDashboard.getInstance().getTelemetry().addData("Power:", getPower());
                FtcDashboard.getInstance().getTelemetry().addData("Target", target);
                FtcDashboard.getInstance().getTelemetry().addData("limits", limits);
                FtcDashboard.getInstance().getTelemetry().addData("Pos", liftPos());
                FtcDashboard.getInstance().getTelemetry().update();
            }
        }
    }

    public void setTarget(double newTarget) {
        target = newTarget;
    }

    public double getTarget() {
        return target;
    }

    public double getPower() {
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

    public void limits(double speed) {
        if (!unlockStatement) {
            if (!magneticSensor.getState() && speed > 0) {
                isOnLimits = true;
                liftMotor.setPower(0);
                liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                aggregate.telemetry.addLine("Нижний лимит");
                aggregate.telemetry.update();
            } else if (liftPos() <= POS_HIGHEST && speed < 0) {
                liftMotor.setPower(0);
                isOnLimits = true;
                aggregate.telemetry.addLine("Верхний лимит");
                aggregate.telemetry.update();
            } else {
                liftMotor.setPower(speed);
                isOnLimits = false;
                aggregate.telemetry.addLine("Работаю");
                aggregate.telemetry.update();
            }
        } else {
            liftMotor.setPower(speed);
            aggregate.telemetry.addLine("Работаю без лимита");
            aggregate.telemetry.update();
        }
    }
}
