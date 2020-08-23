package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MoveHook implements BasicCommand{

    enum State {
        INIT,
        EXECUTE,
        STOP
    }

    State state = State.INIT;
    Servo hook;
    ElapsedTime time;
    double TimeoutMs;
    OpMode op;

    public void init(){
        hook.setPosition(0);

        time = new ElapsedTime();

        time.reset();
    }

    public void execute(){
        if(time.milliseconds() >= TimeoutMs){
            state = State.STOP;
        }
        //every five seconds move the hook
        else if(time.milliseconds() >= 15000){
            hook.setPosition(1);
            op.telemetry.addData("MoveHook: ", "Position Changed");
        }
        else if(time.milliseconds() >= 10000){
            hook.setPosition(0);
            op.telemetry.addData("MoveHook: ", "Position Changed");
        }
        else if(time.milliseconds() >= 5000){
            hook.setPosition(1);
            op.telemetry.addData("MoveHook: ", "Position Changed");
        }
    }

    public void stop (){
        hook.setPosition(0);
    }

    public MoveHook(Servo hook, double TimeoutMs, MyOpMode opMode){
        this.hook = hook;
        this.TimeoutMs = TimeoutMs;
        this.op = opMode;
    }

    public void run() {

        switch(state){

            case INIT:
                init();
                state = State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                break;

            case STOP:
                stop();
                break;


        }
    }
}
