package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Lift {
    private final DcMotorEx liftMotor;
    private final double LOW_POSITION = 0;
    private final double COIL_DIAMETER = 21;
    private final double PI = Math.PI;
    private final double PULSES = 700;
    public static double HIGH_POSITION = 0;
    public static double P_COEF = 0;
    public static double I_COEF = 0;
    public static double D_COEF = 0;
    public static double F_COEF = 0;
    public static double VELOCITY_COEF = 10;

    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();

    public Lift (LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.get(DcMotorEx.class, "liftMotor");
        this.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PIDFCoefficients c = new PIDFCoefficients(P_COEF, I_COEF, D_COEF, F_COEF);
        liftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, c);
    }

    public void setMotorPower (double speed) {
        if (liftMotor.getCurrentPosition() <= LOW_POSITION && speed < 0) {
            liftMotor.setVelocity(speed * VELOCITY_COEF);
        }
        if (liftMotor.getCurrentPosition() <= HIGH_POSITION && speed < 0) {
            liftMotor.setVelocity(speed * VELOCITY_COEF);
        }
        dashboardTelemetry.addData("Velocity:", liftMotor.getVelocity());
        dashboardTelemetry.addData("Real Velocity:", speed * VELOCITY_COEF);
        dashboardTelemetry.update();
    }

    public void resetZero() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void getPIDFCoefficients() {
        telemetry.addData("default COEF:", liftMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
    }
}
