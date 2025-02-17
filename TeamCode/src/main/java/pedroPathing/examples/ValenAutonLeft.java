package pedroPathing.examples;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

/**
 * This is an example auto that showcases movement and control of two servos autonomously.
 * It is a 0+4 (Specimen + Sample) bucket auto. It scores a neutral preload and then pickups 3 samples from the ground and scores them before parking.
 * There are examples of different ways to build paths.
 * A path progression method has been created and can advance based on time, position, or other factors.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 11/28/2024
 */

@Autonomous(name = "GOOD-LEFT")
public class ValenAutonLeft extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;

    /** This is the variable where we store the state of our auto.
     * It is used by the pathUpdate method. */
    private int pathState;

    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */

    /** Start Pose of our robot */
    private final Pose startPose = new Pose(9.796, 87.379, Math.toRadians(0));

    /** Scoring Pose of our robot. It is facing the submersible at a -45 degree (315 degree) angle. */
    private final Pose secondPose = new Pose(34.873, 108.147, Math.toRadians(0));

    private final Pose thirdPose = new Pose(60.147, 108.147, Math.toRadians(0));

    private final Pose fourthPose = new Pose(60.147, 94.237, Math.toRadians(90));

    private PathChain doshit;
    private DcMotor arm;

    private Path line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12;

    /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts. **/
    public void buildPaths() {

        /* There are two major types of paths components: BezierCurves and BezierLines.
         *    * BezierCurves are curved, and require >= 3 points. There are the start and end points, and the control points.
         *    - Control points manipulate the curve between the start and end points.
         *    - A good visualizer for this is [this](https://pedro-path-generator.vercel.app/).
         *    * BezierLines are straight, and require 2 points. There are the start and end points.
         * Paths have can have heading interpolation: Constant, Linear, or Tangential
         *    * Linear heading interpolation:
         *    - Pedro will slowly change the heading of the robot from the startHeading to the endHeading over the course of the entire path.
         *    * Constant Heading Interpolation:
         *    - Pedro will maintain one heading throughout the entire path.
         *    * Tangential Heading Interpolation:
         *    - Pedro will follows the angle of the path such that the robot is always driving forward when it follows the path.
         * PathChains hold Path(s) within it and are able to hold their end point, meaning that they will holdPoint until another path is followed.
         * Here is a explanation of the difference between Paths and PathChains <https://pedropathing.com/commonissues/pathtopathchain.html> */

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        line1 = new Path( new BezierLine(
                new Point(9.796, 87.379, Point.CARTESIAN),
                new Point(34.873, 108.147, Point.CARTESIAN)
        ));
        line1.setConstantHeadingInterpolation(Math.toRadians(0));

        line2 = new Path( new BezierLine(
                new Point(34.873, 108.147, Point.CARTESIAN),
                new Point(60.147, 108.147, Point.CARTESIAN)
        )
        );
        line2.setConstantHeadingInterpolation(Math.toRadians(0));

        line3 = new Path( new BezierLine(
                new Point(60.147, 108.147, Point.CARTESIAN),
                new Point(60.343, 118.335, Point.CARTESIAN)
        ));
        line3.setConstantHeadingInterpolation(Math.toRadians(0));

        line4 = new Path( new BezierLine(
                new Point(60.343, 118.335, Point.CARTESIAN),
                new Point(11.167, 122.841, Point.CARTESIAN)
        ));
        line4.setConstantHeadingInterpolation(Math.toRadians(0));

        line5 = new Path( new BezierLine(
                new Point(11.167, 122.841, Point.CARTESIAN),
                new Point(60.147, 118.531, Point.CARTESIAN)
        ));
        line5.setConstantHeadingInterpolation(Math.toRadians(0));

        line6 = new Path( new BezierLine(
                new Point(60.147, 118.531, Point.CARTESIAN),
                new Point(60.147, 129.306, Point.CARTESIAN)
        ));
        line6.setConstantHeadingInterpolation(Math.toRadians(0));

        line7 = new Path( new BezierLine(
                new Point(60.147, 129.306, Point.CARTESIAN),
                new Point(11.559, 128.914, Point.CARTESIAN)
        ));
        line7.setConstantHeadingInterpolation(Math.toRadians(0));

        line8 = new Path( new BezierLine(
                new Point(11.559, 128.914, Point.CARTESIAN),
                new Point(60.147, 129.502, Point.CARTESIAN)
        ));
        line8.setConstantHeadingInterpolation(Math.toRadians(0));

        line9 = new Path( new BezierLine(
                new Point(60.147, 129.502, Point.CARTESIAN),
                new Point(59.755, 136, Point.CARTESIAN)
        ));
        line9.setConstantHeadingInterpolation(Math.toRadians(0));

        line10 = new Path( new BezierLine(
                new Point(59.755, 136, Point.CARTESIAN),
                new Point(15.869, 134.988, Point.CARTESIAN)
        ));
        line10.setConstantHeadingInterpolation(Math.toRadians(0));

        line11 = new Path( new BezierLine(
                new Point(15.869, 134.988, Point.CARTESIAN),
                new Point(60.147, 108.147, Point.CARTESIAN)
        ));
        line11.setConstantHeadingInterpolation(Math.toRadians(0));

        line12 = new Path( new BezierLine(
                new Point(60.147, 108.147, Point.CARTESIAN),
                new Point(59.755, 100, Point.CARTESIAN)
        ));
        line12.setConstantHeadingInterpolation(Math.toRadians(90));

        doshit = follower.pathBuilder()
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(7.249, 84.441, Point.CARTESIAN),
                                new Point(34.873, 108.147, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 2
                        new BezierLine(
                                new Point(34.873, 108.147, Point.CARTESIAN),
                                new Point(60.147, 108.147, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 3
                        new BezierLine(
                                new Point(60.147, 108.147, Point.CARTESIAN),
                                new Point(60.343, 118.335, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 4
                        new BezierLine(
                                new Point(60.343, 118.335, Point.CARTESIAN),
                                new Point(8.620, 118.335, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 5
                        new BezierLine(
                                new Point(8.620, 118.335, Point.CARTESIAN),
                                new Point(60.147, 118.531, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 6
                        new BezierLine(
                                new Point(60.147, 118.531, Point.CARTESIAN),
                                new Point(60.147, 129.306, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 7
                        new BezierLine(
                                new Point(60.147, 129.306, Point.CARTESIAN),
                                new Point(11.559, 128.914, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 8
                        new BezierLine(
                                new Point(11.559, 128.914, Point.CARTESIAN),
                                new Point(60.147, 129.502, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 9
                        new BezierLine(
                                new Point(60.147, 129.502, Point.CARTESIAN),
                                new Point(59.755, 134.988, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 10
                        new BezierLine(
                                new Point(59.755, 134.988, Point.CARTESIAN),
                                new Point(15.869, 134.988, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 11
                        new BezierLine(
                                new Point(15.869, 134.988, Point.CARTESIAN),
                                new Point(60.147, 108.147, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 12
                        new BezierLine(
                                new Point(60.147, 108.147, Point.CARTESIAN),
                                new Point(59.755, 94.433, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .build();
    }

    /** This switch is called continuously and runs the pathing, at certain points, it triggers the action state.
     * Everytime the switch changes case, it will reset the timer. (This is because of the setPathState() method)
     * The followPath() function sets the follower to run the specific path, but does NOT wait for it to finish before moving on. */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start following the path
                follower.followPath(line1);
                setPathState(1);
                break;

            case 1: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line2);
                    setPathState(2); // Move to arm movement state
                }
                break;
            case 2: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line3);
                    setPathState(3); // Move to arm movement state
                }
                break;
            case 3: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line4);
                    setPathState(4); // Move to arm movement state
                }
                break;
            case 4: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line5);
                    setPathState(5); // Move to arm movement state
                }
                break;
            case 5: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line6);
                    setPathState(6); // Move to arm movement state
                }
                break;
            case 6: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line7);
                    setPathState(7); // Move to arm movement state
                }
                break;
            case 7: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line8);
                    setPathState(8); // Move to arm movement state
                }
                break;
            case 8: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line9);
                    setPathState(9); // Move to arm movement state
                }
                break;
            case 9: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line10);
                    setPathState(10); // Move to arm movement state
                }
                break;
            case 10: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line11);
                    setPathState(11); // Move to arm movement state
                }
                break;
            case 11: // Wait until the path is finished
                if (!follower.isBusy()) {
                    follower.followPath(line12);
                    setPathState(12); // Move to arm movement state
                }
                break;

            case 12: // Wait until the path is finished
                if (!follower.isBusy()) {
                    setPathState(13); // Move to arm movement state
                }
                break;

            case 13: // Move the arm
                double targetPosition = arm.getCurrentPosition() - 2100;
                arm.setPower(-0.5);

                if (arm.getCurrentPosition() <= targetPosition) { // 3s failsafe
                    arm.setPower(0);
                    setPathState(-1); // Stop or proceed to the next action
                }
                break;

        }
    }

    /** These change the states of the paths and actions
     * It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("arm:", arm.getCurrentPosition());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        arm = hardwareMap.get(DcMotor.class, "arm");

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {
    }
}

