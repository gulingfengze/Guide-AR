package com.jwetherell.augmented_reality.camera;

import com.jwetherell.augmented_reality.common.Vector;

/**
 * 摄像机视图。
 */
public class CameraModel {

    private static final float[] tmp1 = new float[3];
    private static final float[] tmp2 = new float[3];

    private int width = 0;
    private int height = 0;
    private float viewAngle = 0F;
    private float distance = 0F;

    public static final float DEFAULT_VIEW_ANGLE = (float) Math.toRadians(45);

    public CameraModel(int width, int height) {
        this(width, height, true);
    }

    public CameraModel(int width, int height, boolean init) {
        set(width, height, init);
    }

    /**
     * 设置这个参数对象
     */
    public void set(int width, int height, boolean init) {
        this.width = width;
        this.height = height;
    }

    /**
     * 相机视图的宽度
     */
    public int getWidth() {
        return width;
    }

    /**
     * 相机视图的高度。
     */
    public int getHeight() {
        return height;
    }

    /**
     * 设置摄像机模型的视角。
     */
    public void setViewAngle(float viewAngle) {
        this.viewAngle = viewAngle;
        this.distance = (this.width / 2) / (float) Math.tan(viewAngle / 2);
    }

    /**
     * 设置摄像机模型的视角。
     */
    public void setViewAngle(int width, int height, float viewAngle) {
        this.viewAngle = viewAngle;
        this.distance = (width / 2) / (float) Math.tan(viewAngle / 2);
    }

    /**
     * 从原点向量到投影向量的投影点。
     */
    public void projectPoint(Vector orgPoint, Vector prjPoint, float addX, float addY) {
        orgPoint.get(tmp1);
        tmp2[0] = (distance * tmp1[0] / -tmp1[2]);
        tmp2[1] = (distance * tmp1[1] / -tmp1[2]);
        tmp2[2] = (tmp1[2]);
        tmp2[0] = (tmp2[0] + addX + width / 2);
        tmp2[1] = (-tmp2[1] + addY + height / 2);
        prjPoint.set(tmp2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CAM(" + width + "," + height + "," + viewAngle + "," + distance + ")";
    }
}
