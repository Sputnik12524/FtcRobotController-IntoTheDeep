package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@Autonomous (name = "Red Basket", group = "Robot")
public class AutoRed1Baskets extends LinearOpMode {
    private DriveTrain base;
    private Lift lift;
    private Shoulder shoulder;
    private Claw claw;

    static final double drive_speed = .3;
    static final double turn_speed = .3;
    static final double side_speed = .25;
    @Override
    public void runOpMode()  {
        base = new DriveTrain(this);
        lift = new Lift(this);
        shoulder = new Shoulder(this);
        claw = new Claw(this);

        waitForStart();

        base.turn(-turn_speed,35);
        base.driveStraight(drive_speed,74);
        base.turn(turn_speed,20);
        sleep(2000);
        //lift.setMotorPower(.2);
        //shoulder.shoulderPlus();
        //claw.open();
        base.driveStraight(-drive_speed*0.3,10);
        //claw.close();
        //lift.setMotorPower(-.2);
        base.turn(turn_speed,40);
        base.driveStraight(drive_speed,65);
        base.side(-side_speed,5);
        base.turn(turn_speed, 65);
        base.driveStraight(drive_speed, 100);
        base.driveStraight(-drive_speed,5);
    }
}
