package pedroPathing.examples;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "Example Auto Blue", group = "Examples")
public class ValenAutonLeft extends OpMode {

    private Follower follower;
    private Timer opmodeTimer;

    /** Start Pose of our robot */
    private final Pose startPose = new Pose(9, 111, Math.toRadians(270));
    private PathChain grabPickup1;
    public void buildPaths() {
        grabPickup1 = follower.pathBuilder()
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(11.755, 62.694, Point.CARTESIAN),
                                new Point(11.755, 10.188, Point.CARTESIAN)
                        ))
                .build();
    }
    public void autonomousPathUpdate() {
        follower.followPath(grabPickup1,true);
    }


    @Override
    public void loop() {
        autonomousPathUpdate();

    }

    @Override
    public void init() {
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
    }

    @Override
    public void stop() {
    }
}

