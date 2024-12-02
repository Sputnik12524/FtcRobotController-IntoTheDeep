package org.firstinspires.ftc.teamcode.roadrunner.modules12524;

// import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

// import com.acmerobotics.dashboard.FtcDashboard;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;


@Config
public class Lift {
    public final DcMotorEx liftMotor;
    //private final double LOW_POSITION = 0;
    //private final double HIGH_POSITION = -50;
    public static double P_COEF = 200;
    public static double I_COEF = 1;
    public static double D_COEF = 20;
    public static double F_COEF = 100;
    public static double VELOCITY_COEF = 1;


    public Lift (LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.get(DcMotorEx.class, "liftMotor");
        this.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // PIDFCoefficients c = new PIDFCoefficients(P_COEF, I_COEF, D_COEF, F_COEF);
        // liftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, c);
    }

    public void setMotorPower (double speed) {
//        if (Math.abs(liftMotor.getCurrentPosition()) < LOW_POSITION && speed < 0) {
//            liftMotor.setVelocity(0);
//        }
//        else if (Math.abs(liftMotor.getCurrentPosition()) > HIGH_POSITION && speed > 0) {
//            liftMotor.setVelocity(0);
//        } else {
//            liftMotor.setVelocity(VELOCITY_COEF * speed);
//        }
        if (Math.abs(speed) < 0.01) {
//            liftMotor.setPower(0.01);
        } else {
            liftMotor.setPower(speed);
        }
    }

    public double getCurrentPosition() {
        double num = liftMotor.getCurrentPosition();
        return num;
    }

    public void resetZero() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

//    public void updatePIDF() {
//        PIDFCoefficients c = new PIDFCoefficients(P_COEF, I_COEF, D_COEF, F_COEF);
//        liftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, c);
//    }
}
