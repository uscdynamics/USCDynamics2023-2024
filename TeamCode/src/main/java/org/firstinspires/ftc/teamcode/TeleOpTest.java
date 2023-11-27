package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TeleOpTest extends UscOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        setUpHardware(true, false, false, false, false);
        waitForStart();
        double speedX = 0.75 * SPEED_MAX;
        double strafeSpeedX = STRAFE_SPEED;
        double currentX;
        double currentY;
        final double PWRSCALER = 3;
        boolean speedButtonOn = false;
        boolean intakeOn = false;

        boolean aPressed = false;

        while(opModeIsActive()){
            currentX = Math.pow(Math.sin((Math.PI * this.gamepad1.right_stick_x)/2),3); //Math.pow(this.gamepad1.right_stick_x, PWRSCALER);
            currentY = Math.pow(Math.sin((Math.PI * this.gamepad1.left_stick_y)/2),3);//Math.pow(this.gamepad1.left_stick_y, PWRSCALER);
            /*if (this.gamepad1.left_stick_button){
                speedButtonOn = !speedButtonOn;
            }
            if (speedButtonOn){
                speedX = SPEED_MAX;
            }
            else{
                speedX = SPEED_HALF;
            }*/
            double throttle = - currentY * speedX;
//            double turn = this.gamepad1.left_stick_x * speedMultiplier;

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
            if(this.gamepad1.a) {
                if (!aPressed) {
                    intakeOn = !intakeOn;
                    aPressed = true;
                }
            }
            else {
                aPressed = false;
            }
            if (intakeOn){
                intakeLeft.setPower(0.75 * SPEED_MAX);
                intakeRight.setPower(- 0.75 * SPEED_MAX);
            }
            if (!intakeOn){
                intakeLeft.setPower(0);
                intakeRight.setPower(0);
            }
            if(this.gamepad1.right_bumper) {
                frontLeft.setPower(-strafeSpeedX + 0.10);
                frontRight.setPower(-strafeSpeedX + 0.10);
                backLeft.setPower(strafeSpeedX);
                backRight.setPower(strafeSpeedX);
            }
        }
    }
}
