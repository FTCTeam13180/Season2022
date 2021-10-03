package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

/**
 * A Spline is route for moving from the current robot position to the destination. A spline
 * has an ordered list of Waypoints. The algorithm for moving along the spline effectively
 * fits a curve to the waypoints, so that the actual path taken is a smooth curve.
 * <br/>
 * A Spline must have at least one Waypoint.
 * <br/>
 * The last waypoint of the Spline is the <b>destination</b>.
 * <br/>
 * The Spline is <b>completed</b> when the destination has been reached.
 * <br/>
 * The <b>target waypoint</b> is the waypoint towards which the robot is moving. All waypoints
 * before the target waypoint have been reached, and the target waypoint and all subsequent
 * waypoints have not been reached. If the spline is completed, there is no target waypoint.
 * <br/>
 * Robot is <b>"moving to the destination"</b> when all way points other than the destination have
 * been reached and the destination has not been reached.
 * <br/>
 * @see Waypoint
 */
public class Spline {

    private ArrayList<Waypoint> waypoints = new ArrayList<>();
    private boolean corrected = false;

    public Spline(Waypoint p) {
        add(p);
    }

    public void add(Waypoint p) {
        waypoints.add(p);
    }

    public Waypoint getDestination() {
        return waypoints.get(waypoints.size() - 1);
    }

    public boolean isCompleted() {
        return getDestination().isReached();
    }

    public boolean movingToDestination() {
        return reachedAllWayPointsExceptDestination() && !getDestination().isReached();
    }

    public Waypoint getTargetWaypoint() {
        if (isCompleted())
            throw new RuntimeException("Spline completed. No target waypoint");

        Waypoint targetWaypoint = null;
        for (Waypoint p : waypoints) {
            if (!p.isReached()) {
                targetWaypoint = p;
                break;
            }
        }
        return targetWaypoint;
    }

    private boolean reachedAllWayPointsExceptDestination() {
        boolean reachedAllWayPointsExceptDestination = true;
        for (int i = 0; i < waypoints.size() - 1; i++) {
            if (!waypoints.get(i).isReached()) {
                reachedAllWayPointsExceptDestination = false;
                break;
            }
        }
        return reachedAllWayPointsExceptDestination;
    }

    public boolean isCorrected() {
        return corrected;
    }

    public void setCorrected() {
        corrected = true;
    }
}
