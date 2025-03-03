package org.firstinspires.ftc.teamcode.opmodes.tele;

public class ConstantsOfTeleOp {

    public enum LiftPositions {
        LIFT_ZERO, WAIT_UPDATE, ZERO_UPDATE,
        LIFT_TO_SIDE, LIFT_TO_SPECIMEN_BEFORE, LIFT_TO_SPECIMEN_AFTER,
        LIFT_TO_BASKET
    }

    public enum ShoulderClawPositions {
        START_POSE,
        MOVED_TO_INTAKE,
        CLAW_CLOSING_TO_BASKET, MOVING_TO_BASKET, MOVING_TO_BASKET_FROM_START, MOVED_TO_BASKET, CLAW_OPENED

    }

    public enum IntakePositions {
        OUTTAKE_POS, INTAKE_POS, INTAKE_POS_FOR_FLIP,
        EXTENDING_OUT, FLIPPING_OUT, BRUSHING_OUT,
        FLIPPING_IN, EXTENDING_IN,
        REMOVE_TRASH,

    }

    /// DRIVETRAIN
    public static double VELO_SCALE_COEF = 0.00225;
    public static double CORRECTION_COEF = 0;

    /// SHOULDER
    public static double TIME_CLOSING_CLAW = 0.25;
    public static double TIME_SH_TO_BASKET = 0.25;

    /// INTAKE
    public static double EXT_TIME = 1;
    public static double FLIP_TIME = 0.5;
    public static double BRUSH_TIME = 0.6;
    public static double BRUSHING_OUT_TIME = 0.4;

    public static double NECESSARY_EXT_POS = 0.2;


}
