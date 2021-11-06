package model;
import java.util.ArrayList;
import java.util.List;

public class  Polygon {
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
        return points.get(i).getX();
    }
    public int getPointY(int i){
        return points.get(i).getY();

    }
}
