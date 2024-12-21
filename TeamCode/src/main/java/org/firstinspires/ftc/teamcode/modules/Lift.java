package org.firstinspires.ftc.teamcode.modules;

// import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

// import com.acmerobotics.dashboard.FtcDashboard;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;


@Config
public class Lift {
    public final DcMotorEx liftMotor;
    public static double P_COEF = 15;
    public static double I_COEF = 1;
    public static double D_COEF = 10;
    public static double F_COEF = 10;
    public static double VELOCITY_COEF = 1200;

    public final DigitalChannel magneticSensor;
    private boolean isOnLimits = false;
    private boolean unlockStatement = false;

    private LinearOpMode aggregate;

    public Lift(LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.get(DcMotorEx.class, "liftMotor");
        this.magneticSensor = opMode.hardwareMap.get(DigitalChannel.class, "magneticSensor");
        this.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        PIDFCoefficients c = new PIDFCoefficients(P_COEF, I_COEF, D_COEF, F_COEF);
        liftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, c);
        this.aggregate = opMode;
    }


    public void setLiftMotorPower(double speed) {
        if (!unlockStatement) {
            double HIGH_POSITION = 3200;
            if (!magneticSensor.getState() && speed > 0) {
                isOnLimits = true;
                liftMotor.setVelocity(0);
                liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else if (Math.abs(liftMotor.getCurrentPosition()) >= HIGH_POSITION && speed < 0) {
                liftMotor.setVelocity(0);
                isOnLimits = true;
            } else {
                liftMotor.setVelocity(speed * VELOCITY_COEF);
                isOnLimits = false;
            }
        }
    }

    public double getSpeed() {
        double num = liftMotor.getPower();
        return num;
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

}
