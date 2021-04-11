package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.GrabberComponent;
import org.firstinspires.ftc.teamcode.component.IntakeComponent;
import org.firstinspires.ftc.teamcode.component.LauncherComponent;
import org.firstinspires.ftc.teamcode.component.StackerComponent;

@TeleOp(name = "TestOdometryModules", group = "POC")
public class TestOdometryModules extends LinearOpMode {
    private OpMode op;
    private ChassisComponent chassisComponent;
    private Odometry odometry;

    public void runOpMode(){
        chassisComponent = new ChassisComponent(this);
        chassisComponent.init();
        chassisComponent.initIMU();

        odometry = new Odometry(this,chassisComponent,50,75);
        odometry.init();

        waitForStart();

        while(opModeIsActive()){
            odometry.readCurrentPosition();
            odometry.displayPosition();
        }


    }
}
