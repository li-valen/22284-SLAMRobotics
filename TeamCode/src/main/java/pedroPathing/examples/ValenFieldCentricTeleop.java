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
 * This is an example teleop that showcases movement and field-centric driving.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 12/30/2024
 */

@TeleOp(name = "Example Field-Centric Teleop", group = "Examples")
public class ValenFieldCentricTeleop extends OpMode {
    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);
    private Servo upLeft0;
    private Servo upRight1;
    private Servo clawSpin2;
    private Servo clawRight3;
    private Servo clawLeft4;

    /** This method is call once when init is played, it initializes the follower **/
    @Override
    public void init() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        upLeft0 = hardwareMap.get(Servo.class, "upLeft");
        upRight1 = hardwareMap.get(Servo.class, "upRight");
        clawSpin2 = hardwareMap.get(Servo.class, "clawSpin");
        clawRight3 = hardwareMap.get(Servo.class, "clawRight");
        clawLeft4 = hardwareMap.get(Servo.class, "clawLeft");
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
        - Robot-Centric Mode: false
        */

        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, false);
        follower.update();

        //raise just boosters
        if (gamepad2.a){
            upLeft0.setPosition(1);
        }

        //retract just boosters
        if (gamepad2.dpad_down){
            upLeft0.setPosition(0.5);
        }

        //spin
        if (gamepad2.y){
            clawSpin2.setPosition(1);
        }


        //extend lifts, drop boosts
        if (gamepad2.dpad_up){
            clawSpin2.setPosition(0);
        }


        //universal reset
        if (gamepad2.x){
            clawLeft4.setPosition(1);
        }

        //grab
        if (gamepad2.y){
            clawRight3.setPosition(0.25);
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