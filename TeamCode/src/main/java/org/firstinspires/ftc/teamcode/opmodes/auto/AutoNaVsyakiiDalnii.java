package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;

@Autonomous(name = "2 Auto Na Vsyakii Sluchai Dalnii")
public class AutoNaVsyakiiDalnii extends LinearOpMode {
    DriveTrain base;
    @Override
    public void runOpMode() throws InterruptedException {
        base = new DriveTrain(this);
        waitForStart();
        base.driveStraight(.25,120);
    }
}
