package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Lift {
    private final DcMotor liftMotor;
    private ElapsedTime timer;

    private LinearOpMode aggregate;

    public Lift (LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.dcMotor.get("liftMotor");
        this.timer = new ElapsedTime();
        timer.reset();
        this.aggregate = opMode;
    }

    public void setMotorPower (double speed) {
        liftMotor.setPower(speed * 0.5);
    }
    public void motorUp (double speed) {
        liftMotor.setPower(speed);
        aggregate.sleep(500);
    }
}
