package com.jwetherell.augmented_reality.ui.objects;

import android.graphics.Canvas;



public class PaintableCircle extends PaintableObject {

    private int color = 0;
    private float radius = 0;
    private boolean fill = false;

    public PaintableCircle(int color, float radius, boolean fill) {
        set(color, radius, fill);
    }


    public void set(int color, float radius, boolean fill) {
        this.color = color;
        this.radius = radius;
        this.fill = fill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (canvas == null) throw new NullPointerException();

        canvas.save();
        canvas.translate(-radius, -radius);

        setFill(fill);
        setColor(color);
        paintCircle(canvas, x, y, radius);

        canvas.restore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return radius * 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return radius * 2;
    }
}
