/**
 *
 * @author Rob Thomas
 * 
 * Data Structures
 * Homework 8
 * Part Two
 * 
 * This program finds all lines on which at least 4 points in a set of points
 * exist. It finds collinear points by checking the angles to other points.
 * 
 * This program performs in quadratic-logarithmic time based on N 
 * (running time ~ (N^2)log(N)), the number of points being analyzed. This is 
 * because there is one loop that operates over (nearly) each of the points, and
 * inside this loop the most time-expensive operation is the sorting method, 
 * which is performed for each loop iteration in "linearithmic" time.
 * 
 * It is notable that the algorithm will take more time depending on the number
 * of lines present amidst the points. This is because the findChain method will
 * be called more often when more lines are present. However, since this method
 * operates in linear time, it does not throw off the quadratic-logarithmic
 * performance of the algorithm. 
 * 
 * This class requires the algs4 library to function, which can be obtained 
 * free of charge at http://algs4.cs.princeton.edu/code/.
 * 
 * Empirical Evidence for Quadratic Performance:
 * Interestingly, when this algorithm is performed on data sets containing few
 * to no lines, the algorithm appears to perform in (better than) quadratic 
 * time. This can be observed in the output below:
 * 
 * >java Fast < .\collinear-data\input1000.txt
Processing took 2.73 seconds for N = 1000

>java  Fast < .\collinear-data\input2000.txt
Processing took 9.362 seconds for N = 2000

>java Fast < .\collinear-data\input3000.txt
Processing took 21.203 seconds for N = 3000
* 
* Observe that doubling the size of N nearly quadrupled the processing time
* (2.73 * 4 = 10.92) and that tripling the size of N didn't quite multiply the
* processing time by 9 (2.73 * 9 = 24.57). These performance times are 
* substantially less than the predicted quadratic-logarithmic performance times.
* However, when the Brute algorithm is applied to the input400.txt file
* (the largest of the files with lines), it actually finishes faster than the
* Fast algorithm (0.50 seconds vs. 0.968 seconds). I theorize that the Fast
* algorithm beats out the Brute algorithm in the realm of processing raw points,
* but is weaker in the realm of processing actual lines. This can be attributed
* to the findChain method inside the Fast algorithm, which takes more time
* depending on the number of lines present.
 */

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Arrays;

public class Fast 
{
    static private Point[] points;
    static private Point[] angles;
    
    /*findChain looks for a chain of equal values of size numInChain inside
        of the angles array starting at startIndex. It returns the index
        of the first element of the chain found (if any). If no chain is found,
        it returns -1.
        This method performs in approximately linear time. While at first 
        glance it may appear that it would perform in quadratic time due to
        the two nested loops present, the inner loop actually actually only
        goes through at most 3 iterations since numInChain will always be 3 for
        this algorithm. Thus, the outer loop contains only constant-time 
        operations, giving the entire method linear performance.*/
    public static int findChain(int startIndex, int numInChain, int originIndex)
    {
        /*Move point by point, checking if the following point has the same
            angle with the origin.*/
        for (int x = startIndex; x < angles.length - 1; x++)
        {
            /*Determine the angle between the origin point and the point
                at x.*/
            double currentAngle = points[originIndex].getAngleTo(angles[x]);
            
            /*Begin looking for a chain of size numInChain in the following 
                elements.*/
            for (int y = x + 1; y < angles.length; y++)
            {
                /*Determine the angle at y.*/
                double nextAngle = points[originIndex].getAngleTo(angles[y]);
                /*If the angle doesn't match current angle, then the current
                    chain is of insufficient length.*/
                if (currentAngle != nextAngle)
                {
                    break;
                }
                /*If the angle does match and numInChain has been fulfilled,
                    return the start index of this chain: x.*/
                if (numInChain <= y - x + 1)
                {
                    return x;
                }
            }
        }
        
        /*If all searching fails, return -1 to indicate failure.*/
        return -1;
    }
    
    /*findChainLength finds the length of the chain of identical values inside
        of the angles array starting at chainIndex.*/
    public static int findChainLength(int chainIndex, int originIndex)
    {
        double currentAngle = points[originIndex].getAngleTo(angles[chainIndex]);
        for (int x = chainIndex + 1; x < angles.length; x++)
        {
            double nextAngle = points[originIndex].getAngleTo(angles[x]);
            if (currentAngle != nextAngle)
            {
                return x - chainIndex;
            }
        }
        
        return angles.length - chainIndex;
    }
    
    public static void main(String[] args)
    {
        /*N is the number of Points being processed.*/
        final int N = StdIn.readInt();
        
        /*Declare and initialize an array of all the Points to be processed.*/
        points = new Point[N];
        for (int x = 0; x < points.length; x++)
        {
            points[x] = new Point(StdIn.readInt(), StdIn.readInt());
        }
        
        /*Sort the points array lexicographically.*/
        Arrays.sort(points);
        
        /*Turn on animation mode so everything is displayed at once.*/
        StdDraw.setXscale(32767 * -0.05, 32767 * 1.1);
        StdDraw.setYscale(32767 * -0.05, 32767 * 1.1);
        StdDraw.show(0);
        
        /*Start the stopwatch to measure how much time processing takes.*/
        Stopwatch timer = new Stopwatch();
        
        /*Begin processing the points one at a time. Each point will have
            the following points sorted by the angle they make with the
            current point. If a chain of at least 3 other points with the same
            angle is found, then a solution has been found. Solutions will be
            printed and displayed immediately.*/
        for (int x = 0; x < points.length - 3; x++)
        {
            /*Create a new array containing all the elements of the points array
                following x.*/
            angles = Arrays.copyOfRange(points, x + 1, points.length);
 
            /*Sort the new array by the angle each point makes with x.*/
            Arrays.sort(angles, points[x].SLOPE_ORDER);
            
            int chainIndex = 0;
            while (chainIndex > -1)
            {
                /*Search the angles array for a chain of at least 3 angles.*/
                chainIndex = findChain(chainIndex, 3, x);
                if (chainIndex > -1)
                {
                    /*Find the actual length of that chain.*/
                    int chainLength = findChainLength(chainIndex, x);
                    
                    /*Make an array of all the points on the line found. Use
                        this array to print out the points and also dispay
                        them.*/
                    Point[] pointsOnLine = new Point[chainLength + 1];
                    pointsOnLine[0] = points[x];
                    for (int y = 1; y < pointsOnLine.length; y++)
                    {
                        pointsOnLine[y] = angles[chainIndex + y - 1];
                    }
                    
                    /*Sort pointsOnLine lexicographically so it's back in the
                        desired order.*/
                    Arrays.sort(pointsOnLine);
                    
                    /*Print out pointsOnLine.*/
                    StdOut.print(pointsOnLine.length + ": ");
                    for (int y = 0; y < pointsOnLine.length - 1; y++)
                    {
                        StdOut.print(pointsOnLine[y] + " -> ");
                    }
                    StdOut.print(pointsOnLine[pointsOnLine.length - 1]);
                    StdOut.println();
                    
                    /*Draw a line from the first point of currentArray to the last
                        point of currentArray.*/
                    pointsOnLine[0].drawTo(pointsOnLine[
                        pointsOnLine.length - 1]);
                    
                    /*Set chainIndex to one past the end of the chain.*/
                    chainIndex += chainLength;
                    
                    /*Search the angles array following that chain for more chains.*/
                }
                /*If the search for a chain failed, stop looking for solutions.*/
            }
        }
        
        /*Print the time it took to process all the points/lines.*/
        StdOut.println("Processing took " + timer.elapsedTime() + 
            " seconds for N = " + N);
        
        /*Display all points.*/
        for (Point p: points)
        {
            p.draw();
        }

        /*With processing, printing, and drawing done, display all the points.*/
        StdDraw.show(0);
    }
}
