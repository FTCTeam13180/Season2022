package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Teleop", group = "POC")
public class Teleop extends LinearOpMode {
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


        /*
        GamePad 1: Navigator
            a - reset the imu at current robot orientation
            left stick - field centric controls
            right stick - turning (only push to left or right)
            dpad up - move completely forward
            dpad down - move completely backward
            right bumper - intake IN
            left bumper - intake OUT

        GamePad 2: Internal motors
            right joystick - launcher:
                up = shoot, down = reverse
            right bumper - toggle whacker
            left bumper - full safeWhacker out and then in
            dpad up - stacker box up
            dpad down - stacker box down
            x - dump

            dpad right - claw open
            dpad left - claw close
            left stick - arm up/down based on stick up and down

         */
        while(opModeIsActive()){
            telemetry.addData("counts frontL:",chassisComponent.topl.getCurrentPosition());
            telemetry.addData("counts frontR:",chassisComponent.topr.getCurrentPosition());
            telemetry.addData("counts rearL:",chassisComponent.rearl.getCurrentPosition());
            telemetry.addData("counts rearR:",chassisComponent.rearr.getCurrentPosition());
            telemetry.update();
            if(gamepad1.x) {
                odometry.nextPoint(150,75,0.8);
                sleep(1000);
            }
            else if(gamepad1.y){
                odometry.nextPoint(50,175,0.8);
                sleep(1000);
            }
            if(gamepad1.a) {
                chassisComponent.initIMU();
            }
            if (Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1
                || (Math.abs(gamepad1.right_stick_x) > 0.1)) {

                if(Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1){
                    chassisComponent.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y ,false);
                }
                if (Math.abs(gamepad1.right_stick_x) > 0.1) {
                    if (gamepad1.right_stick_x > 0) {
                        chassisComponent.spinLeft(gamepad1.right_stick_x);
                    }
                    if (gamepad1.right_stick_x < 0) {
                        chassisComponent.spinRight(gamepad1.right_stick_x);
                    }
                }
            }
            else if (gamepad1.dpad_up) {
                chassisComponent.moveBackward(1);
            }
            else if (gamepad1.dpad_down) {
                chassisComponent.moveForward(1);
            }
            else {
                chassisComponent.stop();
            }

            if(gamepad1.right_bumper){
                intakeComponent.in();
            }
            else if(gamepad1.left_bumper){
                intakeComponent.expel();
            }
            else {
                intakeComponent.stop();
            }


            //gamepad 2
            if(gamepad2.x){
                stackerComponent.stackerDump();
            }
            else if (gamepad2.right_bumper){
                stackerComponent.toggleWhacker();
            }
            else if(gamepad2.dpad_up){
                stackerComponent.stackerUp();
            }
            else if (gamepad2.dpad_down){
                stackerComponent.stackerDown();
            }
            if (gamepad2.left_bumper) {
                stackerComponent.safeWhack();
            }

            if(gamepad2.right_stick_y > 0){
                launcherComponent.shoot();
            }
            else if (gamepad2.right_stick_y < 0){
                launcherComponent.reverse();
            }
            else {
                launcherComponent.stop();
            }

            //wobble arm controls
            if(gamepad2.dpad_right){
                grabberComponent.clawOpen();
            }
            else if(gamepad2.dpad_left){
                grabberComponent.clawClose();
            }
            if(Math.abs(gamepad2.left_stick_y) > 0.1 ){
                if(gamepad2.left_stick_y < 0){
                    grabberComponent.armUp();
                }
                if(gamepad2.left_stick_y > 0){
                    grabberComponent.armDown();
                }
            }

            /* full shooting command
                1. starting the launcher; setting it with a runtime of 10 sec
                2. setting the number of whacks to 3
                3. Stacker moves up
                4. If the launcher has reached max speed; then start whacking
                5. Once we're finished whacking, move the stacker (now empty) down
                6. Stop the launcher
             */

            if(gamepad2.a){
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




        }


    }
}
