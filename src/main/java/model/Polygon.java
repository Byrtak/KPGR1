package model;
import rasterize.FilledLineRasterizer;
import rasterize.Raster;
import java.util.List;

public class  Polygon {
    List<Point> points;
    FilledLineRasterizer trivialLineRasterizer;
    private final Raster raster;

    public Polygon(Raster raster, List<Point> points){
        trivialLineRasterizer = new FilledLineRasterizer(raster);
        this.raster  = raster;
        this.points = points;
    }

    public void drawPoly() {
        raster.clear();
        if (points.size() > 1) {
            for (int i = 0; i < points.size(); i++) {
                trivialLineRasterizer.rasterize(points.get((i + 1) % points.size()).getX(), points.get((i + 1) % points.size()).getY(), points.get(i).getX(), points.get(i).getY(), 0xffffff);
            }
        }
    }

}
