package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.dashboard.config.Config;

@Config
public class ConstantsOfTeleOp {

    public enum LiftStates {
        LIFT_ZERO, WAIT_UPDATE, ZERO_UPDATE,
        LIFT_TO_SPECIMEN_BEFORE, LIFT_TO_SPECIMEN_AFTER,
        LIFT_TO_BASKET
    }

    public enum ShoulderClawStates {
        START_POSE,
        MOVED_TO_INTAKE,
        CLAW_CLOSING_TO_BASKET, MOVING_TO_BASKET, MOVING_TO_BASKET_FROM_START, MOVED_TO_BASKET, CLAW_OPENED

    }

    public enum IntakeStates {
        FOLDED_POS, UNFOLDED_POS, UNFOLDED_POS_FOR_FLIP,
        EXTENDING_OUT, FLIPPING_OUT, BRUSHING_OUT,
        FLIPPING_IN, EXTENDING_IN,
        REMOVE_TRASH,

    }
    public enum ColorSensorStates {
        ACTIVE, PASSIVE
    }

    /// DRIVETRAIN
    public static double VELO_SCALE_COEF = 0.00225;
    public static double CORRECTION_COEF = 0;

    /// SHOULDER
    public static double CLAW_CLOSING_TIME = 400;
    public static double SH_TO_BASKET_TIME = 400;

    /// INTAKE
    public static double EXT_TIME = 400;
    public static double FLIP_TIME = 700;
    public static double BRUSH_TIME = 600;
    public static double BRUSHING_OUT_TIME = 400;

    public static double NECESSARY_EXT_POS = 0.14;


    public static double COLOR_SENSOR_TIMER = 2;


}
