package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
@Disabled
public class ColorPipeline extends OpenCvPipeline
{
    double leftRed = 0;
    double midRed = 0;
    double rightRed = 0;
    char color = 'r';// red or blue what do we want to detect
    Mat inputHSV = new Mat();
    String ruhRowRixel = "FUCK";
    boolean viewportPaused;


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
        Imgproc.cvtColor(input,inputHSV, Imgproc.COLOR_RGB2HSV);

        // iterate through row


        // draw grid lines on our preview
        drawGridLines(input);

        return input;
    }
    public String getDetection(String color, boolean isBackStage){
        int tempRedL = 0;
        int tempRedM = 2000;
        int tempRedR = 0;
        boolean colorIsPresent;
        // iterate through rows
        // draw grid lines on our peview
        for(int row = inputHSV.rows()/2;row < inputHSV.rows(); row++){//intill I optimise m
            for(int col= 5;col < inputHSV.cols()-5;col++){
                if(color.equals("red")){
                    colorIsPresent = ((inputHSV.get(row, col)[0] < 12) || (inputHSV.get(row, col)[0] > 175));
                }
                else{
                    colorIsPresent = ((inputHSV.get(row, col)[0] > 130/0) || (inputHSV.get(row, col)[0] < 145)); //idk what blue is just a guess


                }
                if(col < inputHSV.cols()/3){
                    if( colorIsPresent){
                        tempRedL++;
                    }
                }
                if((col < ((double)inputHSV.cols())*(2.0/3.0)) && col > inputHSV.cols()/3){
                    if( colorIsPresent){
                        tempRedM++;
                    }
                }
                if((col < inputHSV.cols()*(3/3)) && col > ((double)inputHSV.cols())*(2.0/3.0)){
                    if(colorIsPresent){
                        tempRedR++;
                    }
                }

            }
        }
        ruhRowRixel = ""+inputHSV.get(inputHSV.rows()/2,inputHSV.cols()/2)[0];

        leftRed = tempRedL;
        rightRed = tempRedR;
        midRed = tempRedM;
        if((color.equals("red") && isBackStage) ||(color.equals("blue") && !isBackStage) ){
            if (( rightRed >midRed) && rightRed > 1600){
                Imgproc.putText(inputHSV, "right", new Point(70, 200), 20, 40, new Scalar(200, 0, 40));

                return "right";

            }
            else if ((rightRed < midRed) && midRed > 1600){
                Imgproc.putText(inputHSV, "middle", new Point(70, 200), 20, 40, new Scalar(200, 0, 40));

                return "middle";
            }
            else{
                Imgproc.putText(inputHSV, "left", new Point(70, 200), 1, 40, new Scalar(200, 0, 40));

                return "left";
            }
        }
        else{
            if (( leftRed >midRed) && leftRed > 2200){
                Imgproc.putText(inputHSV, "right", new Point(70, 200), 20, 40, new Scalar(200, 0, 40));

                return "left";

            }
            else if ((leftRed < midRed) && midRed > 2200){
                Imgproc.putText(inputHSV, "middle", new Point(70, 200), 20, 40, new Scalar(200, 0, 40));

                return "middle";
            }
            else{
                Imgproc.putText(inputHSV, "left", new Point(70, 200), 1, 40, new Scalar(200, 0, 40));

                return "left";
            }
        }



    }


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