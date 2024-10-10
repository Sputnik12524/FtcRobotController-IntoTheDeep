package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous (name="SimpleAuto12524", group = "Robot")
public class SimpleAuto12524 extends LinearOpMode {
    private DriveTrain12524 base;
    static final double drive_speed = .3;
    static final double turn_speed = .15;
    static final double side_speed =.15;

    @Override
    public void runOpMode(){
        base = new DriveTrain12524(this);

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
            base.side(-side_speed, 6);//Проезжаем вбок
            base.driveStraight(-drive_speed, 100);//задом едем к пробам

            base.side(-side_speed, 10);//проезжаем боком к центральной пробе
            base.driveStraight(drive_speed, 90);//отвозим ее в зону сетей

            base.stop();//останавливаемся
            sleep(100);
        }
    }
}

//base.side(-side_speed, 5);//Проезжаем вбок
//base.driveStraight(-drive_speed, 90);//едем к пробам
// base.side(-side_speed,15);//проезжаем вбок
// base.driveStraight(.4,100);//едем парковаться