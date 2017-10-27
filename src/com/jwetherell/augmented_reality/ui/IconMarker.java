package com.jwetherell.augmented_reality.ui;

import com.jwetherell.augmented_reality.ui.objects.PaintableIcon;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * ����̳���Marker������һ��������ӻ���ԲȦ
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

      
        //��Marker�ж���GPS����
        if (gpsSymbol == null) gpsSymbol = new PaintableIcon(bitmap, 200, 200);//�������������ֱ����ñ�ǵĿ�͸�
        super.drawIcon(canvas);
    }
}
