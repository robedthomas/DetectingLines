/*************************************************************************
 * Name:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.*;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER;       // YOUR DEFINITION HERE

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
        SLOPE_ORDER = new AngularComparator(this);
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        return ((double) (this.y - that.y)) / ((double) (this.x - that.x));
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    @Override
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y < that.y)
        {
            return -1;
        }
        if (this.y > that.y)
        {
            return 1;
        }
        if (this.x < that.x)
        {
            return -1;
        }
        if (this.x > that.x)
        {
            return 1;
        }
        
        return 0;
    }
    
    /*CUSTOM METHOD*/
    public int getX()
    {
        return x;
    }
    
    /*CUSTOM METHOD*/
    public int getY()
    {
        return y;
    }
    
    /*CUSTOM METHOD*/
    public double getAngleTo(Point that)
    {
        double theta = Math.atan((double)(this.y - that.y) /
            (double)(this.x - that.x));
        /*Correct for arctan only spanning the first and fourth quadrants.*/
        if (that.x < this.x)
        {
            theta = Math.PI - theta;
        }
        /*If theta is still negative, add 2pi to it.*/
        if (theta < 0)
        {
            theta += 2 * Math.PI;
        }
        
        return theta;
    }
    
    private class AngularComparator implements Comparator<Point>
    {
        Point origin;
        public AngularComparator(Point origin)
        {
            this.origin = origin;
        }
        
        @Override
        public int compare(Point p, Point q)
        {
            if (origin.getAngleTo(p) > origin.getAngleTo(q))
            {
                return 1;
            }
            
            if (origin.getAngleTo(p) < origin.getAngleTo(q))
            {
                return -1;
            }
            
            return 0;
        }
    }

    // return string representation of this point
    @Override
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) 
    {
        /* YOUR CODE HERE */
        Point point1 = new Point(9850, 500);
        Point point2 = new Point(32050, 3500);
        Point point3 = new Point(27150, 4500);
        StdOut.println(point1.slopeTo(point2) + " should be 1.");
        StdOut.println(point1.slopeTo(point3) + " should be 2.");
        StdOut.println(point2.slopeTo(point3) + " should be 3.");
    }
}
