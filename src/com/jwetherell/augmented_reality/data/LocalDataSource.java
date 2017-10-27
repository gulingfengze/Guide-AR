package com.jwetherell.augmented_reality.data;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.jwetherell.augmented_reality.R;
import com.jwetherell.augmented_reality.ui.IconMarker;
import com.jwetherell.augmented_reality.ui.Marker;

/**
 * 这个类用来添加本地数据：可以通过编程直接添加（这里我直接添加目标数据信息）、SQLite数据库等其他方式添加数据源。
 * 如果数据信息庞大的话使用SQLite数据库好些。
 */

public class LocalDataSource extends DataSource {

    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    
    private static Bitmap icon = null;
    private static Bitmap libz = null;
    private static Bitmap yfz = null;
    private static Bitmap lds = null;
    private static Bitmap wds = null;
    private static Bitmap qxs = null;
    private static Bitmap sxs = null;


    public LocalDataSource(Resources res) {
        if (res == null) throw new NullPointerException();

        createIcon(res);
    }

    protected void createIcon(Resources res) {
        if (res == null) throw new NullPointerException();

        libz = BitmapFactory.decodeResource(res, R.drawable.lib);//兴趣点图标
        icon = BitmapFactory.decodeResource(res, R.drawable.building);
        yfz = BitmapFactory.decodeResource(res, R.drawable.yf);
        lds = BitmapFactory.decodeResource(res, R.drawable.ld);
        wds = BitmapFactory.decodeResource(res, R.drawable.wd);
        qxs = BitmapFactory.decodeResource(res, R.drawable.qx);
        sxs = BitmapFactory.decodeResource(res, R.drawable.sx);
    }
    public List<Marker> getMarkers() {
//        Marker atl = new IconMarker("ATL ICON", 39.931268, -75.051262, 0, Color.DKGRAY, icon);
//        cachedMarkers.add(atl);
//
//        Marker home = new Marker("ATL CIRCLE", 39.931269, -75.051231, 0, Color.YELLOW);
//        cachedMarkers.add(home);
    	
    
    	
  
        Marker lib = new IconMarker("南京信息工程大学图书馆", 32.20482531, 118.70838091, 39.0, Color.RED,libz);
        cachedMarkers.add(lib);
        Marker md = new IconMarker("明德楼", 32.20663651, 118.71332503, 44.0, Color.RED, icon);
        cachedMarkers.add(md);
        Marker dy = new IconMarker("地理与遥感学院", 32.20784918, 118.71234129, 39.0, Color.RED, icon);
        cachedMarkers.add(dy);
        Marker yf = new IconMarker("逸夫楼", 32.20376555, 118.70591098, 54.0, Color.RED, yfz);
        cachedMarkers.add(yf);
        Marker jsj = new IconMarker("计算机与软件学院", 32.2078213, 118.71125784, 38.0, Color.RED, icon);
        cachedMarkers.add(jsj);
        Marker xk = new IconMarker("信息与控制学院", 32.20521818, 118.70642963, 33.0, Color.RED, icon);
        cachedMarkers.add(xk);
        Marker dx = new IconMarker("电子与信息工程学院", 32.20477784, 118.70718374, 44.0, Color.RED, icon);
        cachedMarkers.add(dx);
        Marker hk = new IconMarker("环境科学与工程学院", 32.20508103, 118.70741369, 48.0, Color.RED, icon);
        cachedMarkers.add(hk);
        Marker ld = new IconMarker("雷丁学院", 32.20633604, 118.71978314, 30.0, Color.RED,lds);
        cachedMarkers.add(ld);
        Marker ty = new IconMarker("体育馆", 32.20808385, 118.71950017, 8.0, Color.RED,icon);
        cachedMarkers.add(ty);
        Marker xz = new IconMarker("行政楼", 32.20623881, 118.71894502, 24.0, Color.RED, icon);
        cachedMarkers.add(xz);
        Marker wd = new IconMarker("文德楼", 32.20582777, 118.71638087, 50.0, Color.RED,wds);
        cachedMarkers.add(wd);
        Marker qx = new IconMarker("气象楼", 32.20636413, 118.7173245, 35.0, Color.RED,qxs);
        cachedMarkers.add(qx);
        Marker sx = new IconMarker("尚贤楼", 32.20641658, 118.71339664, 31.0, Color.RED,sxs);
        cachedMarkers.add(sx);
        Marker en = new IconMarker("英语角", 32.20754982, 118.71078235, 53.0, Color.RED, icon);
        cachedMarkers.add(en);
        Marker ta = new IconMarker("长望塔", 32.20748056, 118.71131861, 51.0, Color.RED, icon);
        cachedMarkers.add(ta);
        Marker fzy = new IconMarker("范仲淹像", 32.20643223, 118.71180418, 40.0, Color.RED, icon);
        cachedMarkers.add(fzy);
        Marker xcw = new IconMarker("徐长望像", 32.20594871, 118.7173723, 18.0, Color.RED, icon);
        cachedMarkers.add(xcw);
        
        
        
        
        
        /*
         * Marker lon = new IconMarker(
         * 
         * , 39.95335, -74.9223445, 0, Color.MAGENTA, icon);
         * cachedMarkers.add(lon); Marker lon2 = new IconMarker(
         * 
         * , 39.95334, -74.9223446, 0, Color.MAGENTA, icon);
         * cachedMarkers.add(lon2);
         */

        /*
         * float max = 10; for (float i=0; i<max; i++) { Marker marker = null;
         * float decimal = i/max; if (i%2==0) marker = new Marker("Test-"+i,
         * 39.99, -75.33+decimal, 0, Color.LTGRAY); marker = new
         * IconMarker("Test-"+i, 39.99+decimal, -75.33, 0, Color.LTGRAY, icon);
         * cachedMarkers.add(marker); }
         */

        return cachedMarkers;
    }
}
