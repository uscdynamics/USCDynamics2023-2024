package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TeleOpTest extends UscOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        setUpHardware(true, false, true, true, false);
        waitForStart();
        double speedX = 0.75 * SPEED_MAX;
        double strafeSpeedX = STRAFE_SPEED;
        double currentX;
        double currentY;
        final double PWRSCALER = 3;
        boolean speedButtonOn = false;
        boolean intakeOn = false;
        boolean xPressed = false;

        while(opModeIsActive()){
            telemetry.addData("servo1",""+clawServo1.getPosition());
            telemetry.addData("servo2",""+clawServo2.getPosition());
            telemetry.addData("ArmPos",""+((armMotor1.getCurrentPosition() + armMotor2.getCurrentPosition())/2));
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

            // Arm Code
            if(this.gamepad1.a && currentArmPosition < MAX_ARM_HEIGHT){
                armMotor1.setVelocity(ARM_SPEED);
                armMotor2.setVelocity(-ARM_SPEED);
                currentArmPosition = ((armMotor1.getCurrentPosition() + armMotor2.getCurrentPosition())/2);
                /* clawRotation.setPosition(servoPlacePosition)*/
            }
            else if(this.gamepad1.b && currentArmPosition > MIN_ARM_HEIGHT){
                armMotor1.setVelocity(-ARM_SPEED);
                armMotor2.setVelocity(ARM_SPEED);
                currentArmPosition = ((armMotor1.getCurrentPosition() + armMotor2.getCurrentPosition())/2);
                /* clawRotation.setPosition(servoGrabPosition)*/
            }
            else {
                armMotor1.setVelocity(0);
                armMotor2.setVelocity(0);
            }
             // Intake Code
            if(this.gamepad1.x) {
                if (!xPressed) {
                    intakeOn = !intakeOn;
                    xPressed = true;
                }
            }
            else {
                xPressed = false;
            }
            if (intakeOn){
               // intake.setPower(0.75 * SPEED_MAX);
            }
            if (!intakeOn){
                // intake.setPower(0);
            }
            // Claw Code
            if (this.gamepad1.right_trigger > 0){

            }
        }
    }
}
