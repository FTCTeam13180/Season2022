package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class AutonomousTasks{

    enum State {
        INIT,
        MOVE_TO_TARGET_ZONE,
        MOVE_BEFORE_LAUNCH_LINE,
        LAUNCH_RINGS,
        PICK_UP_RINGS,
        PARK_AT_LAUNCH_LINE,
        STOP,
    }

    AutonomousTasks.State state = AutonomousTasks.State.INIT;
    ElapsedTime time;
    double TimeoutMs;
    OpMode op;

    public void init(){
        time = new ElapsedTime();
        time.reset();
    }

    public void detect(){
    }

    public void moveToTargetZone() {
    }

    public void moveBeforeLaunchLine() {
    }

    public void launchRings() {
    }

    public void pickUpRings() {
    }

    public void parkAtLaunchLine() {
    }

    public void stop (){

    }

    public AutonomousTasks(Servo hook, double TimeoutMs, ParallelStateMachineOpMode opMode){
        this.TimeoutMs = TimeoutMs;
        this.op = opMode;
    }

    public void run() {

        switch(state){

            case INIT:
                init();
                state = State.MOVE_TO_TARGET_ZONE;
                break;

            case MOVE_TO_TARGET_ZONE:
                moveToTargetZone();
                state = State.MOVE_BEFORE_LAUNCH_LINE;
                break;

            case MOVE_BEFORE_LAUNCH_LINE:
                moveBeforeLaunchLine();
                state = State.LAUNCH_RINGS;
                break;

            case LAUNCH_RINGS:
                launchRings();
                state = State.PICK_UP_RINGS;
                break;

            case PICK_UP_RINGS:
                pickUpRings();
                state = State.PARK_AT_LAUNCH_LINE;
                break;

            case PARK_AT_LAUNCH_LINE:
                parkAtLaunchLine();
                state = State.STOP;
                break;

            case STOP:
                stop();
                break;


        }
    }
}


