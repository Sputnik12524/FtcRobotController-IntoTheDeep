package org.firstinspires.ftc.teamcode.opmodes.auto.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

@Autonomous (name = "Red Human", group = "Robot")
@Disabled
public class AutoRed2HumanPl extends LinearOpMode {

    static final double drive_speed = .3;
    static final double turn_speed = .3;
    static final double side_speed = .25;
    @Override
    public void runOpMode() {
        DriveTrain base = new DriveTrain(this);
        Claw claw = new Claw(this);
        Lift lift = new Lift(this);
        Shoulder shoulder = new Shoulder(this);

        waitForStart();

        base.turn(turn_speed,30);
        base.driveStraight(-drive_speed,75);
        base.turn(-turn_speed,25);
        base.driveStraight(-.25,5);
        shoulder.shoulderPosition(.555);
        sleep(500);
        claw.openSh();
        base.driveStraight(-drive_speed*0.5,10);
        claw.closeSh();
        lift.motorUp(-.7);
        base.turn(turn_speed,40);
        base.driveStraight(drive_speed,70);
        base.side(side_speed,5);
        base.turn(-turn_speed, 73);
        base.driveStraight(drive_speed, 100);
        base.driveStraight(-drive_speed,5);
    }
}
