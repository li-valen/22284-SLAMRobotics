package pedroPathing.examples;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

/**
 * This is an example teleop that showcases movement and robot-centric driving.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 12/30/2024
 */

@TeleOp(name = "Example Robot-Centric Teleop", group = "Examples")
public class ValenRobotCentricTeleop extends OpMode {
    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);

    private Servo boostLeft;
    private Servo boostRight;
    private Servo liftLeft;
    private Servo liftRight;
    private Servo claw;

    /** This method is call once when init is played, it initializes the follower **/
    @Override
    public void init() {
        Constants.setConstants(FConstants.class,LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        boostLeft = hardwareMap.get(Servo.class, "boostLeft");
        boostRight = hardwareMap.get(Servo.class, "boostRight");
        liftLeft = hardwareMap.get(Servo.class, "liftLeft");
        liftRight = hardwareMap.get(Servo.class, "liftRight");
        claw = hardwareMap.get(Servo.class, "claw");
    }

    /** This method is called continuously after Init while waiting to be started. **/
    @Override
    public void init_loop() {
    }

    /** This method is called once at the start of the OpMode. **/
    @Override
    public void start() {
        follower.startTeleopDrive();
    }

    /** This is the main loop of the opmode and runs continuously after play **/
    @Override
    public void loop() {

        /* Update Pedro to move the robot based on:
        - Forward/Backward Movement: -gamepad1.left_stick_y
        - Left/Right Movement: -gamepad1.left_stick_x
        - Turn Left/Right Movement: -gamepad1.right_stick_x
        - Robot-Centric Mode: true
        */

        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();

        //raise just boosters
        if (gamepad2.a){
            boostLeft.setPosition(1);
            boostRight.setPosition(1);
        }

        //retract just boosters
        if (gamepad2.b){
            boostLeft.setPosition(0);
            boostRight.setPosition(0);
        }

        //retract just lifts
        if (gamepad2.y){
            liftLeft.setPosition(0);
            liftRight.setPosition(1);
        }

        //extend lifts, drop boosts
        if (gamepad2.x){
            liftLeft.setPosition(0.5);
            liftRight.setPosition(0.5);
            boostLeft.setPosition(0);
            boostRight.setPosition(0);
        }


        //universal reset
        if (gamepad2.dpad_down){
            boostLeft.setPosition(0);
            boostRight.setPosition(0);
            liftLeft.setPosition(0);
            claw.setPosition(1);
        }

        //grab
        if (gamepad2.right_bumper){
            claw.setPosition(0.25);
        }

        //let go
        if (gamepad2.left_bumper){
            claw.setPosition(1);
        }

        /* Telemetry Outputs of our Follower */
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));

        /* Update Telemetry to the Driver Hub */
        telemetry.update();

    }

    /** We do not use this because everything automatically should disable **/
    @Override
    public void stop() {
    }
}