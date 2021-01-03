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

@TeleOp(name = "TestTeleop", group = "POC")
public class TestTeleop extends LinearOpMode {
    private OpMode op;
    private ChassisComponent chassisComponent;
    private IntakeComponent intakeComponent;
    private GrabberComponent grabberComponent;
    private LauncherComponent launcherComponent;
    private LauncherStateMachine launcherStateMachine;


    Odometry odometry;

    ElapsedTime runningTime;
    private StackerComponent stackerComponent;
    private StackerStateMachine stackerStateMachine;
    private ChassisComponent chassisComponent1;

    double power=1.0;


    public void runOpMode(){
        grabberComponent = new GrabberComponent(this);
        grabberComponent.init();

        chassisComponent = new ChassisComponent(this);
        chassisComponent.init();
        chassisComponent.initIMU();

        launcherComponent = new LauncherComponent(this);
        launcherComponent.init();
        launcherStateMachine = new LauncherStateMachine(launcherComponent, op);

        intakeComponent = new IntakeComponent(this);
        intakeComponent.init();

        odometry = new Odometry(this,chassisComponent,50,75);

        stackerComponent = new StackerComponent(this);
        stackerComponent.init();
        stackerStateMachine = new StackerStateMachine(stackerComponent, op);

        waitForStart();

        while(opModeIsActive()){
            if(gamepad2.x){
                stackerComponent.logStackerPosition();
                telemetry.update();
            }

            if (gamepad2.dpad_up)
                stackerComponent.stackerIncremetalUp();

            if (gamepad2.dpad_down)
                stackerComponent.stackerIncremetalDown();

        }


    }
}
