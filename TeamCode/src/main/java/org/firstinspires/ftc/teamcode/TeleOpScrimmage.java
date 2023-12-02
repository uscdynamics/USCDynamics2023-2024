package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class TeleOpScrimmage extends UscOpMode {

    final double SAFETY_ARM_HEIGHT_MAX = 2400;
    final double SAFETY_CLAW_SWING_ARM_HEIGHT = 900;
    final double CLAW_ROTATION_PLACE = 0.9;
    final double CLAW_ROTATION_PICK = 0.28;

    final double CLOSE_CLAW_1 = 0.39;
    final double CLOSE_CLAW_2 = 0.23;
    final double OPEN_CLAW_1 = 0.0;
    final double OPEN_CLAW_2 = 0.3;
    final double ARM_POWER = 1.0;

    public double smartDownPower() {
        return currentArmPosition > 1100 ? ARM_POWER : (currentArmPosition > 500 ? ARM_POWER / 2 : ARM_POWER / 3);
    }

    public void moveArm(boolean isUp, double power) {
        armMotor1.setDirection(isUp ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        armMotor2.setDirection(isUp ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        armMotor1.setPower(power);
        armMotor2.setPower(power);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        setUpHardware(true, false, true, true, true);

        // TODO: Move this to setUpHardware
        armMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        double speedX = 0.75 * SPEED_MAX;
        double strafeSpeedX = STRAFE_SPEED;
        double currentX;
        double currentY;
        boolean intakeOn = false;
        boolean xPressed = false;

        boolean isRightTriggerPressed = false;
        boolean isLeftTriggerPressed = false;

        boolean clawOpen = true;
        boolean clawInPickPosition = true;

        boolean unburyingArm = false;

        // Move to start positions
        clawRotation.setPosition(CLAW_ROTATION_PICK);
        clawServo1.setPosition(OPEN_CLAW_1);
        clawServo2.setPosition(OPEN_CLAW_2);

        while(opModeIsActive()){
            telemetry.addData("servo1",""+clawServo1.getPosition());
            telemetry.addData("servo2",""+clawServo2.getPosition());
            telemetry.addData("AvgArm",""+currentArmPosition);
            telemetry.update();

            currentX = Math.pow(Math.sin((Math.PI * this.gamepad1.right_stick_x)/2),3);
            currentY = Math.pow(Math.sin((Math.PI * this.gamepad1.left_stick_y)/2),3);

            double throttle = - currentY * speedX;

            // Allow second stick to turn also
            double turn = currentX * speedX;
            double leftSpeed = -1 * (throttle + turn);
            double rightSpeed = throttle - turn;
            frontLeft.setPower(leftSpeed);
            backLeft.setPower(leftSpeed);
            frontRight.setPower(rightSpeed);
            backRight.setPower(rightSpeed);

            // Strafe
            if(this.gamepad1.left_bumper) {
                frontLeft.setPower(strafeSpeedX - 0.10);
                frontRight.setPower(strafeSpeedX - 0.10);
                backLeft.setPower(-strafeSpeedX);
                backRight.setPower(-strafeSpeedX);
            }
            if(this.gamepad1.right_bumper) {
                frontLeft.setPower(-strafeSpeedX + 0.10);
                frontRight.setPower(-strafeSpeedX + 0.10);
                backLeft.setPower(strafeSpeedX);
                backRight.setPower(strafeSpeedX);
            }

            //
            // Determine arm position
            //
            currentArmPosition = (armMotor1.getCurrentPosition() + armMotor2.getCurrentPosition()) / 2 * (armMotor1.getDirection() == DcMotorSimple.Direction.FORWARD ? 1 : -1);


            if (this.gamepad1.a && currentArmPosition < SAFETY_ARM_HEIGHT_MAX) {
                moveArm(true, ARM_POWER);

                // Auto rotate the claw
                if (currentArmPosition > 1000 && clawInPickPosition) {
                    clawInPickPosition = false;
                    clawRotation.setPosition(CLAW_ROTATION_PLACE);
                }
            }
            else if (this.gamepad1.b && currentArmPosition > 0) {
                moveArm(false, smartDownPower());

                // Auto rotate the claw
                if (currentArmPosition < 2000 && !clawInPickPosition) {
                    clawInPickPosition = true;
                    clawRotation.setPosition(CLAW_ROTATION_PICK);
                }
            }
            else {
                armMotor1.setPower(0);
                armMotor2.setPower(0);
            }

            // Just in case the arm blasted too low
            if (currentArmPosition < -5) {
                unburyingArm = true;
                moveArm(true, 0.2);
            }
            else if (unburyingArm){
                unburyingArm = false;
                armMotor1.setPower(0);
                armMotor2.setPower(0);
            }

            // Intake Code
            if(this.gamepad1.x) {
                if (!xPressed) {
                    intakeOn = !intakeOn;
                    intake.setPower(intakeOn ? 1.0 : 0.0);
                    xPressed = true;
                }
            }
            else {
                xPressed = false;
            }

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
        }
    }
}
