package org.firstinspires.ftc.teamcode.opmodes.auto.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@Autonomous (name = "AUTO 2 SPECIMEN", group = "Robot")
@Disabled
public class AutoSpecimenWithoutRR extends LinearOpMode {

    static final double drive_speed = .3;
    static final double turn_speed = .3;
    static final double side_speed = .25;
    @Override
    public void runOpMode() {
        DriveTrain base = new DriveTrain(this);
        Claw claw = new Claw(this);
        Lift lift = new Lift(this);
        Shoulder shoulder = new Shoulder(this);
        claw.closeSh();
        lift.resetZero();
        waitForStart();
        while(opModeIsActive()) {
            base.driveStraight(-.25, 20);
            shoulder.shoulderPosition(.8);
            base.driveStraight(-.25,60);
            shoulder.shoulderPosition(.9);
            sleep(1000);
            base.driveStraight(.1,5);
            claw.openSh();
            base.driveStraight(drive_speed * 0.5, 30);
            shoulder.shoulderPosition(1);
            sleep(1000);
            base.turn(.15, 60);
            sleep(500);
            base.driveStraight(-.2, 75);
        }
    }
}
