
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Arrays;

/**
 *
 * @author Rob Thomas
 * 
 * Data Structures
 * Homework 8
 * Part One
 * 
 * This program finds all lines on which at least 4 points in a set of points
 * exist. It uses a "brute force" method that examines each pair of points.
 * 
 * This program performs in quartic (N^4) time. This is evidenced by the four
 * nested loops present, each of which iterates over nearly the entire array of
 * points. 
 * 
 * This class requires the algs4 library to function, which can be obtained 
 * free of charge at http://algs4.cs.princeton.edu/code/.
 * 
 * Empirical Evidence for Quartic Performance:
 * During testing, the algorithm appeared to perform slightly better than
 * quartic performance. However, the presence of lines in a data set will 
 * increase the time it takes to process the data.
 * 
 * >java Brute < .\collinear-data\input200.txt
 * Processing took 3.195 seconds for N = 200
* 
* >java Brute < .\collinear-data\input400.txt
* Processing took 50.084 seconds for N = 400
* (3.195 * 2^4 = 51.12)
* 
* >java Brute < .\collinear-data\input1000.txt
* Processing took 1681.286 seconds for N = 1000
* (3.195 * 5^4 = 1996.875)
* 
* Observe that doubling the number of points multiplied the processing time by
* roughly 16, as quartic performance would indicate. Similarly, multipling the
* number of points by 5 multiplied the processing time by nearly 625 (or 5^4).
 */

public class Brute 
{
    /*main takes in a list of points and outputs each line found which connects
        at least 4 of those points. Output is both textual and drawn.*/
    public static void main (String[] args)
    {
        /*Turn on animation mode so everything is displayed at once.*/
        StdDraw.setXscale(32767 * -0.05, 32767 * 1.1);
        StdDraw.setYscale(32767 * -0.05, 32767 * 1.1);
        StdDraw.show(0);
        
        /*Read in the number of points, the first token.*/
        final int N = StdIn.readInt();
        
        /*Construct the array of points. This array will keep track of all the
            points being processed.*/
        Point[] points = new Point[N];
        
        /*Read in each of the points.*/
        for (int x = 0; x < N; x++)
        {
            points[x] = new Point(StdIn.readInt(), StdIn.readInt());
        }
        
        /*Sort the points.*/
        Arrays.sort(points);
        
        /*Draw all the points.*/
        for (Point p: points)
        {
            p.draw();
        }
        
        /*Begin timing the point processing.*/
        Stopwatch timer = new Stopwatch();
        
        /*Begin processing points.*/
        for (int x = 0; x < N - 3; x++)
        {
            for (int y = x + 1; y < N - 2; y++)
            {
                for (int z = y + 1; z < N - 1; z++)
                {
                    for (int w = z + 1; w < N; w++)
                    {
                        /*If the slope between x and y is the same as that 
                            between x and z and x and w, then a four-tuple
                            has been found. Draw a line between x and w, and
                            print out the four-tuple.*/
                        if (points[x].slopeTo(points[y]) == 
                                points[x].slopeTo(points[z]) && 
                                points[x].slopeTo(points[y]) == 
                                points[x].slopeTo(points[w]))
                        {
                            StdOut.println("4: " + points[x] + " -> " + points[y] + 
                                " -> " + points[z] + " -> " + points[w]);
                            points[x].drawTo(points[w]);
                        }
                    }
                }
            }
        }
        
        /*Print out the time that processing took.*/
        StdOut.println("Processing took " + timer.elapsedTime() +
            " seconds for N = " + N);
        
        /*Display all output at once.*/
        StdDraw.show(0);
    }
}
