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
    private LauncherStateMachine launcherStateMachine;
    private IntakeComponent intakeComponent;

    ElapsedTime runningTime;
    private StackerComponent stackerComponent;
    private StackerStateMachine stackerStateMachine;


    public void runOpMode(){
        chassisComponent = new ChassisComponent(this);
        chassisComponent.init();

        launcherComponent = new LauncherComponent(this);
        launcherComponent.init();
       // launcherStateMachine = new LauncherStateMachine(launcherComponent, op);
        intakeComponent = new IntakeComponent(this);
        intakeComponent.init();
        stackerComponent = new StackerComponent(this);

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
            if(Math.abs(gamepad1.right_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1){
                chassisComponent.mecanumDrive(gamepad1.right_stick_x, gamepad1.right_stick_y );
            }

            /* the stacker box controls; these are manual, meaning they will do
            exactly what you tell them to do. There is another button for the
            full, safe launching mechanism.
             */
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

            //if less than 0 (pushed up) then intake
            //if greater than 0 (pushed down) then push out
            if(gamepad2.left_stick_y < 0){
                intakeComponent.in();
            }
            else if(gamepad2.left_stick_y > 0){
                intakeComponent.expel();
            }
            else if (gamepad2.left_stick_y == 0){
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
