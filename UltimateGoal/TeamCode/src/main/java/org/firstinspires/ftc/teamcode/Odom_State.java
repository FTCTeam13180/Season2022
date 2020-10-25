//Made by Rohan Gulati
//Set of Classes used in Odometry program. Array of Odom_States are used and looped through for autonomous period
package org.firstinspires.ftc.teamcode;

//X Y Coordinate on Playing Field

public class Odom_State {
    public enum State{
        NextPoint,
        Turn,
        State_A,
        State_B,
        State_C,
        Sleep
    }
    public State state;
    public Point point;
    private double turn;
    private double sleep;

    public double getTurn(){
        return turn;
    }

    public double getSleep(){
        return sleep;
    }

    Odom_State(State s, double px, double py,double p){
        if(s==State.NextPoint) {
            point = new Point(px, py,p);
            state = s;
            turn = 0;
            sleep = 0;
        }
    }
    Odom_State(State s, double x){
        if(s==State.Turn){
            turn = x;
            sleep = 0;
        }
        else if (s==State.Sleep){
            sleep = x;
            turn = 0;
        }
    }


}
