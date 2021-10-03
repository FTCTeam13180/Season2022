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

    class Point{
        private double X;
        private double Y;
        private double power;
        Point(double x, double y, double p){
            X = x;
            Y = y;
            power = p;
        }
        public double getX(){
            return X;
        }
        public double getY(){
            return Y;
        }
        public double getPower(){
            return power;
        }

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
