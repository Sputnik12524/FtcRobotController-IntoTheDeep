package org.firstinspires.ftc.teamcode.opmodes.test;

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
        lift.liftMotorPowerDriver.start();
        waitForStart();

        while (opModeIsActive()) {
            boolean stateB = gamepad2.b;

            double speed = gamepad2.right_stick_y;

            if (!stateB && gamepad2.b) {
                lift.unlockLift();
            }


            telemetry.addData("Encoder Position: ", lift.liftMotor.getCurrentPosition());
            dashboardTelemetry.addData("Velocity:", lift.liftMotor.getVelocity());
            dashboardTelemetry.addData("Real Velocity:", speed);
            dashboardTelemetry.update();
            telemetry.update();
        }
        lift.liftMotorPowerDriver.interrupt();
    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        Lift.LiftMotorPowerDriver liftMotorPowerDriver = lift.liftMotorPowerDriver;
        liftMotorPowerDriver.start();

        waitForStart();

        while (opModeIsActive()) {

            boolean stateB = gamepad2.b;
            boolean stateA = gamepad2.a;
            if (gamepad2.b && !stateB) {
                lift.resetZero();
            }

            if (gamepad2.a && !stateA) {
                lift.unlockLift();
            }

            liftMotorPowerDriver.run(-gamepad2.right_stick_y);


            telemetry.addData("encoder position: ", lift.getCurrentPosition());
            telemetry.addData("joystick speed: ", gamepad2.right_stick_y);
            telemetry.addData("magnit state: ", lift.isMagneting());
            telemetry.addData("is on limits: ", lift.isOnLimits());
            telemetry.update();
        }
    }
}
