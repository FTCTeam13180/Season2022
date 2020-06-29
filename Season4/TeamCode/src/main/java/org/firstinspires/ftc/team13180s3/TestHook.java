package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestHook", group="manualmode")
public class TestHook extends LinearOpMode {
    public Hook hook;
    @Override
    public void runOpMode() throws InterruptedException {
        hook=new Hook(this);
        hook.init();
        waitForStart();
        while (opModeIsActive()){
            if(gamepad1.a){
                hook.attach();
            }

            else if(gamepad1.b){
                hook.detach();
            }
        }
    }
}
