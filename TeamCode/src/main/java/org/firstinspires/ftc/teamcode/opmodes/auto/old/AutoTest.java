package org.firstinspires.ftc.teamcode.opmodes.auto.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.roadrunner.modules12524.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@Autonomous (name = "AUTO SPECIMEN SCORING", group = "Robot")
public class AutoTest extends LinearOpMode {
    private DriveTrain base;
    private Claw claw;
    private Shoulder shoulder;
    private Lift lift;

    static final double drive_speed = .3;
    static final double turn_speed = .3;
    static final double side_speed = .25;
    @Override
    public void runOpMode() {
        base = new DriveTrain(this);
        claw = new Claw(this);
        lift = new Lift(this);
        shoulder = new Shoulder(this);
        shoulder.shoulderPosition(.7);
        claw.setPosition(.76);
        lift.resetZero();
        waitForStart();
        while(opModeIsActive()) {
            //lift.motorUp(.5);
            sleep(1000);
            shoulder.shoulderPosition(.48);
            sleep(500);
            base.driveStraight(-.25, 57);
            sleep(1000);
            //lift.motorMove(-0.15);
            claw.open();
            base.driveStraight(drive_speed * 0.5, 30);
            base.turn(.15, 60);
            base.driveStraight(.2, 75);
            //lift.motorDown(-.5);
        }
    }
}
