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

@Autonomous(name="Robot: PlaceParkRedTopRight", group="Robot")

public class PlaceParkRedBottomRight extends UscOpMode {
    OpenCvWebcam webcam1;
    OpenCvWebcam webcam2;
    ColorMaskPipeline pipeline1 = new ColorMaskPipeline();
    ColorMaskPipeline pipeline2 = new ColorMaskPipeline();
    String place = "";
    int aaryan = 250;
    int jai = 700;
    int anthony = 250;

    @Override
    public void runOpMode() {
        waitForStart();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        int[] viewportContainerIds = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(cameraMonitorViewId, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 2"), viewportContainerIds[0]);
        webcam2 = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), viewportContainerIds[1]);

        setUpHardware(true,false,true,true,true,true);
        webcam1.setPipeline(pipeline1);
        webcam2.setPipeline(pipeline2);

        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam1.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }
        });
        webcam2.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam2.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }
        });
        while((pipeline1.getLeftBlue() > 0 && pipeline2.getLeftBlue() > 0) == false){
            telemetry.addData("waiting for cams to load","hopfully we are not fucked");
            telemetry.update();
        }
        sleep(100);


        if(pipeline1.getLeftRed() > pipeline1.getRightRed() && pipeline2.getRightRed() < pipeline1.getLeftRed()){
            telemetry.addData("pos","LEFFF");
            place = "left";
        }
        else if(pipeline2.getRightRed() > pipeline2.getLeftRed() && pipeline2.getRightRed() > pipeline1.getLeftRed()){
            telemetry.addData("pos","RIGGGGGHTHTHTHGITIRII");
            place = "right";
        }
        else{
            telemetry.addData("pos","mid");
            place = "mid";
        }



        clawServo1.setPosition(CLOSE_CLAW_1);
        clawServo2.setPosition(CLOSE_CLAW_2);
        sleep(800);
        moveArm(7+5,800);
        clawRotation.setPosition(CLAW_ROTATION_PICK-.05);
        sleep(400);
        resetMotors();
        moveBackward(700,820);
        moveArmBack(5,860);


        if(place.equals("right")){

            strafeRight(319,920);
            clawServo1.setPosition(OPEN_CLAW_1);
            clawServo2.setPosition(OPEN_CLAW_2);
            sleep(400);
            moveArm(12,900);
            sleep(200);

        }
        else if(place.equals("left")){

            strafeLeft(355 , 890);
            sleep(200);


            clawServo1.setPosition(OPEN_CLAW_1);
            clawServo2.setPosition(OPEN_CLAW_2);


            sleep(1000);
            moveArm(5,900);
            sleep(200);
            clawServo1.setPosition(CLOSE_CLAW_1);
            clawServo2.setPosition(CLOSE_CLAW_2);
            moveArm(92,900);

            strafeRight(359 , 910);
        }
        else {
            moveBackward(277, 880);
            clawServo1.setPosition(OPEN_CLAW_1);
            clawServo2.setPosition(OPEN_CLAW_2);
            sleep(400);
            moveArm(12,900);
            sleep(200);
        }


        clawServo1.setPosition(CLOSE_CLAW_1);
        clawServo2.setPosition(CLOSE_CLAW_2);

        sleep(200);
        moveForward(1600,890);
        moveBackward(120,1000);
        turnRight(77,500);
        if(place.equals("left")){
            moveBackward(598,950);


            strafeLeft(875,700);


        }
        else if(place.equals("right")){
            moveBackward(580,950);
            strafeLeft(435,870);




        }
        else {
            moveBackward(890,750);

            strafeLeft(688,700);



        }
        moveBackward(350,670);
        moveArm(791,950);
        sleep(900);
        clawRotation.setPosition(CLAW_ROTATION_PLACE);
        sleep(1000);
        clawServo1.setPosition(OPEN_CLAW_1);
        clawServo2.setPosition(OPEN_CLAW_2);
        moveBackward(50,1000);
        moveForward(100,800);
        clawRotation.setPosition(CLAW_ROTATION_PICK);
        moveArmBack(1900,850);
        clawServo1.setPosition(CLOSE_CLAW_1);
        clawServo2.setPosition(CLOSE_CLAW_2);
        strafeRight(1000,900);
        moveBackward(300,900);
        sleep(2000);





        telemetry.update();

    }}