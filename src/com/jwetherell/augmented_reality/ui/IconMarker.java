package com.jwetherell.augmented_reality.ui;

import com.jwetherell.augmented_reality.ui.objects.PaintableIcon;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 此类继承自Marker，绘制一个替代可视化的圆圈
 */
public class IconMarker extends Marker {

    private Bitmap bitmap = null;

    public IconMarker(String name, double latitude, double longitude, double altitude, int color, Bitmap bitmap) {
        super(name, latitude, longitude, altitude, color);
        this.bitmap = bitmap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawIcon(Canvas canvas) {
        if (canvas == null || bitmap == null) throw new NullPointerException();

      
        //在Marker中定义GPS符号
        if (gpsSymbol == null) gpsSymbol = new PaintableIcon(bitmap, 200, 200);//后面两个参数分别设置标记的宽和高
        super.drawIcon(canvas);
    }
}
