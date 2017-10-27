package com.jwetherell.augmented_reality.ui.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class PaintableIcon extends PaintableObject {

    private Bitmap bitmap = null;

    public PaintableIcon(Bitmap bitmap, int width, int height) {
        set(bitmap, width, height);
    }

    public void set(Bitmap bitmap, int width, int height) {
        if (bitmap == null) throw new NullPointerException();

        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (canvas == null || bitmap == null) throw new NullPointerException();

        canvas.save();
        canvas.translate(-getWidth()/2, -getHeight()/2);

        paintBitmap(canvas, bitmap, x, y);

        canvas.restore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return bitmap.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return bitmap.getHeight();
    }
}
