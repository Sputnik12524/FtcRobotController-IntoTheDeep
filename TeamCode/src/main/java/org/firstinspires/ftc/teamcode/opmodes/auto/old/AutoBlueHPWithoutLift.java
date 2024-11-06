package org.firstinspires.ftc.teamcode.opmodes.auto.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@Autonomous (name = "Blue Basket", group = "Robot")
public class AutoBlueHPWithoutLift extends LinearOpMode {
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

        waitForStart();

        base.driveStraight(drive_speed,80);
        base.turn(-turn_speed,85);
        base.driveStraight(drive_speed,6);
        base.turn(-turn_speed,85);
        base.driveStraight(drive_speed,65);
        base.driveStraight(-drive_speed,100);
        base.turn(turn_speed,90);
        base.driveStraight(-drive_speed,5);
    }
}
//HIGH 2400