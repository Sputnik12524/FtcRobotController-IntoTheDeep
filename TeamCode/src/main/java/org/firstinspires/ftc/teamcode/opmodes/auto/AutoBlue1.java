package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@Autonomous (name = "Blue 1 (near the baskets)", group = "Robot")
public class AutoBlue1 extends LinearOpMode {
    private DriveTrain base;
    private Lift lift;
    private Shoulder shoulder;
    private Claw claw;

    static final double drive_speed = .3;
    static final double turn_speed = .2;
    static final double side_speed = .15;

    @Override
    public void runOpMode()  {
        base = new DriveTrain(this);
        lift = new Lift(this);
        shoulder = new Shoulder(this);
        claw = new Claw(this);

        waitForStart();

        base.turn(turn_speed,45);
        base.driveStraight(drive_speed,90);
        base.turn(-turn_speed,45);
        shoulder.shoulderPlus();
        claw.open();
        base.driveStraight(-drive_speed,10);
        claw.close();
        base.turn(-turn_speed,90);
        base.driveStraight(drive_speed,100);
        base.side(side_speed,10);
        base.turn(-turn_speed, 70);
        base.driveStraight(drive_speed, 100);
        base.driveStraight(-drive_speed,5);
    }
}
