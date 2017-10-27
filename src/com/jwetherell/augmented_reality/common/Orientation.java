package com.jwetherell.augmented_reality.common;


/**
 * 检测设备的方向，因为它的值来自于加速度计。
 */
public class Orientation {

    private static ORIENTATION currentOrientation = ORIENTATION.UNKNOWN;
    private static int orientationAngle = -1;

    private Orientation() { }

    public static ORIENTATION getDeviceOrientation() {
        return currentOrientation;
    }

    public static int getDeviceAngle() {
        return orientationAngle;
    }

    public static enum ORIENTATION {
        UNKNOWN, PORTRAIT, PORTRAIT_UPSIDE_DOWN, LANDSCAPE, LANDSCAPE_UPSIDE_DOWN
    };

    public static void calcOrientation(float[] accel_values) {
        int tempOrientation = -1;
        float X = -accel_values[0];
        float Y = -accel_values[1];
        float Z = -accel_values[2];
        float magnitude = X * X + Y * Y;
  
        if (magnitude * 4 >= Z * Z) {
            float OneEightyOverPi = 57.29577957855f;
            float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
            tempOrientation = 90 - Math.round(angle);
            //标准化到0-359范围
            while (tempOrientation >= 360) {
                tempOrientation -= 360;
            }
            while (tempOrientation < 0) {
                tempOrientation += 360;
            }
        }


        // 找出基于度的方向
        ORIENTATION tempOrientRounded = ORIENTATION.UNKNOWN;
        // 算出实际的方向
        if (tempOrientation <= 45 || tempOrientation > 315) { // round to 0
            tempOrientRounded = ORIENTATION.PORTRAIT;// portrait
        } else if (tempOrientation > 45 && tempOrientation <= 135) { // round to 90
            tempOrientRounded = ORIENTATION.LANDSCAPE_UPSIDE_DOWN; // landscape left
        } else if (tempOrientation > 135 && tempOrientation <= 225) { // round to 180
            tempOrientRounded = ORIENTATION.PORTRAIT_UPSIDE_DOWN; // portrait upside down
        } else if (tempOrientation > 225 && tempOrientation <= 315) { // round to 270
            tempOrientRounded = ORIENTATION.LANDSCAPE;// landscape right
        }

        orientationAngle = tempOrientation;
        currentOrientation = tempOrientRounded;
    }
}
