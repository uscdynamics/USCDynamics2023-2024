package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TeleOpTest extends UscOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        setUpHardware();
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

        }
    }
}
