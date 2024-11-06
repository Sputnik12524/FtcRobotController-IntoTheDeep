package org.firstinspires.ftc.teamcode.opmodes.auto.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;

@Autonomous (name="SimpleAuto12524", group = "Robot")
public class SimpleAuto12524 extends LinearOpMode {
    private DriveTrain base;
    static final double drive_speed = .3;
    static final double turn_speed = .15;
    static final double side_speed =.15;

    @Override
    public void runOpMode(){
        base = new DriveTrain(this);

        waitForStart();
        while (opModeIsActive()) {
            base.driveStraight(.4, 25);
            base.driveStraight(-.4,10);
            base.turn(-turn_speed, 75);
            base.driveStraight(drive_speed, 93); //Проезжаем вперед 3 плитки
            base.turn(turn_speed, 75);//Поворачиваемся налево
            base.driveStraight(drive_speed, 2);//Проезжаем вперед
            base.turn(turn_speed, 57);//Поворачиваемся налево

            base.driveStraight(drive_speed, 100);//Едем вперед до зоны сетей, сбивая крайнюю пробу
            base.side(-side_speed, 5.5);//Проезжаем вбок
            base.driveStraight(-drive_speed, 100);//задом едем к пробам

            base.side(-side_speed, 10);//проезжаем боком к центральной пробе
            base.driveStraight(drive_speed, 85);//отвозим ее в зону сетей
            base.driveStraight(0,0);
            sleep(5000);
        }
    }
}