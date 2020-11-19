package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teleop", group = "POC")
public class Teleop extends LinearOpMode {
    private OpMode op;
    private LauncherComponent launcherComponent;
    private IntakeComponent intakeComponent;

    //ToDo Merge whacker and stacker into a single class
    private WhackerComponent whackerComponent;
    private StackerComponent stackerComponent;


    public void runOpMode(){
        launcherComponent.init();
        intakeComponent.init();
        whackerComponent.init();
        stackerComponent.init();
        waitForStart();

        //Gamepad Controls, not including Chassis movements yet since chassis
        //still needs some cleaning up
        //ToDo: Move to combined classes; there should be only two booleans
        boolean whacker_extend;
        boolean whacker_back;
        boolean stacker_up;
        boolean stacker_down;

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
            whacker_extend = gamepad1.left_bumper;
            whacker_back = gamepad1.right_bumper;
            stacker_up = gamepad1.dpad_up;
            stacker_down = gamepad1.dpad_down;

            //if less than 0 (pushed up) then FIRE!
            //if greater than 0 (pushed down) then reverse launcher to pull inward
            if(gamepad1.right_stick_y < 0){
                launcherComponent.shoot();
            }
            else if (gamepad1.right_stick_y > 0){
                launcherComponent.reverse();
            }
            else if (gamepad1.right_stick_y == 0){
                launcherComponent.stop();
            }

            //if less than 0 (pushed up) then intake
            //if greater than 0 (pushed down) then push out
            if(gamepad1.left_stick_y < 0){
                intakeComponent.in();
            }
            else if(gamepad1.left_stick_y > 0){
                intakeComponent.expel();
            }
            else if (gamepad1.left_stick_y == 0){
                intakeComponent.stop();
            }


        }


    }
}
