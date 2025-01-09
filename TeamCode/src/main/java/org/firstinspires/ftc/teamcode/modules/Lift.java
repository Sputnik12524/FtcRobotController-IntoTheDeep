package org.firstinspires.ftc.teamcode.modules;

// import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

// import com.acmerobotics.dashboard.FtcDashboard;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;


@Config
public class Lift {
    public final DcMotorEx liftMotor;

    public final DigitalChannel magneticSensor;
    private boolean isOnLimits = false;
    private boolean unlockStatement = false;
    private final LinearOpMode aggregate;
    public static double Kp = 500;
    public static double Ki = 10;
    public static double Kd = 500;
    private double error, previousError, u;
    private double sError = 0;
    private double target;
    private final ElapsedTime timer = new ElapsedTime();
    public static double HIGH_POSITION = 3200;
    private boolean isStable;
    public SetLiftMotorPower setLiftMotorPower = new SetLiftMotorPower();

    public Lift(LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.get(DcMotorEx.class, "liftMotor");
        this.magneticSensor = opMode.hardwareMap.get(DigitalChannel.class, "magneticSensor");
        this.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.aggregate = opMode;
    }

    private double liftPos(int encoderPos) {
        int stepsPerRevolution = 420;
        int D = 3;
        return D * Math.PI * encoderPos / stepsPerRevolution;
    }


    public class SetLiftMotorPower extends Thread {
        public void run(double speed)
        {
            int startIterationPos = liftMotor.getCurrentPosition();
            timer.reset();

            while (!isInterrupted()) {
                error = liftPos(startIterationPos - liftMotor.getCurrentPosition()) - target;

                sError = sError + error * timer.seconds();
                timer.reset();

                liftMotor.setPower(error * Kp + sError * Ki + (error - previousError) * Kd / timer.seconds());

                previousError = error;
                isOnLimits = (!magneticSensor.getState() && speed > 0) || (liftMotor.getCurrentPosition() >= HIGH_POSITION && speed < 0);
            }
        }
    }

//    public void setLiftMotorPower(double speed) {
//        if (!unlockStatement) {
//            double HIGH_POSITION = 3200;
//            if (!magneticSensor.getState() && speed > 0) {
//                isOnLimits = true;
//                liftMotor.setPower(0);
//                liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            } else if (Math.abs(liftMotor.getCurrentPosition()) >= HIGH_POSITION && speed < 0) {
//                liftMotor.setPower(0);
//                isOnLimits = true;
//            } else {
//                liftMotor.setPower(speed);
//                isOnLimits = false;
//            }
//        } else {
//            liftMotor.setPower(speed);
//        }
//
//        // telemetry.addData("encoder position: ", liftMotor.getCurrentPosition());
//        // dashboardTelemetry.addData("Velocity:", liftMotor.getVelocity());
//        // dashboardTelemetry.addData("Real Velocity:", speed * VELOCITY_COEF);
//        // dashboardTelemetry.update();
//

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
