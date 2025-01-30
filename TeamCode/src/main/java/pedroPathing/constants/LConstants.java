package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.0029;
        ThreeWheelConstants.strafeTicksToInches = -0.0029;
        ThreeWheelConstants.turnTicksToInches = 0.0029;
        ThreeWheelConstants.leftY = 6.125;
        ThreeWheelConstants.rightY = -6.125;
        ThreeWheelConstants.strafeX = 2.75;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "fL";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "fR";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "bL";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.REVERSE;
    }
}




