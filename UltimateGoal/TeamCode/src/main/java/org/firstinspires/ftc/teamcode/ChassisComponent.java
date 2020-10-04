package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yash Pradhan on 10/4/20
 */

public class ChassisComponent {

    private DcMotor topl;
    private DcMotor topr;
    private DcMotor rearr;
    private DcMotor rearl;

    public void init() {
        topl = opMode.hardwareMap.get(DcMotor.class, "Topl");
        topr = opMode.hardwareMap.get(DcMotor.class, "Topr");
        rearl = opMode.hardwareMap.get(DcMotor.class, "Rearl");
        rearr = opMode.hardwareMap.get(DcMotor.class, "Rearr");
        topr.setDirection(DcMotor.Direction.REVERSE);
        rearr.setDirection(DcMotor.Direction.REVERSE);
        topl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        navigatorPower = 1.0;
        if (logging) {
            opMode.telemetry.addData("RoboNavigator:", "Initialized");
        }
    }

    private void move(double toprPower, double toplPower, double rearrPower, double rearlPower) {
        topr.setPower(toplPower);
        topl.setPower(toprPower);
        rearr.setPower(rearrPower);
        rearl.setPower(rearlPower);
    }

}