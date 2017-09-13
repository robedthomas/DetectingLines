
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
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
 * This program performs in cubic time based on N (running time ~ N^3), 
 * the number of points being analyzed. This is because the algorithm checks
 * each of the ~N^2 possible pairs of points, then checks for each pair the 
 * remaining ~N points. The distinguishing method of this algorithm is the
 * slopeTo method, which is done for each triplet of points being analyzed.
 * Since ~N^3 triplets of points exist, the slopeTo method is performed ~N^3 
 * times.
 * 
 * This class requires the algs4 library to function, which can be obtained 
 * free of charge at http://algs4.cs.princeton.edu/code/.
 * 
 * Empirical Evidence for Cubic Performance:
 * To obtain empirical evidence for this algorithm's cubic performance, I ran
 *  the following code:
 * >java BetterBrute < .\collinear-data\input1000.txt
PrProcessing took 7.332 seconds for N = 1000

java BetterBrute < .\collinear-data\input2000.txt
Processing took 58.144 seconds for N = 2000

java BetterBrute < .\collinear-data\input3000.txt
Processing took 197.778 seconds for N = 3000
* 
* Observe that doubling the size of N multiplied the processing time by ~8
* (7.332 * 8 = 58.656) and that tripling the size of N multiplied the
* processing time by ~27 (7.332 * 27 = 197.964). This provides very precise 
* evidence of the algorithm performing in cubic time.
 */

public class BetterBrute 
{    
    /*main runs the file input code.*/
    public static void main(String[] args)
    {
        /*solutions is a Queue of Integer arrays that will store the points 
            that share a line. A new Integer array will be enqueued whenever
            at least 4 points are found to fall on a single line. The new
            array will then be filled with the indeces of the corresponding 
            Points inside the point array.*/
        Queue<Integer[]> solutions = new Queue<Integer[]>();
        
        /*N is the number of Points being processed.*/
        final int N = StdIn.readInt();
        
        /*Declare and initialize an array of all the Points to be processed.*/
        Point[] points = new Point[N];
        for (int x = 0; x < points.length; x++)
        {
            points[x] = new Point(StdIn.readInt(), StdIn.readInt());
        }
        
        /*Sort the points lexicographically.*/
        Arrays.sort(points);
        
        /*Start the stopwatch for timing the processing.*/
        Stopwatch timer = new Stopwatch();
        
        
        
        /*Begin processing points/lines:*/
            /*Move up the array of Points, building Lines between the current
                two points.*/
        for (int x = 0; x < points.length - 3; x++)
        {
            for (int y = x + 1; y < points.length - 2; y++)
            {
                double currentSlope = points[x].slopeTo(points[y]);
                /*All following points must be examined to see if the slope from
                    the point at x to that point matches the slope from the
                    point at x to the point at y. If at least two other points
                    fall on that line, enqueue the array of indeces into the
                    solutions Queue.*/
                /*The Integer array pointsOnLine tracks the indeces of the 
                    Points found to be on currentLine. The first two points
                    will always be at x and y.*/
                Integer[] pointsOnLine = new Integer[N];
                pointsOnLine[0] = x;
                pointsOnLine[1] = y;
                /*nextIndex tracks the next index at which a Point's index
                    will be written.*/
                int nextIndex = 2;
                for (int z = y + 1; z < points.length; z++)
                {
                    /*If the the slope between x and z matches the slope between
                        x and y, then z is on the line.*/
                    if (points[x].slopeTo(points[z]) == 
                        points[x].slopeTo(points[y]))
                    {
                        pointsOnLine[nextIndex] = z;
                        nextIndex++;
                    }
                }
                
                /*If there were at least 4 points (including x and y) found on
                    the line, enqueue the array of these points into solutions.*/
                if (nextIndex >= 4)
                {
                    /*Copy the contents of pointsOnLine over to a new array
                        so that its length is now accurate.*/
                    Integer[] cleanArray = Arrays.copyOfRange(pointsOnLine, 0, 
                        nextIndex);
                    
                    solutions.enqueue(cleanArray);
                }
            }
        }
        
        /*Read from the stopwatch the time that processing took.*/
        double time = timer.elapsedTime();
        
        /*With the processing of points done, the text and graphic output
            must be done. First, display all the points in graphics.*/ 
        StdDraw.setXscale(32767 * -0.05, 32767 * 1.1);
        StdDraw.setYscale(32767 * -0.05, 32767 * 1.1);
        StdDraw.show(0);
        
        for (Point p: points)
        {
            p.draw();
        }
        
        /*Dequeue each element of solutions one at a time. The elements are 
            arrays of point indeces. Each time one is dequeue'd, print out its
            size followed by the points corresponding to each of its elements.
            Then, draw a line from the first point to the last point.*/
        while (!solutions.isEmpty())
        {
            Integer[] currentArray = solutions.dequeue();
            StdOut.print(currentArray.length + ": ");
            for (int x = 0; x < currentArray.length - 1; x++)
            {
                StdOut.print(points[currentArray[x]] + " -> ");
            }
            StdOut.print(points[currentArray[currentArray.length - 1]]);
            StdOut.println();
            
            /*Draw a line from the first point of currentArray to the last
                point of currentArray.*/
            points[currentArray[0]].drawTo(
                points[currentArray[currentArray.length - 1]]);
        }
        
        /*Print out the time that processing took.*/
        StdOut.println("Processing took " + time + " seconds for N = " + N);
        
        /*Display all output at once.*/
        StdDraw.show(0);
    }
}
