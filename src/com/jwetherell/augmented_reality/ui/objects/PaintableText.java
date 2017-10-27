package com.jwetherell.augmented_reality.ui.objects;

import android.graphics.Canvas;
import android.graphics.Color;



public class PaintableText extends PaintableObject {

    private static final float WIDTH_PAD = 4;
    private static final float HEIGHT_PAD = 2;

    private CharSequence text = null;
    private int color = 0;
    private int size = 0;
    private float width = 0;
    private float height = 0;
    private boolean bg = false;

    public PaintableText(CharSequence text, int color, int size, boolean paintBackground) {
        set(text, color, size, paintBackground);
    }

   
    public void set(CharSequence text, int color, int size, boolean paintBackground) {
        if (text == null) throw new NullPointerException();

        this.text = text;
        this.bg = paintBackground;
        this.color = color;
        this.size = size;
        this.width = getTextWidth(text,0,text.length()) + WIDTH_PAD * 2;
        this.height = getTextAsc() + getTextDesc() + HEIGHT_PAD * 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Canvas canvas) {
        if (canvas == null || text == null) throw new NullPointerException();

        setColor(color);
        setFontSize(size);
        if (bg) {
            canvas.save();
            canvas.translate(-width/2, -height/2);

            setColor(Color.rgb(0, 0, 0));
            setFill(true);
            paintRect(canvas, x, y, width, height);
            setColor(Color.rgb(255, 255, 255));
            setFill(false);
            paintRect(canvas, x, y, width, height);

            canvas.restore();
        }
        paintText(canvas, (WIDTH_PAD-(width/2)), (HEIGHT_PAD+getTextAsc()-(height/2)), text, 0, text.length());
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
