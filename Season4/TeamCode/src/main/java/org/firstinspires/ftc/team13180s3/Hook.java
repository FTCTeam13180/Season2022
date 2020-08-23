package org.firstinspires.ftc.team13180s3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
public class Hook {
    public LinearOpMode opMode;
    private Servo hook;

    Hook(LinearOpMode op) {
        opMode = op;
    }

    public void init() {
        hook = opMode.hardwareMap.get(Servo.class, "Hook");
    }

    public void attach() {

        hook.setPosition(1);
    }

    public void detach() {

        hook.setPosition(0);
    }
    public void halfattach(double value){
        hook.setPosition(value);
    }
}