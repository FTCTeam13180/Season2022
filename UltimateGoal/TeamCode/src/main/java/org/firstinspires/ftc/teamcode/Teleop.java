package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Teleop", group = "POC")
public class Teleop extends LinearOpMode {
    private OpMode op;
    private ChassisComponent chassisComponent;
    private LauncherComponent launcherComponent;
    private GrabberComponent grabberComponent;
    private LauncherStateMachine launcherStateMachine;
    private IntakeComponent intakeComponent;

    ElapsedTime runningTime;
    private StackerComponent stackerComponent;
    private StackerStateMachine stackerStateMachine;


    public void runOpMode(){
        grabberComponent = new GrabberComponent(this);
        grabberComponent.init();

        chassisComponent = new ChassisComponent(this);
        chassisComponent.init();

        launcherComponent = new LauncherComponent(this);
        launcherComponent.init();
       // launcherStateMachine = new LauncherStateMachine(launcherComponent, op);
        intakeComponent = new IntakeComponent(this);
        intakeComponent.init();
        stackerComponent = new StackerComponent(this);
        stackerComponent.init();

      //  stackerStateMachine = new StackerStateMachine(stackerComponent, op);
        waitForStart();

        //Gamepad Controls, not including Chassis movements yet since chassis
        //still needs some cleaning up
        //ToDo: Move to combined classes; there should be only two booleans


        /*
        GamePad 1: Navigator
        GamePad 2: Internal motors
        right joystick - launcher:
            up = shoot, down = reverse
        left joystick - intake:
            up = pull in, down = push out

        left bumper - whack out
        right bumper - whack in
        dpad up - stacker up
        dpad down - stacker down
         */
        while(opModeIsActive()){
            if(Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1){
                chassisComponent.mecanumDrive(gamepad1.right_stick_x, gamepad1.right_stick_y );
            }
            else if(Math.abs(gamepad1.right_stick_x) > 0.5);
                if(gamepad1.right_stick_x > 0){
                    chassisComponent.spinRight(gamepad1.right_stick_x);
                }
                if(gamepad1.right_stick_x < 0){
                    chassisComponent.spinLeft(gamepad1.right_stick_x);
                }
            else
                chassisComponent.stop();

            /* the stacker box controls; these are manual, meaning they will do
            exactly what you tell them to do. There is another button for the
            full, safe launching mechanism.
             */
            if(gamepad2.b){
                stackerComponent.stackerDump();
            }
            if (gamepad2.left_bumper) {
                stackerComponent.unsafeWhackerOut();
            }
            if (gamepad2.right_bumper){
                stackerComponent.unsafeWhackerIn();
            }
            if(gamepad2.dpad_up){
                stackerComponent.stackerUp();
            }
            if (gamepad2.dpad_down){
                stackerComponent.stackerDown();
            }


            //if less than 0 (pushed up) then FIRE!
            //if greater than 0 (pushed down) then reverse launcher to pull inward
            if(gamepad2.right_stick_y > 0){
                launcherComponent.shoot();
            }
            else if (gamepad2.right_stick_y < 0){
                launcherComponent.reverse();
            }
            else if (gamepad2.right_stick_y == 0){
                launcherComponent.stop();
            }

            //wobble arm controls
            if(gamepad2.right_bumper){
                grabberComponent.armDown();
            }
            if(gamepad2.left_bumper){
                grabberComponent.armUp();
            }

            if(gamepad2.right_trigger > 0.5){
                grabberComponent.clawOpen();
            }
            if(gamepad2.left_trigger > 0.5){
                grabberComponent.clawClose();
            }

            //if less than 0 (pushed up) then intake
            //if greater than 0 (pushed down) then push out
            if(gamepad1.right_bumper){
                intakeComponent.in();
            }
            else if(gamepad1.left_bumper){
                intakeComponent.expel();
            }
            else {
                intakeComponent.stop();
            }

            /* full shooting command
                1. starting the launcher; setting it with a runtime of 10 sec
                2. setting the number of whacks to 3
                3. Stacker moves up
                4. If the launcher has reached max speed; then start whacking
                5. Once we're finished whacking, move the stacker (now empty) down
                6. Stop the launcher
             */
            /*
            if(gamepad1.a){
                runningTime = new ElapsedTime();
                runningTime.reset();
                launcherStateMachine.setRunningTime(10000);
                launcherStateMachine.run();
                stackerStateMachine.setWhacks(3);

                if (launcherStateMachine.getState() == LauncherStateMachine.State.REACHED_MAX ){
                    stackerStateMachine.run();
                }
                launcherStateMachine.stop();
            }
            */



        }


    }
}
