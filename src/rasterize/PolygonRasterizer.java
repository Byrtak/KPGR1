package rasterize;

import model.Point;

import java.util.ArrayList;
import java.util.List;

public class PolygonRasterizer extends LineRasterizer{
   List<Point> points = new ArrayList<>();




    public PolygonRasterizer(Raster raster) {
        super(raster);
    }

    public void setPoint(int x, int y){
        points.add(new Point(x,y));

    }
    public void clearPoints(){
        points.clear();

    }
    public int getPointX(int i){
        Point point = points.get(i);
        return point.getX();
    }
    public int getPointY(int i){
        Point point = points.get(i);
        return point.getY();
    }




    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {

        int dx = x2 - x1;
        int dy = y2 - y1;
        float k =  dy/ (float) dx;
        float q = y1 - k * x1;
        if(Math.abs(dy) < Math.abs(dx)){
            if (x2<x1) {
                for (int x = x2 ; x <=x1 ; x ++) {
                    float y = k * x +q;
                    raster.setPixel(x, Math.round(y),color);
                }
            }else {
                for (int x = x1; x <= x2; x++) {
                    float y = k * x + q;
                    raster.setPixel(x, Math.round(y), color);
                }
            }
        } else {
            if (y2<y1) {
                for (int y = y2 ; y <=y1 ; y ++) {
                    float x = (y-q)/k;
                    raster.setPixel(Math.round(x),y,color);
                }
            }else {
                for (int y = y1 ; y <=y2 ; y ++) {
                    float x = (y-q)/k;
                    raster.setPixel(Math.round(x), y, color);
                }
            }

        }

    }



}
