/*
 * Copyright (c) 2019 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
import org.openftc.easyopencv.OpenCvInternalCamera;
    class ColorPipeline extends OpenCvPipeline
    {
        double leftRed = 0;
        double midRed = 0;
        double rightRed = 0;
        char color = 'r';// red or blue what do we want to detect
        Mat inputHSV = new Mat();
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
            int tempRedR = 0;
            boolean redIsPresent;
            Imgproc.cvtColor(input,inputHSV, Imgproc.COLOR_RGB2HSV);
            // iterate through rows


            // draw grid lines on our preview
            drawGridLines(input);


            for(int row = 50;row < inputHSV.rows()-50; row++){//intill I optimise m
                for(int col= 5;col < inputHSV.cols()-5;col++){
                    redIsPresent = ((inputHSV.get(row, col)[0] < 12) || (inputHSV.get(row, col)[0] > 175));

                    if(col < input.cols()/3){
                        if( redIsPresent){
                            tempRedL++;
                        }
                    }
                    if((col < ((double)input.cols())*(2.0/3.0)) && col > input.cols()/3){
                        if( redIsPresent){
                            tempRedM++;
                        }
                    }
                    if((col < input.cols()*(3/3)) && col > ((double)input.cols())*(2.0/3.0)){
                        if(redIsPresent){
                            tempRedR++;
                        }
                    }

                }
            }

            leftRed = tempRedL;
            rightRed = tempRedR;
            midRed = tempRedM;

            return input;
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
        public void drawGridLines(Mat input){
            Imgproc.rectangle(
                    input,
                    new Point(
                            input.cols()/3,
                            input.rows()),
                    new Point(
                            input.cols()/3,
                            0),
                    new Scalar(200, 0, 40), 4);
            Imgproc.rectangle(
                    input,
                    new Point(
                            input.cols()*(2/3),
                            input.rows()),

                    new Point(
                            ((double)input.cols())*(2.0/3.0),
                            0),
                    new Scalar(0, 255, 0), 4);

        }


        }

