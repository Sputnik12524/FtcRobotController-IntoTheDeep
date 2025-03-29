package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;

//TODO: add gamepad buttons to do actions
@TeleOp(name = "Modules Test", group = "Robot")
public class ModulesTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        Lift lift = new Lift(this);
        Claw claw = new Claw(this);
        Intake intake = new Intake(this);
        Shoulder shoulder = new Shoulder(this);

        waitForStart();

        if (isStopRequested()) return;

        if (gamepad1.a) {
            telemetry.addLine("Testing lift claw");
            claw.openLift();
            claw.closeLift();
            telemetry.update();
        }

        telemetry.addLine("Testing shoulder");
        shoulder.openSh();
        shoulder.shoulderPosition(Shoulder.SH_POS_TO_BASKET);
        telemetry.update();

        telemetry.addLine("Testing intake");
        intake.extensionPosition(Intake.EXT_POS_MAX);
        sleep(1000);
        intake.brushIntake();
        intake.extensionPosition(Intake.EXT_POS_MIN);
        intake.flipPosition(Intake.FLIP_POS_FOR_TAKE);
        sleep(1000);
        intake.flipPosition(Intake.FLIP_POS_FOR_OUTTAKE);
        telemetry.update();

        telemetry.addLine("Testing lift");
        lift.setTarget(-20);
        sleep(2000);
        lift.setTarget(0);
        sleep(2000);
        telemetry.update();
    }
}
