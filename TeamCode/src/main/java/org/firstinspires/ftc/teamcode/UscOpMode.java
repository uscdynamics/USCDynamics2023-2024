package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.UNKNOWN;
import static com.sun.tools.doclint.Entity.curren;
import static com.sun.tools.doclint.Entity.nbsp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.*;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessorImpl;

import java.util.ArrayList;
import java.util.List;


public abstract class UscOpMode extends LinearOpMode {

    protected DcMotorEx frontLeft;
    protected DcMotorEx frontRight;
    protected DcMotorEx backLeft;
    protected DcMotorEx backRight;
    protected DcMotorEx intake;
    protected DcMotorEx armMotor1;
    protected DcMotorEx armMotor2;
    protected double posX;
    protected double posY;
    protected int currentArmPosition;
    private AprilTagProcessor aprilTag;
    protected ArrayList<DetectionStorage> detectedObjects;

    protected static WebcamName camera1;
    protected static WebcamName camera2;
    protected static WebcamName camera3;
    protected static VisionPortal visionPortal;

    protected final DetectionStorage TAG_1 = new DetectionStorage(31.0, 12.0, "Tag: 1");
    protected final DetectionStorage TAG_2 = new DetectionStorage(36.0, 12.0, "Tag: 2");
    protected final DetectionStorage TAG_3 = new DetectionStorage(41.0, 12.0, "Tag: 3");
    protected final DetectionStorage TAG_4 = new DetectionStorage(102.0, 12.0, "Tag: 4");
    protected final DetectionStorage TAG_5 = new DetectionStorage(108.0, 12.0, "Tag: 5");
    protected final DetectionStorage TAG_6 = new DetectionStorage(114.0, 12.0, "Tag: 6");
    protected final DetectionStorage TAG_7 = new DetectionStorage(42.0, 144.0, "Tag: 7");
    protected final DetectionStorage TAG_8 = new DetectionStorage(36.0, 144.0, "Tag: 8");
    protected final DetectionStorage TAG_9 = new DetectionStorage(108.0, 144.0, "Tag: 9");
    protected final DetectionStorage TAG_10 = new DetectionStorage(102.0, 144.0, "Tag: 10");
    protected final DetectionStorage[] TAG_LIST = {TAG_1, TAG_2, TAG_3, TAG_4, TAG_5, TAG_6, TAG_7, TAG_8, TAG_9, TAG_10};

    protected Servo clawServo1;
    protected Servo clawServo2;
    protected Servo clawRotation;
    protected boolean aprilTagCam = false;

    protected final double WHEEL_DIAMETER = 96.0;
    protected final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
    protected final double TICKS_PER_REVOLUTION = 538;
    protected final double SPEED_MAX = 1.0;
    protected final double STRAFE_SPEED = 0.75;
    protected final double SPEED_HALF = 0.5;
    protected final int ARM_SPEED = 200;
    protected final int MAX_ARM_HEIGHT = 4250;
    protected final int MIN_ARM_HEIGHT = 5;
    protected /*final*/ float servoPlacePosition;
    protected /*final*/ float servoGrabPosition;
    final double CLOSE_CLAW_1 = 0.39+ .03;
    final double CLOSE_CLAW_2 = 0.23-.01;
    final double OPEN_CLAW_1 = 0.0;
    final double OPEN_CLAW_2 = 0.3;


    public void setUpHardware(boolean drivetrain, boolean cameras, boolean arm, boolean claw, boolean intake){
        if (drivetrain){
            setUpDrivetrain();
        }
        if(cameras){
            setUpCameras();
        }
        if(arm){
            setUpArm();
        }
        if(claw){
            setUpClaw();
        }
        if(intake){
            setUpIntake();
        }
    }
    public void setUpDrivetrain() {
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft"); // Motor 0
        backRight = hardwareMap.get(DcMotorEx.class, "backRight"); // Motor 1
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft"); // Motor 2
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight"); // Motor 3
    }
    public void setUpCameras(){
        aprilTagCam = true;
        initAprilTag();
    }
    public void setUpArm(){
        armMotor1 = hardwareMap.get(DcMotorEx.class, "arm1");
        armMotor2 = hardwareMap.get(DcMotorEx.class, "arm2");
        armMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        currentArmPosition = ((armMotor1.getCurrentPosition() + armMotor2.getCurrentPosition())/2);
        armMotor1.setZeroPowerBehavior(BRAKE);
        armMotor2.setZeroPowerBehavior(BRAKE);
    }
    public void setUpClaw(){
        clawServo1 = hardwareMap.get(Servo.class, "clawServo1");
        clawServo2 = hardwareMap.get(Servo.class, "clawServo2");
        clawRotation = hardwareMap.get(Servo.class, "clawRotation");

    }
    public void setUpIntake(){
        intake = hardwareMap.get(DcMotorEx.class, "intake");
    }
    public void runOpMode() throws InterruptedException {
    }

    @Override
    public void waitForStart() {
            super.waitForStart();
        }

    protected void resetMotors() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    protected void setRunToPosition() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    protected void setVelocity(double velocity) {
        frontLeft.setVelocity(velocity);
        backLeft.setVelocity(velocity);
        frontRight.setVelocity(velocity);
        backRight.setVelocity(velocity);
    }

    protected void setTargetPosition(int numberOfTicks) {
        frontLeft.setTargetPosition(numberOfTicks);
        frontRight.setTargetPosition(numberOfTicks);
        backLeft.setTargetPosition(numberOfTicks);
        backRight.setTargetPosition(numberOfTicks);
    }

    protected void motorsForward() {
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    protected void motorsBackward() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    protected void motorsStrafeLeft() {
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD); //+
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE); //-
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD); //+
        backRight.setDirection(DcMotorSimple.Direction.REVERSE); //-
    }

    protected void motorsStrafeRight() {
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD); //-
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE); //+
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD); //-
        backRight.setDirection(DcMotorSimple.Direction.REVERSE); //+
    }

    protected void motorsLeft() {
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    protected void initAprilTag() {
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        if (aprilTagCam) {
            visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTag);
        }
        else {
            visionPortal = VisionPortal.easyCreateWithDefaults(BuiltinCameraDirection.BACK, aprilTag);
        }
    }

    protected ArrayList<DetectionStorage> processAprilTags() {
        List<AprilTagDetection> detections =  aprilTag.getDetections();
        ArrayList<DetectionStorage> out = new ArrayList<DetectionStorage>();
        telemetry.addData("# AprilTags Detected", detections.size());
        for (AprilTagDetection detection : detections) {
            if (detection.metadata != null) {
                out.add(new DetectionStorage(detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z,
                        detection.id, detection.metadata.name));
            }
        }
        return out;
    }

    protected void calculatePosition(ArrayList<DetectionStorage> in ){
        int iters = 0;
        double xOut = 0;
        double yOut = 0;
        for (int i = 0; i < in.size(); i++){
            if (in.get(i).id >= 7){
                xOut += -in.get(i).x + (TAG_LIST[in.get(i).id - 1]).x;
                yOut += -in.get(i).y + (TAG_LIST[in.get(i).id - 1]).y;
                iters++;
            }
            else {
                xOut += -in.get(i).x + (TAG_LIST[in.get(i).id - 1]).x;
                yOut += in.get(i).y + (TAG_LIST[in.get(i).id - 1]).y;
                iters++;
            }
        }
        posX = xOut/iters;
        posY = yOut/iters;
    }

    protected void motorsRight() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    protected void movementPowerDisable() {
        frontLeft.setMotorDisable();
        frontRight.setMotorDisable();
        backLeft.setMotorDisable();
        backRight.setMotorDisable();
    }

    protected void movementPowerEnable() {
        frontLeft.setMotorEnable();
        frontRight.setMotorEnable();
        backLeft.setMotorEnable();
        backRight.setMotorEnable();
    }

    protected void turnLeft(double degrees, double velocity) {
        double numberOfTicks = degrees * ((TICKS_PER_REVOLUTION * 2) / 90);
        resetMotors();
        motorsLeft();
        setTargetPosition((int) numberOfTicks);
        setRunToPosition();
        setVelocity(velocity);
        while (frontLeft.isBusy()) {
            telemetry.addData("velocity", frontLeft.getVelocity());
            telemetry.addData("position", frontLeft.getCurrentPosition());
            telemetry.addData("is at target", !frontLeft.isBusy());
            telemetry.update();
        }
    }

    protected void turnRight(double degrees, double velocity) {
        double numberOfTicks = degrees * ((TICKS_PER_REVOLUTION * 2) / 90);
        resetMotors();
        motorsRight();
        setTargetPosition((int) numberOfTicks);
        setRunToPosition();
        setVelocity(velocity);
        while (frontLeft.isBusy()) {
            telemetry.addData("velocity", frontLeft.getVelocity());
            telemetry.addData("position", frontLeft.getCurrentPosition());
            telemetry.addData("is at target", !frontLeft.isBusy());
            telemetry.update();
        }
    }

    protected void moveForward(double distanceMm, double velocity) {
        double numberOfTicks = (distanceMm / WHEEL_CIRCUMFERENCE) * TICKS_PER_REVOLUTION;
        resetMotors();
        motorsForward();
        setTargetPosition((int) numberOfTicks);
        setRunToPosition();
        setVelocity(velocity);
        while (frontLeft.isBusy()) {
            telemetry.addData("velocity", frontLeft.getVelocity());
            telemetry.addData("position", frontLeft.getCurrentPosition());
            telemetry.addData("is at target", !frontLeft.isBusy());
            telemetry.update();
        }
    }

    protected void moveBackward(double distanceMm, double velocity) {
        double numberOfTicks = (distanceMm / WHEEL_CIRCUMFERENCE) * TICKS_PER_REVOLUTION;
        resetMotors();
        motorsBackward();
        setTargetPosition((int) numberOfTicks);
        setRunToPosition();
        setVelocity(velocity);
        while (frontLeft.isBusy()) {
            telemetry.addData("velocity", frontLeft.getVelocity());
            telemetry.addData("position", frontLeft.getCurrentPosition());
            telemetry.addData("is at target", !frontLeft.isBusy());
            telemetry.update();
        }
    }

    protected void strafeLeft(double distanceMm, double velocity) {
        double numberOfTicks = (distanceMm / WHEEL_CIRCUMFERENCE) * TICKS_PER_REVOLUTION;
        resetMotors();
        motorsStrafeLeft();
        setTargetPosition((int) numberOfTicks);
        setRunToPosition();
        setVelocity(velocity);
        if (frontLeft.getCurrentPosition() >= numberOfTicks) {
            setVelocity(0.0);
        }
        while (frontLeft.isBusy()) {
            telemetry.addData("velocity", frontLeft.getVelocity());
            telemetry.addData("position", frontLeft.getCurrentPosition());
            telemetry.addData("is at target", !frontLeft.isBusy());
            telemetry.update();
        }
    }

    protected void strafeRight(double distanceMm, double velocity) {
        double numberOfTicks = (distanceMm / WHEEL_CIRCUMFERENCE) * TICKS_PER_REVOLUTION;
        resetMotors();
        motorsStrafeRight();
        setTargetPosition((int) numberOfTicks);
        setRunToPosition();
        setVelocity(velocity);
        if (frontLeft.getCurrentPosition() >= numberOfTicks) {
            setVelocity(0.0);
        }
        while (frontLeft.isBusy()) {
            telemetry.addData("velocity", frontLeft.getVelocity());
            telemetry.addData("position", frontLeft.getCurrentPosition());
            telemetry.addData("is at target", !frontLeft.isBusy());
            telemetry.update();
        }
    }

}
