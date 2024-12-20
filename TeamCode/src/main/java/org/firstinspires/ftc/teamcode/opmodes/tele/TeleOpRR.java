package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.DriveTrain;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.roadrunner.modules12524.DriveTrainMecanum;
/*
Need test
 */
@TeleOp(name="TeleOp Road Runner")
@Config
public class TeleOpRR extends LinearOpMode {

    //КБ
    private double straight;
    private double side;
    private double rotate;
    public static double VELO_SCALE_COEF = 0.00225;
    public static double CORRECTION_COEF = 7;
    private double w_target;
    private double w_real;

    // Подъемник
    public static double LIFT_POWER_COEFFICIENT = 0.7;
    boolean bState;

    // Плечо


    // Клешни
    private boolean stateLeftBumper = false;
    private boolean stateRightBumper = false;

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lt = new Lift(this);
        DriveTrain dt = new DriveTrain(this);
        Shoulder sl = new Shoulder(this);
        Claw cl = new Claw(this);
        cl.closeSh();

        sl.shoulderPosition(1);
        lt.resetZero();

        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        PoseStorage.currentPose = driveTrain.getPoseEstimate();
        driveTrain.setPoseEstimate(PoseStorage.currentPose);

        //straight = gamepad1.left_stick_y * DriveTrainMecanum.multiplier;
        //side = gamepad1.right_stick_x * DriveTrainMecanum.multiplier;
        //rotate = (gamepad1.left_trigger - gamepad1.right_trigger) * DriveTrainMecanum.multiplier;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {

            //КБ
            w_target = gamepad1.left_trigger - gamepad1.right_trigger;
            w_real = driveTrain.getExternalHeadingVelocity();
            if (Math.abs(w_real) < 1) {
                w_real = 0;
            }
            rotate = CORRECTION_COEF * (w_target - w_real * VELO_SCALE_COEF) + w_target;

            driveTrain.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y * DriveTrainMecanum.multiplier,
                            gamepad1.right_stick_x * DriveTrainMecanum.multiplier,
                            rotate * DriveTrainMecanum.multiplier
                    )
            );

            if(gamepad1.left_bumper){
                driveTrain.switchSlowMode();
            }

            driveTrain.update();

            if(gamepad1.x) {
                driveTrain.resetIMU();
            }

//            if (gamepad2.y && !stateY) {
//                lift.updatePIDF();
//            }
            // Read pose
            Pose2d poseEstimate = driveTrain.getPoseEstimate();

            // Управление подъемником
            double speed = gamepad2.right_stick_y;
            lt.setLiftMotorPower(speed);
            boolean stateDown = gamepad2.dpad_down;
            boolean stateUp = gamepad2.dpad_up;
            if (gamepad2.dpad_down && !stateDown) {
                lt.resetZero();
            }
            if (gamepad2.dpad_up && !stateUp) {
                lt.unlockLift();
            }


            // Управление плечо
            if(gamepad2.y){
                sl.shoulderPosition(.59); //highest
            } else if (gamepad2.x) {
                sl.shoulderPosition(.4); //level 1 and specimen
            } else if(gamepad2.a){
                sl.shoulderPosition(.1289); //lowest
            }

            // Управление клешней
            if (gamepad2.right_bumper && !stateRightBumper) {
                cl.switchPositionShoulder();
            }
            if (gamepad2.left_bumper && !stateLeftBumper) {
                cl.switchPositionLift();
            }
            stateLeftBumper = gamepad2.left_bumper;
            stateRightBumper = gamepad2.right_bumper;
            bState = gamepad2.b;



            // Print pose to telemetry
            //telemetry.addData("W x:", driveTrain.imu.getRobotAngularVelocity(AngleUnit.DEGREES).xRotationRate);
            //telemetry.addData("W y:", driveTrain.imu.getRobotAngularVelocity(AngleUnit.DEGREES).yRotationRate);
            //telemetry.addData("W z", driveTrain.imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate);
            telemetry.addData("W real", w_real);
            telemetry.addData("W target", w_target);
            telemetry.addData("Rotate", rotate);
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }

    static class PoseStorage {
        public static Pose2d currentPose = new Pose2d();
    }
}

