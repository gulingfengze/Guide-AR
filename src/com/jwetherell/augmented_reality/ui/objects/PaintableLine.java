package com.jwetherell.augmented_reality.ui.objects;

import android.graphics.Canvas;



public class PaintableLine extends PaintableObject {

    private int color = 0;

    public PaintableLine(int color, float x, float y) {
        set(color, x, y);
    }

    
    public void set(int color, float x, float y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (canvas == null) throw new NullPointerException();

        setFill(false);
        setColor(color);
        paintLine(canvas, 0, 0, x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return y;
    }
}
