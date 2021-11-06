package rasterize;

import model.Point;
import java.util.ArrayList;
import java.util.List;

public class PolygonRasterizer {
   List<Point> points = new ArrayList<>();


    public int getSize(){
       return points.size();
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





}
