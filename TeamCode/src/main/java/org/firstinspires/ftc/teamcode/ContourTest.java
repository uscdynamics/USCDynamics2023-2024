package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvTracker;


@Disabled
public class ContourTest extends OpenCvTracker
{
    Mat rgbImage;



    /*
     * NOTE
     * : if you wish to use additional Mat objects in your processing pipeline, it is
     * highly recommended to declare them here as instance variables and re-use them for
     * each invocation of processFrame(), rather than declaring them as new local variables
     * each time through processFrame(). This removes the danger of causing a memory leak
     * by forgetting to call mat.release(), and it also reduces memory pressure by not
     * constantly allocating and freeing large chunks of memory.
     */

    @Override
    public Mat processFrame(Mat input)
    {

        int tempRedL = 0;

        // iterate through row


        // draw grid lines on our preview
        drawGridLines(input);

        return input;
    }
    public String getRedLocation(){
        //Imgproc.findContours();


        return "";
    }


    public void drawGridLines(Mat input) {
        Imgproc.rectangle(
                input,
                new Point(
                        input.cols() / 2,
                        input. rows()),
                new Point(
                        input.cols() / 2,
                        0),
                new Scalar(200, 0, 40), 4);
        Imgproc.rectangle(
                input,
                new Point(
                        input.cols(),
                        input.rows()/2),
                new Point(
                        0,
                        input.rows()/2),
                new Scalar(200, 0, 40), 4);



    }}