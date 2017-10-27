package com.jwetherell.augmented_reality.ui;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.util.Log;

import com.jwetherell.augmented_reality.activity.AugmentedReality;
import com.jwetherell.augmented_reality.camera.CameraModel;
import com.jwetherell.augmented_reality.common.Vector;
import com.jwetherell.augmented_reality.data.ARData;
import com.jwetherell.augmented_reality.data.PhysicalLocation;
import com.jwetherell.augmented_reality.ui.objects.PaintableBoxedText;
import com.jwetherell.augmented_reality.ui.objects.PaintableCircle;
import com.jwetherell.augmented_reality.ui.objects.PaintableGps;
import com.jwetherell.augmented_reality.ui.objects.PaintableIcon;
import com.jwetherell.augmented_reality.ui.objects.PaintableObject;
import com.jwetherell.augmented_reality.ui.objects.PaintablePoint;
import com.jwetherell.augmented_reality.ui.objects.PaintablePosition;

/**
 * 此类表示一个物理位置，并计算其可见性和文本绘制以及视觉表示。如果想改变标记查看方式，需要继承此类。
 * 注意：此类假设如果连个标记具有形同的名称，它们是同一个对象（目的）
 * 
 */
public class Marker implements Comparable<Marker> {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("@#");
    private static final Vector locationVector = new Vector(0, 0, 0);

    private final Vector screenPositionVector = new Vector();
    private final Vector tmpVector = new Vector();
    private final Vector tmpLocationVector = new Vector();
    private final Vector locationXyzRelativeToCameraView = new Vector();
    private final float[] distanceArray = new float[1];
    private final float[] locationArray = new float[3];

    private final StringBuilder textStr = new StringBuilder();

    private final Box box = new Box();

    private float initialY = 0.0f;

    private static CameraModel cam = null;

    
    protected PaintableObject gpsSymbol = null;
    private PaintablePosition symbolContainer = null;

   
    protected PaintableBoxedText textBox = null;
    private PaintablePosition textContainer = null;

  
    private String name = null;
   
    private final PhysicalLocation physicalLocation = new PhysicalLocation();
    
    private double distance = 0.0;
   
    private boolean isOnRadar = false;
   
    private boolean isInView = false;
   
    private final Vector locationXyzRelativeToPhysicalLocation = new Vector();
    
    private int color = Color.WHITE;//标记的默认颜色
    
    private boolean noAltitude = false;

    
    private static boolean debugGpsPosition = false;
    private PaintablePoint positionPoint = null;
    private PaintablePosition positionContainer = null;
	
	private Bitmap bitmap;
	private float TEXT_SIZE;

    public Marker(String name, double latitude, double longitude, double altitude, int color) {
        set(name, latitude, longitude, altitude, color);
    }

//    /*5月1日测试*/


    public void set(String name, double latitude, double longitude, double altitude, int color) {
        if (name == null)
            throw new NullPointerException();

        this.name = name;
        this.physicalLocation.set(latitude, longitude, altitude);
        this.color = color;
        this.isOnRadar = false;
        this.isInView = false;
        this.locationXyzRelativeToPhysicalLocation.set(0, 0, 0);
        this.initialY = 0.0f;
        if (altitude == 0.0d)
            this.noAltitude = true;
        else
            this.noAltitude = false;
    }


    public  String getName() {
        return this.name;
    }


    public int getColor() {
        return this.color;
    }

    public double getDistance() {
        return this.distance;
    }

 
    public float getInitialY() {
        return this.initialY;
    }


    public boolean isOnRadar() {
        return this.isOnRadar;
    }


    public boolean isInView() {
        return this.isInView;
    }

 
    public Vector getScreenPosition() {
        screenPositionVector.set(locationXyzRelativeToCameraView);
        return screenPositionVector;
    }


    public Vector getLocation() {
        return this.locationXyzRelativeToPhysicalLocation;
    }

    public float getHeight() {
        if (symbolContainer == null || textContainer == null)
            return 0f;
        return symbolContainer.getHeight() + textContainer.getHeight();
    }

    public float getWidth() {
        if (symbolContainer == null || textContainer == null)
            return 0f;
        float symbolWidth = symbolContainer.getWidth();
        float textWidth = textContainer.getWidth();
        return (textWidth > symbolWidth) ? textWidth : symbolWidth;
    }


    public void update(Canvas canvas, float addX, float addY) {
        if (canvas == null)
            throw new NullPointerException();

        if (cam == null)
            cam = new CameraModel(canvas.getWidth(), canvas.getHeight(), true);
        cam.set(canvas.getWidth(), canvas.getHeight(), false);
        cam.setViewAngle(CameraModel.DEFAULT_VIEW_ANGLE);
        populateMatrices(cam, addX, addY);
        updateRadar();
        updateView();
    }

    private void populateMatrices(CameraModel cam, float addX, float addY) {
        if (cam == null)
            throw new NullPointerException();

        
        tmpLocationVector.set(locationVector);
        tmpLocationVector.add(locationXyzRelativeToPhysicalLocation);
        tmpLocationVector.prod(ARData.getRotationMatrix());
        cam.projectPoint(tmpLocationVector, tmpVector, addX, addY);
        locationXyzRelativeToCameraView.set(tmpVector);
    }

    private void updateRadar() {
        isOnRadar = false;

        float range = ARData.getRadius() * 1000;
        float scale = range / Radar.RADIUS;
        locationXyzRelativeToPhysicalLocation.get(locationArray);
        float x = locationArray[0] / scale;
        float y = locationArray[2] / scale; // z==y Switched on purpose
        if ((x*x + y*y) < (Radar.RADIUS * Radar.RADIUS))
            isOnRadar = true;
    }

    private void updateView() {
        isInView = false;

        
        if (!isOnRadar)
            return;

        locationXyzRelativeToCameraView.get(locationArray);
        float x = locationArray[0];
        float y = locationArray[1];
        float z = locationArray[2];

        
        if (z >= -1f)
            return;

        float max = Math.max(getWidth(), getHeight()) + 25; 
        float ulX = x - max / 2;
        float ulY = y - max / 2;
        float lrX = x + max / 2;
        float lrY = y + max / 2;
        if (lrX >= -1 && ulX <= cam.getWidth() && lrY >= -1 && ulY <= cam.getHeight())
            isInView = true;
    }

    /**
     * 计算点距离
     */
    public void calcRelativePosition(Location location) {
        if (location == null)
            throw new NullPointerException();

       
        updateDistance(location);

       
        if (noAltitude)
            physicalLocation.setAltitude(location.getAltitude());

       
        PhysicalLocation.convLocationToVector(location, 
                                              physicalLocation, 
                                              locationXyzRelativeToPhysicalLocation);
        initialY = locationXyzRelativeToPhysicalLocation.getY();
        updateRadar();
    }
                  //两点间的距离
    private void updateDistance(Location location) {
        if (location == null)
            throw new NullPointerException();

        Location.distanceBetween(physicalLocation.getLatitude(), 
                                 physicalLocation.getLongitude(), 
                                 location.getLatitude(),
                                 location.getLongitude(), 
                                 distanceArray);
        distance = distanceArray[0];
    }

   
    public boolean handleClick(float x, float y) {
        if (!isOnRadar || !isInView)
            return false;

        boolean result = isPointOnMarker(x, y);
        Log.i("handleClick", "point (x="+x+" y="+y+") isPointOnMarker="+result);
        return result;
    }

   
    public boolean isMarkerOnMarker(Marker marker) {
        return isMarkerOnMarker(marker, true);
    }

  
    private boolean isMarkerOnMarker(Marker marker, boolean reflect) {
        if (marker == null)
            return false;

        marker.getScreenPosition().get(locationArray);
        float x = locationArray[0];
        float y = locationArray[1];

        boolean middleOfMarker = isPointOnMarker(x, y);
        if (middleOfMarker)
            return true;

        boolean onGps = isPaintableOnMarker(marker.gpsSymbol);
        if (onGps)
            return true;

        boolean onText = isPaintableOnMarker(marker.textBox);
        if (onText)
            return true;

       
        return (reflect) ? marker.isMarkerOnMarker(this, false) : false;
    }

    private boolean isPaintableOnMarker(PaintableObject paintable) {
        if (paintable == null)
            return false;

        box.set(paintable);

        // UL
        float ulX = box.ulX;
        float ulY = box.ulY;
        // UR
        float urX = box.urX;
        float urY = box.urY;
        // LL
        float llX = box.llX;
        float llY = box.llY;
        // LR
        float lrX = box.lrX;
        float lrY = box.lrY;

        boolean upperLeftOfMarker = isPointOnMarker(ulX, ulY);
        if (upperLeftOfMarker)
            return true;

        boolean upperRightOfMarker = isPointOnMarker(urX, urY);
        if (upperRightOfMarker)
            return true;

        boolean lowerLeftOfMarker = isPointOnMarker(llX, llY);
        if (lowerLeftOfMarker)
            return true;

        boolean lowerRightOfMarker = isPointOnMarker(lrX, lrY);
        if (lowerRightOfMarker)
            return true;

        return false;
    }

   
    private boolean isPointOnMarker(float xPoint, float yPoint) {
        box.set(gpsSymbol);
        if (box.isPointInBox(xPoint, yPoint))
            return true;
        box.set(textBox);
        if (box.isPointInBox(xPoint, yPoint))
            return true;
        return false;
    }

   
    public void draw(Canvas canvas) {
        if (canvas == null)
            throw new NullPointerException();

        
        if (!isOnRadar || !isInView)
            return;

        
        drawIcon(canvas);

        drawText(canvas);

        
        if (debugGpsPosition)
            drawPosition(canvas);
    }

    protected void drawIcon(Canvas canvas) {
    	/*5月1日测试*/
    	/* float scale = pixelsConverter.getDensity();
         if(scale == 0.75){
             if (gpsSymbol==null) gpsSymbol = new PaintableIcon(bitmap,48,48);
         }else if(scale == 1.5){//diko mou
             if (gpsSymbol==null) gpsSymbol = new PaintableIcon(bitmap,96,96);
         }else if(scale == 2.0){
             if (gpsSymbol==null) gpsSymbol = new PaintableIcon(bitmap,128,128);
         }else{//edw einai gia to default mdpi poy einai 1.0
             if (gpsSymbol==null) gpsSymbol = new PaintableIcon(bitmap,64,64);
         }*/
    	
         
         
        if (canvas == null)
            throw new NullPointerException();

        if (gpsSymbol == null)
            gpsSymbol = new PaintableGps(36, 8, true, getColor());

        getScreenPosition().get(locationArray);
        float x = locationArray[0];
        float y = locationArray[1];

        
        gpsSymbol.setCoordinates(0, -gpsSymbol.getHeight()/2);

        float currentAngle = 0;
        if (AugmentedReality.useMarkerAutoRotate) {
            currentAngle = ARData.getDeviceOrientationAngle()+90;
            currentAngle = 360 - currentAngle;
        }
        if (symbolContainer == null)
            symbolContainer = new PaintablePosition(gpsSymbol, x, y, currentAngle, 1);
        else
            symbolContainer.set(gpsSymbol, x, y, currentAngle, 1);

        symbolContainer.paint(canvas);
    }

    private void drawText(Canvas canvas) {
        if (canvas == null)
            throw new NullPointerException();

        textStr.setLength(0);
        if (distance < 1000.0) {
            textStr.append(name).append(" (").append(DECIMAL_FORMAT.format(distance)).append("m)");
           
        } else {
            double d = distance / 1000.0;
            textStr.append(name).append(" (").append(DECIMAL_FORMAT.format(d)).append("km)");
        }
        float maxHeight = Math.round(canvas.getHeight() / 10f) + 1;

        if (textBox == null)
            textBox = new PaintableBoxedText(textStr.toString(), Math.round(maxHeight / 2f) + 1, 300);
        else
            textBox.set(textStr.toString(), Math.round(maxHeight / 2f) + 1, 300);
        
       /* 5月1日测试
        if (textBox == null) textBox = new PaintableBoxedText(textStr, pixelsConverter.pixelsToSp(TEXT_SIZE), pixelsConverter.pixelsToDips(250));
        else textBox.set(textStr, pixelsConverter.pixelsToSp(TEXT_SIZE), pixelsConverter.pixelsToDips(250));
         TEXT_SIZE = 18;*/

        getScreenPosition().get(locationArray);
        float x = locationArray[0];
        float y = locationArray[1];

       
        textBox.setCoordinates(0, textBox.getHeight()/2);

        float currentAngle = 0;
        if (AugmentedReality.useMarkerAutoRotate) {
            currentAngle = ARData.getDeviceOrientationAngle()+90;
            currentAngle = 360 - currentAngle;
        }
        if (textContainer == null)
            textContainer = new PaintablePosition(textBox, x, y, currentAngle, 1);
        else
            textContainer.set(textBox, x, y, currentAngle, 1);

        textContainer.paint(canvas);
    }

    private void drawPosition(Canvas canvas) {
        if (canvas == null)
            throw new NullPointerException();

        if (positionPoint == null)
            positionPoint = new PaintablePoint(Color.MAGENTA, true);

        getScreenPosition().get(locationArray);
        float x = locationArray[0];
        float y = locationArray[1];

        float currentAngle = 0;
        if (AugmentedReality.useMarkerAutoRotate) {
            currentAngle = ARData.getDeviceOrientationAngle()+90;
            currentAngle = 360 - currentAngle;
        }
        if (positionContainer == null)
            positionContainer = new PaintablePosition(positionPoint, x, y, currentAngle, 1);
        else
            positionContainer.set(positionPoint, x, y, currentAngle, 1);

        positionContainer.paint(canvas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Marker another) {
        if (another == null)
            throw new NullPointerException();

        return name.compareTo(another.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object marker) {
        if (marker == null || name == null)
            throw new NullPointerException();

        return name.equals(((Marker)marker).getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    private static final class Box {

        private final float[] points = new float[]{0,0};
        private final Matrix matrix = new Matrix();

        // UL
        private float ulX = 0;
        private float ulY = 0;
        // UR
        private float urX = 0;
        private float urY = 0;
        // LL
        private float llX = 0;
        private float llY = 0;
        // LR
        private float lrX = 0;
        private float lrY = 0;

        private void set(PaintableObject paintable) {
            if (paintable == null)
                return;

            float x = paintable.getX();
            float y = paintable.getY();
            if ((paintable instanceof PaintableGps) || (paintable instanceof PaintableCircle)) {
                
                x = -paintable.getWidth()/2;
                y = -paintable.getHeight();
            }
            matrix.set(paintable.matrix);

            // UL
            points[0] = x;
            points[1] = y;
            matrix.mapPoints(points);
            ulX = points[0];
            ulY = points[1];

            // UR
            points[0] = x+paintable.getWidth();
            points[1] = y;
            matrix.mapPoints(points);
            urX = points[0];
            urY = points[1];

            // LL
            points[0] = x;
            points[1] = y+paintable.getHeight();
            matrix.mapPoints(points);
            llX = points[0];
            llY = points[1];

            // LR
            points[0] = x+paintable.getWidth();
            points[1] = y+paintable.getHeight();
            matrix.mapPoints(points);
            lrX = points[0];
            lrY = points[1];
        }

       
        private static final byte side(float Ax, float Ay, 
                                          float Bx, float By, 
                                          float x, float y) {
            float result = (Bx-Ax)*(y-Ay) - (By-Ay)*(x-Ax);
            if (result<0) {
                
                return -1;
            }
            if (result>0) {
               
                return 1;
            }
            
            return 0;
        }

       
        private static final boolean between(float aX, float aY, 
                                                float bX, float bY, 
                                                float cX, float cY, 
                                                float dX, float dY, 
                                                float x, float y) {
            byte first = side(aX, aY, bX, bY, x, y);
            byte second = side(cX, cY, dX, dY, x, y);
            if (first==(second*-1)) return true;
            return false;
        }

        private boolean isPointInBox(float xPoint, float yPoint) {
          
            boolean betweenTB = between(ulX, ulY, 
                                         urX, urY, 
                                         llX, llY, 
                                         lrX, lrY, 
                                         xPoint, yPoint);
           
            boolean betweenLR = between(ulX, ulY, 
                                         llX, llY, 
                                         urX, urY, 
                                         lrX, lrY, 
                                         xPoint, yPoint);
            if (betweenTB && betweenLR) return true;
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "ul=("+ulX+", "+ulY+") "+
                    "ur=("+urX+", "+urY+")\n"+
                    "ll=("+llX+", "+llY+") "+
                    "lr=("+lrX+", "+lrY+")";
        }
    }
}
