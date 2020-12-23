package org.firstinspires.ftc.teamcode;

/**
 * A waypoint is an intermediate point or place on a route or line of travel, a stopping point or
 * point at which course is changed (per Wikipedia).
 * <br/>
 * A waypoint has X and Y coordinates. These coordinates are defined on a cartesian coordinate
 * plane. The origin (point (0,0)) of the coordinate plane is the front left corner of the field.
 * The units of X and Y are centimeters.
 * <br/>
 * A waypoint has the power with which the robot should move to it.
 * <br/>
 * A waypoint knows whether it has been reached or not. A waypoint is initialized to the not reached
 * state. <code>setReach</code> changes it's state to reached. Once it is reached, a waypoint can't
 * be reset to the not reached state.
 * <br/>
 * @see "https://en.wikipedia.org/wiki/Waypoint"
 * @see Spline
 */
public class Waypoint {
    private double X;
    private double Y;
    private double power;
    private boolean reached = false;

    Waypoint(double x, double y, double p){
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
    public boolean isReached() { return reached; }
    public void setReached() { reached = true; }
}
