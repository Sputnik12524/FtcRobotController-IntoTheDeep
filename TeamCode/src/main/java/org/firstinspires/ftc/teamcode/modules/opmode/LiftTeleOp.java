package org.firstinspires.ftc.teamcode.modules.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.modules.Lift;

@TeleOp(name="LiftTeleOp: Test", group="robot")

public class LiftTeleOp extends LinearOpMode {

    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();

    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        waitForStart();

        while (opModeIsActive()) {
            boolean stateB = gamepad2.b;

            double speed = gamepad2.right_stick_y;

            if (!stateB && gamepad2.b) {
                lift.unlockLift();
            }

            lift.setLiftMotorPower(gamepad2.right_stick_y);

            telemetry.addData("Encoder Position: ", lift.liftMotor.getCurrentPosition());
            dashboardTelemetry.addData("Velocity:", lift.liftMotor.getVelocity());
            dashboardTelemetry.addData("Real Velocity:", speed);
            dashboardTelemetry.update();
            telemetry.update();
        }
    }
}
