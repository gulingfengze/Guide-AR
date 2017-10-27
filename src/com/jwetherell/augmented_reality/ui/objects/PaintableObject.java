package com.jwetherell.augmented_reality.ui.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;


public abstract class PaintableObject {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rect = new RectF();

    protected float x = 0;
    protected float y = 0;

    public Matrix matrix = new Matrix();

    public PaintableObject() {
        if (paint == null) {
            paint = new Paint();
            paint.setTextSize(16);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
        }
    }

    
    public void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

   
    public float getX() {
        return x;
    }

   
    public float getY() {
        return y;
    }

    
    public abstract float getWidth();

   
    public abstract float getHeight();

  
    public abstract void paint(Canvas canvas);

    
    public void setFill(boolean fill) {
        if (fill) paint.setStyle(Paint.Style.FILL);
        else paint.setStyle(Paint.Style.STROKE);
    }

   
    public void setColor(int c) {
        paint.setColor(c);
    }

    
    public void setStrokeWidth(float w) {
        paint.setStrokeWidth(w);
    }

   
    public float getTextWidth(CharSequence txt, int start, int end) {
        if (txt == null) throw new NullPointerException();
        return paint.measureText(txt, start, end);
    }

    
    public float getTextAsc() {
        return -paint.ascent();
    }

    
    public float getTextDesc() {
        return paint.descent();
    }

   
    public void setFontSize(float size) {
        paint.setTextSize(size);
    }

   
    public void paintLine(Canvas canvas, float x1, float y1, float x2, float y2) {
        if (canvas == null) throw new NullPointerException();

        canvas.getMatrix(matrix);

        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    
    public void paintRect(Canvas canvas, float x, float y, float width, float height) {
        if (canvas == null) throw new NullPointerException();

        canvas.getMatrix(matrix);

        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    public void paintRoundedRect(Canvas canvas, float x, float y, float width, float height) {
        if (canvas == null) throw new NullPointerException();

        canvas.getMatrix(matrix);

        rect.set(x, y, x + width, y + height);
        canvas.drawRoundRect(rect, 15F, 15F, paint);
    }

   
    public void paintBitmap(Canvas canvas, Bitmap bitmap, float left, float top) {
        if (canvas == null || bitmap == null) throw new NullPointerException();

        canvas.getMatrix(matrix);

        canvas.drawBitmap(bitmap, left, top, paint);
    }

   
    public void paintCircle(Canvas canvas, float x, float y, float radius) {
        if (canvas == null) throw new NullPointerException();

        canvas.save();
        canvas.translate(radius,radius);

        canvas.getMatrix(matrix);

        canvas.drawCircle(x, y, radius, paint);
        canvas.restore();
    }

   
    public void paintText(Canvas canvas, float x, float y, CharSequence text, int start, int end) {
        if (canvas == null || text == null) throw new NullPointerException();

        canvas.getMatrix(matrix);

        canvas.drawText(text, start, end, x, y, paint);
    }

   
    public void paintObj(Canvas canvas, PaintableObject obj, float x, float y, float rotation, float scale) {
        if (canvas == null || obj == null) throw new NullPointerException();

        canvas.save();
        canvas.translate(x,y);
        canvas.rotate(rotation);
        canvas.scale(scale, scale);
        obj.paint(canvas);
        matrix.set(obj.matrix);
        canvas.restore();
    }
}
