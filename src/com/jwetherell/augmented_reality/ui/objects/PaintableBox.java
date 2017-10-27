package com.jwetherell.augmented_reality.ui.objects;

import android.graphics.Canvas;
import android.graphics.Color;


public class PaintableBox extends PaintableObject {

    private float width = 0, height = 0;
    private int borderColor = Color.rgb(255, 255, 255);
    private int backgroundColor = Color.argb(128, 0, 0, 0);

    public PaintableBox(float width, float height) {
        this(width, height, Color.rgb(255, 255, 255), Color.argb(128, 0, 0, 0));
      
    }

    public PaintableBox(float width, float height, int borderColor, int bgColor) {
        set(width, height, borderColor, bgColor);
    }


    public void set(float width, float height) {
        set(width, height, borderColor, backgroundColor);
    }


    public void set(float width, float height, int borderColor, int bgColor) {
        this.width = width;
        this.height = height;
        this.borderColor = borderColor;
        this.backgroundColor = bgColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (canvas == null) throw new NullPointerException();

        canvas.save();
        canvas.translate(-width/2, -height/2);

        setFill(true);
        setColor(backgroundColor);
        paintRect(canvas, x, y, width, height);

        setFill(false);
        setColor(borderColor);
        paintRect(canvas, x, y, width, height);

        canvas.restore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return height;
    }
}
