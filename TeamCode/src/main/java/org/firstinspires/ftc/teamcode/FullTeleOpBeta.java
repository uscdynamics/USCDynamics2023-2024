package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class FullTeleOpBeta extends UscOpMode{

    boolean isKindaEqualTo(double a, double b) {
        double uncertainty = 0.1;
        telemetry.addData("isKindaEqualTo: ", "" + Math.abs(a) + "-" + Math.abs(b) + "<" + uncertainty + "=" + ((Math.abs(a) - Math.abs(b)) < uncertainty));
        return (Math.abs((Math.abs(a) - Math.abs(b))) < uncertainty);
    }

    public void runOpMode(){

        setUpHardware(true, true, true, true, true, true);
        setUpLights();
        waitForStart();
        setFrontLights(true, false);
        setBackLights(false, true);
        double speedX = 0.75 * SPEED_MAX;
        double strafeSpeedX = STRAFE_SPEED;
        double currentX;
        double currentY;
        double currentArm;

        Integer desiredArmPosition1 = null;
        Integer desiredArmPosition2 = null;

        final double PWRSCALER = 3;
        boolean speedButtonOn = false;
        boolean intakeOn = false;
        boolean rotationPick = true;
        boolean isLeftStickPressed = false;
        boolean isHeadingToPick = false;

        boolean isRightTriggerPressed = false;
        boolean isLeftTriggerPressed = false;
        boolean isAPressed = false;
        boolean isBPressed = false;
        boolean isYPressed = false;
        boolean isBackPressed = false;

        boolean clawOpen = true;
        boolean clawInPickPosition = true;
        clawRotation.setPosition(CLAW_ROTATION_PICK);
        clawServo1.setPosition(OPEN_CLAW_1);
        clawServo2.setPosition(OPEN_CLAW_2);

        while (opModeIsActive()) {
            //claw chicanery

            // Inversion
            if (this.gamepad1.dpad_up){
                drivetrainDirection(true);
                telemetry.addData("Direction: ", "Forward");
                setFrontLights(true, false);
                setBackLights(false, true);
            }
            else if (this.gamepad1.dpad_down){
                drivetrainDirection(false);
                telemetry.addData( "", "!!!!!!!!!!!!!!!!!!WARNING: REVERSE MODE!!!!!!!!!!!!!!!!!!!!");
                telemetry.addData("Direction: ", "Reverse");
                setFrontLights(false, true);
                setBackLights(true, false);
            }
            if (this.gamepad1.left_stick_button) {
                if (!isLeftStickPressed) {
                    drivetrainDirection(!direction);
                    if (direction){
                        telemetry.addData("Direction: ", "Forward");
                    }
                    if (!direction){
                        telemetry.addData("Direction: ", "Reverse");
                    }
                    setFrontLights(direction, !direction);
                    setBackLights(!direction, direction);
                }
                isLeftStickPressed = true;
            }
            else {
                isLeftStickPressed = false;
            }
            if (direction){
                telemetry.addData("Direction: ", "Forward");
            }
            else {
                telemetry.addData("Direction: ", "Reverse");
            }
            // Drive
            telemetry.addData("Turn: ", this.gamepad1.right_stick_x);
            telemetry.addData("Throttle: ", this.gamepad1.left_stick_y);
            currentX = this.gamepad1.right_stick_x;
            currentY = this.gamepad1.left_stick_y;
            double throttle = -currentY * speedX;
            // Allow second stick to turn also
            double turn = currentX * speedX / 2.0;
            double leftSpeed = -1 * (throttle + turn);
            double rightSpeed = throttle - turn;
            frontLeft.setPower(leftSpeed);
            backLeft.setPower(leftSpeed);
            frontRight.setPower(rightSpeed);
            backRight.setPower(rightSpeed);
            // Strafe
            if (this.gamepad1.left_bumper) {
                frontLeft.setPower(strafeSpeedX );
                frontRight.setPower(strafeSpeedX );
                backLeft.setPower(-strafeSpeedX);
                backRight.setPower(-strafeSpeedX);
            }
            if (this.gamepad1.right_bumper) {
                frontLeft.setPower(-strafeSpeedX );
                frontRight.setPower(-strafeSpeedX );
                backLeft.setPower(strafeSpeedX);
                backRight.setPower(strafeSpeedX);
            }

            // Arm
            currentArmPosition = (Math.abs(armMotor2.getCurrentPosition()) + Math.abs(armMotor1.getCurrentPosition())) / 2;
            currentArm = ARM_SPEED;
            double armRange = MAX_ARM_HEIGHT - MIN_ARM_HEIGHT;
            double armPercent = (currentArmPosition - MIN_ARM_HEIGHT) / armRange;
            double midPoint = (.5d * armRange) + MIN_ARM_HEIGHT;

            double minSpeed = 0.3d * ARM_SPEED;

            double adjustedSpeed = ARM_SPEED;
            if (currentArmPosition <= MIN_ARM_HEIGHT) {
                adjustedSpeed = 0d;
            } else if (currentArmPosition >= MAX_ARM_HEIGHT) {
                adjustedSpeed = 0d;
            } else if (currentArmPosition < midPoint) {
                // Speed decreases as the current position gets closer to the minimum position
                adjustedSpeed = minSpeed + ((currentArmPosition - MIN_ARM_HEIGHT) * (ARM_SPEED - minSpeed)) / (midPoint - MIN_ARM_HEIGHT);
            } else {
                // Speed decreases as the current position gets closer to the maximum position
                adjustedSpeed = minSpeed + ((MAX_ARM_HEIGHT - currentArmPosition) * (ARM_SPEED - minSpeed)) / (MAX_ARM_HEIGHT - midPoint);
            }
            currentArm = adjustedSpeed;

            // Any time the arm is above threshold, move rotation to PLACE
            if (armPercent > 0.60d && !isKindaEqualTo(clawRotation.getPosition(), CLAW_ROTATION_PLACE)) {
                clawRotation.setPosition(CLAW_ROTATION_PLACE);
            }
            // Any time the arm is below threshold, move rotation to PICK
            if (armPercent < 0.55d && !isKindaEqualTo(clawRotation.getPosition(), CLAW_ROTATION_PICK)) {
                clawRotation.setPosition(CLAW_ROTATION_PICK);
            }

            if(this.gamepad1.b) {
                if (!isBPressed) {
                    isHeadingToPick = true;

                    // Make sure the claw opens on the way down
                    if (!clawOpen) {
                        clawOpen = true;
                        clawServo1.setPosition(OPEN_CLAW_1);
                        clawServo2.setPosition(OPEN_CLAW_2);
                    }
                }
                isBPressed = true;
            }
            else {
                isBPressed = false;
            }

            if(this.gamepad1.a && currentArmPosition < MAX_ARM_HEIGHT) {
                currentArm = Math.max(currentArm, minSpeed); // Ensure we can move up - no get stuck
                armMotor1.setVelocity(currentArm);
                armMotor2.setVelocity(-currentArm);

                desiredArmPosition1 = (Integer)armMotor1.getCurrentPosition();
                desiredArmPosition2 = (Integer)armMotor2.getCurrentPosition();

                // On initial press, make sure claw closed
                if (!isAPressed) {
                    isHeadingToPick = false;

                    // Make sure the claw opens on the way down
                    if (clawOpen) {
                        clawOpen = false;
                        clawServo1.setPosition(CLOSE_CLAW_1);
                        clawServo2.setPosition(CLOSE_CLAW_2);
                    }
                }
                isAPressed = true;
            }
            else {
                isAPressed = false;

                if (isHeadingToPick && currentArmPosition > MIN_ARM_HEIGHT){
                    desiredArmPosition1 = null;
                    desiredArmPosition2 = null;

                    currentArm = Math.max(currentArm, minSpeed); // Ensure we can move up - no get stuck
                    armMotor1.setVelocity(-currentArm);
                    armMotor2.setVelocity(currentArm);
                }
                else {
                    if (isHeadingToPick)
                        isHeadingToPick = false;
                    else {
                        // Adjust if needed
                        if (desiredArmPosition1 != null && armMotor1.getCurrentPosition() < desiredArmPosition1.intValue()) {
                            telemetry.addData("Adjust arm1: ", desiredArmPosition1.intValue() - armMotor1.getCurrentPosition());
                            armMotor1.setVelocity(ARM_SPEED/20.0);
                        }
                        else {
                            armMotor1.setVelocity(0);
                        }
                        if (desiredArmPosition2 != null && armMotor2.getCurrentPosition() > desiredArmPosition2.intValue()) {
                            telemetry.addData("Adjust arm2: ", desiredArmPosition2.intValue() - armMotor2.getCurrentPosition());
                            armMotor2.setVelocity(-ARM_SPEED/20.0);
                        }
                        else {
                            armMotor2.setVelocity(0);
                        }
                    }
                }
            }

            telemetry.addData("clawRotation: ", clawRotation.getPosition());
            telemetry.addData("CLAW_ROTATION_PLACE/CLAW_ROTATION_PICK: ", CLAW_ROTATION_PLACE  + "/" + CLAW_ROTATION_PICK);

            telemetry.addData("Arm Position 1: ", armMotor1.getCurrentPosition());
            telemetry.addData("Arm Position 2: ", armMotor2.getCurrentPosition());
            telemetry.addData("Arm Position X: ", currentArmPosition);
            telemetry.addData("armPercentage: ", armPercent);

            // Airplane
            if (this.gamepad1.dpad_right){
                planeLauncher.setPosition(AIRPLANE_HOLD_POS);
            }
            if (this.gamepad1.dpad_left && this.gamepad1.start){
                planeLauncher.setPosition(AIRPLANE_RELEASE_POS);
            }

            // intake
            if (this.gamepad1.y) {
                if (!isYPressed) {
                    intakeOn = !intakeOn;
                    intake.setPower(intakeOn ? INTAKE_SPEED : 0.0d);
                }
                isYPressed = true;
            }
            else {
                isYPressed = false;
            }

            // rotation
            if (this.gamepad1.back) {
                if (!isBackPressed) {
                    rotationPick = !rotationPick;
                    clawRotation.setPosition(rotationPick ? CLAW_ROTATION_PICK : CLAW_ROTATION_PLACE);
                }
                isBackPressed = true;
            }
            else {
                isBackPressed = false;
            }

            // April Tags
//            detectedObjects = processAprilTags();
//            detectedObjects.trimToSize();
//            for (int j = 0; j < detectedObjects.size(); j++) {
//                telemetry.addData("Tag " + detectedObjects.get(j).id, detectedObjects.get(j).toString());
//            }
//            calculatePosition(detectedObjects);
            telemetry.addData("Average X: ", Math.round(posX));
            telemetry.addData("Average Y: ", Math.round(posY));

            // Claw Code
            // Toggle option for triggers to open/close claw
//            if(this.gamepad1.left_trigger > 0 && currentArmPosition > SAFETY_CLAW_SWING_ARM_HEIGHT) {
//                if(!isLeftTriggerPressed) {
//                    clawInPickPosition = !clawInPickPosition;
//                    if(clawInPickPosition) {
//                        clawRotation.setPosition(CLAW_ROTATION_PICK);
//                    }
//                    else {
//                        clawRotation.setPosition(CLAW_ROTATION_PLACE);
//                    }
//                }
//                isLeftTriggerPressed = true;
//            }
//            else {
//                isLeftTriggerPressed = false;
//            }

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
            telemetry.addData("desiredArmPosition1",""+desiredArmPosition1);
            telemetry.addData("desiredArmPosition2",""+desiredArmPosition2);
            telemetry.update();
        }
    }
}
