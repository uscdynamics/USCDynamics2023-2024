package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Disabled
public class NoNoColorMaskPipeline extends OpenCvPipeline
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


    Scalar lower1 = new Scalar(0,100,70);
    Scalar lower2 = new Scalar(160,100,70);
    Scalar upper1 = new Scalar(18,255,255);
    Scalar upper2 = new Scalar(180,255,255);
//blue
    Scalar blower = new Scalar(80,130,45);
    Scalar bupper = new Scalar(135,255,255);
    Mat blueMask =  new Mat();

    int redLSum = 0;//ah
    int redRSum = 0;

    int blueLSum = 0;
    int blueRSum = 0;



    @Override
    public Mat processFrame(Mat input)
    {



        Imgproc.cvtColor(input,inputHSV, Imgproc.COLOR_RGB2HSV);
        Core.inRange(inputHSV,lower1,upper1,redMask1);
        Core.inRange(inputHSV,lower2,upper2,redMask2);

        Core.bitwise_or(redMask1,redMask2,fullRedMask);
        Core.inRange(inputHSV,blower,bupper,blueMask);

        blueLSum = Core.countNonZero(blueMask.submat(0,blueMask.rows(),0,blueMask.cols()/2));
        blueRSum = Core.countNonZero(blueMask.submat(0,blueMask.rows(),blueMask.cols()/2,blueMask.cols()));


        redLSum = Core.countNonZero(fullRedMask.submat(0,fullRedMask.rows(),0,fullRedMask.cols()/2));
        redRSum = Core.countNonZero(fullRedMask.submat(0,fullRedMask.rows(),fullRedMask.cols()/2,fullRedMask.cols()));





        // draw grid  lines on our preview
        drawGridLines(input);

        return blueMask;
    }
//    public String getDetection(String color, boolean isBackStage){
//
//
//    }


    public int getLeftRed(){
        return redLSum;
    }

    public int getRightRed(){
        return redRSum;
    }
    public int getLeftBlue(){
        return blueLSum;
    }

    public double getRightBlue(){
        return blueRSum;
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