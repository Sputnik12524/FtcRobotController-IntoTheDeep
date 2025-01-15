package org.firstinspires.ftc.teamcode.opmodes.auto.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.DriveTrain;

@Autonomous(name = "Auto Na Vsyakii Sluchai Blizhinii")
@Disabled
public class AutoNaVsyakiiBlizhniii extends LinearOpMode {
    DriveTrain base;

    @Override
    public void runOpMode() throws InterruptedException {
        base = new DriveTrain(this);
        waitForStart();
        base.driveStraight(.25, 70);
    }
}