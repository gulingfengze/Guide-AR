/*
 * Copyright (C) 2010- Peer internet solutions
 * 
 * This file was an original part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package com.jwetherell.augmented_reality.common;

import com.jwetherell.augmented_reality.data.ARData;

/**
 * 一个静态类，用于计算方位、俯仰和旋转。矩阵。
 * 
 * 参考 <http://www.mixare.org/>
 */
public class Navigation {

    private static final Vector looking = new Vector();
    private static final float[] lookingArray = new float[3];
    private static final Matrix tempMatrix = new Matrix();
    private static final float unitPerDegree = 1f/90f;

    private static float azimuth = 0;
    private static float pitch = 0;
    private static float roll = 0;

    private Navigation() { }

    /**
     * 获得两点之间的度角
     */
    public static final float getAngle(float center_x, float center_y, float post_x, float post_y) {
        float delta_x = post_x - center_x;
        float delta_y = post_y - center_y;
        return (float)(Math.atan2(delta_y, delta_x) * 180 / Math.PI);
    }

   
    public static float getAzimuth() {
        return azimuth;
    }

  
    public static float getPitch() {
        return pitch;
    }

  
    public static float getRoll() {
        return roll;
    }

    /**
     * 计算并填充方位角，俯仰和滚动
     */
    public static void calcPitchBearing(Matrix rotationMatrix) {
        if (rotationMatrix == null) return;

        tempMatrix.set(rotationMatrix);
        tempMatrix.transpose();

        float x = 0;
        float y = 0;
        int angle = ARData.getDeviceOrientationAngle();
        if (angle>=0 && angle<90) {
            x = (angle*unitPerDegree)-1;
            y = 1-(angle*unitPerDegree);
        } else if (angle>=90 && angle<180) {
            angle -= 90;
            x = (angle*unitPerDegree)-1;
            y = (angle*unitPerDegree)-1;
        } else if (angle>=180 && angle<270) {
            angle -= 180;
            x = 1-(angle*unitPerDegree);
            y = (angle*unitPerDegree)-1;
        } else {
            // >= 270 && < 360
            angle -= 270;
            x = 1-(angle*unitPerDegree);
            y = 1-(angle*unitPerDegree);
        }
        looking.set(x, y, 0);
        looking.prod(tempMatrix);
        looking.get(lookingArray);

        azimuth = ((getAngle(0, 0, lookingArray[0], lookingArray[2]) + 360) % 360);

        roll = -(90 - Math.abs(getAngle(0, 0, lookingArray[1], lookingArray[2])));

        looking.set(0, 0, 1);
        looking.prod(tempMatrix);
        looking.get(lookingArray);
        pitch = -(90 - Math.abs(getAngle(0, 0, lookingArray[1], lookingArray[2])));
    }
}
