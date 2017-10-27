package com.jwetherell.augmented_reality.data;

import android.util.FloatMath;

/**
 * 用作计算相对位置的实用工具。
 */
public class ScreenPosition {

    private float x = 0f;
    private float y = 0f;

    public ScreenPosition() {
        set(0, 0);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

 
    public float getX() {
        return x;
    }

   
    public void setX(float x) {
        this.x = x;
    }

   
    public float getY() {
        return y;
    }

   
    public void setY(float y) {
        this.y = y;
    }

    public void rotate(float t) {
        float xp = FloatMath.cos(t) * x - FloatMath.sin(t) * y;
        float yp = FloatMath.sin(t) * x + FloatMath.cos(t) * y;
        x = xp;
        y = yp;
    }

    
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "< x=" + x + " y=" + y + " >";
    }
}
