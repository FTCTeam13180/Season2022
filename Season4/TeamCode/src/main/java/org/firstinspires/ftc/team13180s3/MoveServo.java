package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MoveServo implements BasicCommand{

    enum State {
        INIT,
        EXECUTE,
        STOP
    }

    State state = State.INIT;
    Servo servo;
    ElapsedTime time;
    double TimeoutMs;
    OpMode op;

    public void init(){
        servo.setPosition(0);

        time = new ElapsedTime();

        time.reset();
    }

    public void execute(){
        if(time.milliseconds() >= TimeoutMs){
            state = State.STOP;
        }
        //every five seconds move the servo and change the position
        else if(time.milliseconds() >= 15000){
            servo.setPosition(1);
            op.telemetry.addData("Moveservo: ", "Position Changed");
        }
        else if(time.milliseconds() >= 10000){
            servo.setPosition(0);
            op.telemetry.addData("Moveservo: ", "Position Changed");
        }
        else if(time.milliseconds() >= 5000){
            servo.setPosition(1);
            op.telemetry.addData("Moveservo: ", "Position Changed");
        }
    }

    public void stop (){
        servo.setPosition(0);
    }

    public MoveServo(Servo servo, double TimeoutMs, ParallelStateMachineOpMode opMode){
        this.servo = servo;
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
