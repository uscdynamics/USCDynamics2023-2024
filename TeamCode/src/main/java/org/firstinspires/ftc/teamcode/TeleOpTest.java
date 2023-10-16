package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TeleOpTest extends UscOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        setUpHardware(true, false, false, true);
        waitForStart();
        double speedX = SPEED_HALF;
        double strafeSpeedX = STRAFE_SPEED;
        boolean speedButtonIsPressed = false;
        while(opModeIsActive()){

            if (this.gamepad1.left_stick_button){
                if (speedX == SPEED_HALF){
                    speedX = SPEED_MAX;
                    strafeSpeedX = SPEED_MAX;
                }
                else{
                    speedX = SPEED_HALF;
                    strafeSpeedX = STRAFE_SPEED;
                }
            }
            double throttle = -this.gamepad1.left_stick_y * speedX;
//            double turn = this.gamepad1.left_stick_x * speedMultiplier;

            // Allow second stick to turn also
            double turn = this.gamepad1.right_stick_x * speedX;
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
                intakeLeft.setPower(SPEED_MAX);
                intakeRight.setPower(-1*SPEED_MAX);
            }
            else{
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
