package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Core;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Disabled
public class ColorMaskPipeline extends OpenCvPipeline
{
    double leftRed = 0;
    double midRed = 0;
    double rightRed = 0;
    char color = 'r';// red or blue what do we want to detect
    Mat inputHSV = new Mat();
    String ruhRowRixel = "FUCK";
    boolean viewportPaused;
    Mat redMask1 =  new Mat();
    Mat redMask2 = new Mat();
    Mat fullRedMask = new Mat();


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
        int tempRedM = 0;
        int tempRedR = 700;
        boolean redIsPresent;
        Scalar lower1 = new Scalar(0,120,120);
        Scalar lower2 = new Scalar(160,120,120);
        Scalar upper1 = new Scalar(18,255,255);
        Scalar upper2 = new Scalar(180,255,255);



        Imgproc.cvtColor(input,inputHSV, Imgproc.COLOR_RGB2HSV);
        Core.inRange(inputHSV,lower1,upper1,redMask1);
        Core.inRange(inputHSV,lower2,upper2,redMask2);

        Core.bitwise_or(redMask1,redMask2,fullRedMask);
        Imgproc.find




        // draw grid lines on our preview
        drawGridLines(input);

        return fullRedMask;
    }
//    public String getDetection(String color, boolean isBackStage){
//
//
//    }


    public double getLeftRed(){
        return leftRed;
    }
    public double getMidRed(){
        return midRed;
    }
    public double getRightRed(){
        return rightRed;
    }
    public String getPix(){
        return ruhRowRixel;
    }
    public void drawGridLines(Mat input) {
        Imgproc.rectangle(
                input,
                new Point(
                        input.cols() / 2,
                        input.rows()),
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
//        Imgproc.rectangle(
//                input,
//                new Point(
//                        input.cols()*(2/3),
//                        input.rows()),
//
//                new Point(
//                        ((double)input.cols())*(2.0/3.0),
//                        0),
//                new Scalar(0, 255, 0), 4);
//
//    }


    }}