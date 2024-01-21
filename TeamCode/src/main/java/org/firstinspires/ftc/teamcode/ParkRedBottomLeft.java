/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name="Robot: ParkRedBottomLeft", group="Robot")

public class ParkRedBottomLeft extends UscOpMode {
    OpenCvWebcam webcam;
    String a = "";
    ColorMaskPipeline pipeline = new ColorMaskPipeline();


    @Override
    public void runOpMode()
    {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        // setUpHardware(true,false,false,true,false, false);
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
            }
        });


        while (opModeIsActive())
        {


//
//            if (pipeline.getDetection("red",true).equals("right")){
//                telemetry.addData("Detection", "RIGHTY ");
//                 telemetry.update();
//                 strafeLeft(30,60);
//                moveForward(810,900);
//                 turnRight(90,800);
//                moveForward(80,50);
//                clawServo1.setPosition(OPEN_CLAW_1);
//                clawServo2.setPosition(OPEN_CLAW_2);
//
//            }
//            //right
//            else if (pipeline.getDetection("red",true).equals("middle")){
//                strafeLeft(30,60);
//
//                telemetry.addData("Detection", "middley ");
//                telemetry.update();
//                moveForward(1100,800);
//                clawServo1.setPosition(OPEN_CLAW_1);
//                clawServo2.setPosition(OPEN_CLAW_2);
//               // moveBackward(35,1000);
//
//
//            }
//            else{
//                strafeLeft(30,60);
//
//                telemetry.addData("Detection", "left or nothing ");
//                telemetry.update();
//                moveForward(810,800);
//                turnLeft(88,800);
//                moveForward(80,50);
//                clawServo1.setPosition(.14);
//                clawServo2.setPosition(.14);
//                clawServo1.setPosition(OPEN_CLAW_1);
//                clawServo2.setPosition(OPEN_CLAW_2);
//


              //  moveBackward(100,1000);


            }
            telemetry.update();



        }}

