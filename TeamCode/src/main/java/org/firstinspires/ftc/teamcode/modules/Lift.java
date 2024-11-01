package org.firstinspires.ftc.teamcode.modules;

// import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

// import com.acmerobotics.dashboard.FtcDashboard;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@Config
public class Lift {
    public final DcMotorEx liftMotor;
    private final double LOW_POSITION = 0;
    private final double HIGH_POSITION = 2400;
    // public static double P_COEF = 500;
    // public static double I_COEF = 10;
    //public static double D_COEF = 500;
    //public static double F_COEF = 500;
    // public static double VELOCITY_COEF = 10;

    // FtcDashboard dashboard = FtcDashboard.getInstance();
    // Telemetry dashboardTelemetry = dashboard.getTelemetry();

    public Lift (LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.get(DcMotorEx.class, "liftMotor");
        this.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // PIDFCoefficients c = new PIDFCoefficients(P_COEF, I_COEF, D_COEF, F_COEF);
        // liftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, c);
    }

    public void setMotorPower (double speed) {
        if (liftMotor.getCurrentPosition() < LOW_POSITION && speed < 0) {
            liftMotor.setPower(0);
        } else if (Math.abs(liftMotor.getCurrentPosition()) > HIGH_POSITION && speed > 0) {
            liftMotor.setPower(0);
        } else {
            liftMotor.setPower(speed);
        }

        // telemetry.addData("encoder position: ", liftMotor.getCurrentPosition());
        // dashboardTelemetry.addData("Velocity:", liftMotor.getVelocity());
        // dashboardTelemetry.addData("Real Velocity:", speed * VELOCITY_COEF);
        // dashboardTelemetry.update();
    }
    public double getCurrentPosition() {
        double num = liftMotor.getCurrentPosition();
        return num;
    }
    public double getSpeed() {
        double num = liftMotor.getPower();
        return num;
    }

    public void resetZero() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }




}
