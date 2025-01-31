package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Intake;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Shoulder;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveConstants;
import org.firstinspires.ftc.teamcode.modules.driveTrainMecanum.DriveTrainMecanum;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous(name = "BLUE Auto Specimen", group = "Robot")
public class AutoSpecimenBLUE extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, this);
        Lift lift = new Lift(this);
        Claw claw = new Claw(this);
        Shoulder shoulder = new Shoulder(this);
        Intake intake = new Intake(this);
        lift.liftMotorPowerDriver.start();

        Pose2d startPose = new Pose2d(10, 57, Math.toRadians(-90));
        driveTrain.setPoseEstimate(startPose);
        shoulder.shoulderPosition(0.1);
        shoulder.strongCloseSh();
        intake.samplesTaker.start();
        intake.extensionPosition(intake.EXTENSION_MIN);

        TrajectorySequence trajectory = driveTrain.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker(() -> {
                    shoulder.shoulderPosition(.7);
                    lift.setTarget(-32);
                })
                .back(10, DriveTrainMecanum.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(2)
                .waitSeconds(1.5)
                .back(18, DriveTrainMecanum.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> {
                    shoulder.openSh();
                    telemetry.addLine("Здесь опустится подъемник");
                    telemetry.update();
                })
                .waitSeconds(2)
                .forward(4)
                .addDisplacementMarker(() -> {
                    sleep(500);
                    shoulder.shoulderPosition(.1);
                })
                .waitSeconds(1)
                //here code for scoring specimen finishes
                .turn(Math.toRadians(65))
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_SIDE);
                    sleep(100);
                })
                .forward(30)
                .turn(Math.toRadians(125))
                //выдвижение + захват
                .back(13,
                        DriveTrainMecanum.getVelocityConstraint(25,
                                DriveConstants.MAX_ANG_VEL,
                                DriveConstants.TRACK_WIDTH),
                        DriveTrainMecanum.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .waitSeconds(2)
                .addDisplacementMarker(() -> {
                    claw.closeLift();
                    sleep(1000);
                    lift.setTarget(lift.POS_HIGH_SPECIMEN_BEFORE);
                    telemetry.addLine("Здесь клешня на каретке возьмет образец");
                    telemetry.update();
                })
                .forward(3)
                .turn(Math.toRadians(85))
                .forward(25)
                .turn(Math.toRadians(-75))
                .waitSeconds(4)
                .addDisplacementMarker(() -> {
                    lift.setTarget(lift.POS_HIGH_SPECIMEN_AFTER);
                    telemetry.addLine("Здесь мы зацепим специмен (lift down)");
                    telemetry.update();
                    sleep(1000);
                    claw.openLift();
                })
                .forward(3)
                .splineTo(new Vector2d(52, 53), Math.toRadians(0))
                .turn(Math.toRadians(90))
                .waitSeconds(1)
                .build();

        waitForStart();
        if (isStopRequested()) return;
        driveTrain.followTrajectorySequence(trajectory);
        lift.liftMotorPowerDriver.interrupt();
        intake.samplesTaker.interrupt();
    }
}
