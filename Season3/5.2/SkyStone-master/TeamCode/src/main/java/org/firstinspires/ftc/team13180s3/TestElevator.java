package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestElevator", group="manualmode")
public class TestElevator extends LinearOpMode {
    public Elevator elevator;
    @Override
    public void runOpMode() throws InterruptedException {
        elevator =new Elevator (this);
        elevator.init();
        waitForStart();
        while (opModeIsActive()){
            double y=gamepad1.left_stick_y;
            if (y > 0.1){
                elevator.goUp(y);
            }
            else if (y < -0.1)
            {
                elevator.goDown(y);
            }
            else
                elevator.stopUpDown();


            if(gamepad2.right_bumper){
                elevator.goOutByRotations(1.0,1);
            }
              else if(gamepad2.left_bumper){
                elevator.goInByRotations(1.0, 1);
            }


             if(gamepad2.a){
                elevator.homeposition();
            }
            else if(gamepad2.b){
                elevator.playposition();
            }
            if(gamepad1.x){
                elevator.goUpByInches(1,4);
            }
            else if(gamepad1.y){
                elevator.goDownByInches(1,4);
            }
        }
    }
}
