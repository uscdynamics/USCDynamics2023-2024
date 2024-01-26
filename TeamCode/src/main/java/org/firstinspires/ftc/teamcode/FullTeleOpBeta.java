package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class FullTeleOpBeta extends UscOpMode{
    public void runOpMode(){
        setUpHardware(true, true, true, true, true, true);
        waitForStart();
        double speedX = 0.75 * SPEED_MAX;
        double strafeSpeedX = STRAFE_SPEED;
        double currentX;
        double currentY;
        double currentArm;
        double desiredArmPosition = 0;
        final double PWRSCALER = 3;
        boolean speedButtonOn = false;
        boolean intakeOn = false;

        boolean isRightTriggerPressed = false;
        boolean isLeftTriggerPressed = false;

        boolean clawOpen = true;
        boolean clawInPickPosition = true;
        clawRotation.setPosition(CLAW_ROTATION_PLACE);
        clawServo1.setPosition(OPEN_CLAW_1);
        clawServo2.setPosition(OPEN_CLAW_2);

        while (opModeIsActive()) {
            //claw chicanery

            telemetry.update();
            // Inversion
            if (this.gamepad1.dpad_up){
                drivetrainDirection(true);
                telemetry.addData("Direction: ", "Forward");
            }
            else if (this.gamepad1.dpad_down){
                drivetrainDirection(false);
                telemetry.addData("", "!!!!!!!!!!!!!!!!!!WARNING: REVERSE MODE!!!!!!!!!!!!!!!!!!!!");
                telemetry.addData("Direction: ", "Reverse");
            }
            // Drive
            currentX = scaleMovement(this.gamepad1.right_stick_x);
            currentY = scaleMovement(this.gamepad1.left_stick_y);
            double throttle = -currentY * speedX;
            // Allow second stick to turn also
            double turn = currentX * speedX;
            double leftSpeed = -1 * (throttle + turn);
            double rightSpeed = throttle - turn;
            frontLeft.setPower(leftSpeed);
            backLeft.setPower(leftSpeed);
            frontRight.setPower(rightSpeed);
            backRight.setPower(rightSpeed);
            // Strafe
            if (this.gamepad1.left_bumper) {
                frontLeft.setPower(strafeSpeedX - 0.10);
                frontRight.setPower(strafeSpeedX - 0.10);
                backLeft.setPower(-strafeSpeedX);
                backRight.setPower(-strafeSpeedX);
            }
            if (this.gamepad1.right_bumper) {
                frontLeft.setPower(-strafeSpeedX + 0.10);
                frontRight.setPower(-strafeSpeedX + 0.10);
                backLeft.setPower(strafeSpeedX);
                backRight.setPower(strafeSpeedX);
            }
            // Arm
            currentArmPosition = (armMotor2.getCurrentPosition() - armMotor1.getCurrentPosition())/2;
            currentArm = ARM_SPEED * scaleArmMovement(currentArmPosition);
            if(this.gamepad1.a && currentArmPosition < MAX_ARM_HEIGHT){
                armMotor1.setVelocity(-currentArm);
                armMotor2.setVelocity(currentArm);
                desiredArmPosition = currentArmPosition;
            }
            else if(this.gamepad1.b && currentArmPosition > MIN_ARM_HEIGHT){
                armMotor1.setVelocity(currentArm);
                armMotor2.setVelocity(-currentArm);
                desiredArmPosition = currentArmPosition;
            }
            else {
                double adjustFactor = 0;
                if(desiredArmPosition > 100 && currentArmPosition < desiredArmPosition)
                    adjustFactor = 10;
                armMotor1.setVelocity(-adjustFactor);
                armMotor2.setVelocity(adjustFactor);
            }
            telemetry.addData("Arm Position: ", currentArmPosition);

            // Airplane
            if (this.gamepad1.dpad_left){
                planeLauncher.setPosition(AIRPLANE_RELEASE_POS);
            }
            if (this.gamepad1.dpad_right){
                planeLauncher.setPosition(AIRPLANE_HOLD_POS);
            }

            // intake
            if (this.gamepad1.y) {
                intakeOn = !intakeOn;
                intake.setVelocity(intakeOn ? INTAKE_SPEED : 0);
            }

            // April Tags
            detectedObjects = processAprilTags();
            detectedObjects.trimToSize();
            for (int j = 0; j < detectedObjects.size(); j++) {
                telemetry.addData("Tag " + detectedObjects.get(j).id, detectedObjects.get(j).toString());
            }
            calculatePosition(detectedObjects);
            telemetry.addData("Average X: ", Math.round(posX));
            telemetry.addData("Average Y: ", Math.round(posY));
            // Claw Code
            // Toggle option for triggers to open/close claw
            if(this.gamepad1.left_trigger > 0 && currentArmPosition > SAFETY_CLAW_SWING_ARM_HEIGHT) {
                if(!isLeftTriggerPressed) {
                    clawInPickPosition = !clawInPickPosition;
                    if(clawInPickPosition) {
                        clawRotation.setPosition(CLAW_ROTATION_PICK);
                    }
                    else {
                        clawRotation.setPosition(CLAW_ROTATION_PLACE);
                    }
                }
                isLeftTriggerPressed = true;
            }
            else {
                isLeftTriggerPressed = false;
            }

            // Toggle option for triggers to open/close claw
            if(this.gamepad1.right_trigger > 0) {
                if(!isRightTriggerPressed) {
                    clawOpen = !clawOpen;
                    if(clawOpen) {
                        clawServo1.setPosition(OPEN_CLAW_1);
                        clawServo2.setPosition(OPEN_CLAW_2);
                    }
                    else {
                        clawServo1.setPosition(CLOSE_CLAW_1);
                        clawServo2.setPosition(CLOSE_CLAW_2);
                    }
                }
                isRightTriggerPressed = true;
            }
            else {
                isRightTriggerPressed = false;
            }
            telemetry.addData(""+(clawOpen?"YAAA":"NOooo"),"");
            telemetry.addData("claw1",""+clawServo1.getPosition());

        }
    }
}
