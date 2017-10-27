package com.jwetherell.augmented_reality.ui.objects;

import android.graphics.Canvas;


public class PaintableGps extends PaintableObject {

    private float radius = 0;
    private float strokeWidth = 0;
    private boolean fill = false;
    private int color = 0;

    public PaintableGps(float radius, float strokeWidth, boolean fill, int color) {
        set(radius, strokeWidth, fill, color);
    }

   
    public void set(float radius, float strokeWidth, boolean fill, int color) {
        this.radius = radius;
        this.strokeWidth = strokeWidth;
        this.fill = fill;
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (canvas == null) throw new NullPointerException();

        canvas.save();
        canvas.translate(-radius, -radius);

        setStrokeWidth(strokeWidth);
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
        return (radius * 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return (radius * 2);
    }
}
