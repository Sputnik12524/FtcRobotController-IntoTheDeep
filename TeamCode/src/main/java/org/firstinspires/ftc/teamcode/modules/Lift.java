package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
public class Lift {
    private final DcMotor liftMotor;


    public Lift (LinearOpMode opMode) {
        this.liftMotor = opMode.hardwareMap.dcMotor.get("liftMotor");
    }

    public void setMotorPower (double speed) {
        liftMotor.setPower(speed);
    }
}
