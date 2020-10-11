package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class ChassisSerialMotion {

    public static final double POWER = 1.0;
    public static final double CMS = 60 * 2.54;
    public static final double TIMOUTMS = 20000;

    private ChassisComponent chassisComponent;
    private ChassisStateMachine chassisStateMachine;

    public ChassisSerialMotion(ChassisComponent chassisComponent, OpMode opMode) {
        this.chassisComponent = chassisComponent;
        chassisStateMachine = new ChassisStateMachine(chassisComponent, POWER, CMS, TIMOUTMS, opMode);
    }

}
