package fill;

import model.Edge;
import model.Point;
import rasterize.FilledLineRasterizer;
import rasterize.Raster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLine implements Filler {

    private final Raster raster;
    private final List<Point> points;
    private final int fillColor;
    private final int borderColor;
    private final FilledLineRasterizer trivialLineRasterizer;

    public ScanLine(Raster raster, List<Point> points, int fillColor, int borderColor) {
        trivialLineRasterizer = new FilledLineRasterizer(raster);
        this.raster = raster;
        this.points = points;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }


    @Override
    public void fill() {
        List<Edge> edges = new ArrayList<>();
        int minY =0;
        int maxY =0;
        int y;
        int x1 ,x2 ,y1 ,y2 ;
        // projet všechny body (list points) a vytvořit z nich hrany
        for (y = 0; y < points.size(); y++) {
            // 0. a 1. bod budou první hranou; 1. a 2. budou druhou hranou; ...; poslední a nultý
            x1 = points.get(y).getX();
            y1 = points.get(y).getY();
            x2 = points.get((y+1) % points.size()).getX();
            y2 = points.get((y+1)% points.size()).getY();
            Edge e = new Edge(x1,y1,x2,y2);
            // rovnou ignorovat vodorovné hrany
            if(e.isHorizontal()){
                // vytvořené nevodorovné hrany zorientovat a přidat do seznamu
                if (y1>y2) e.orientate();
                // výsledkem bude seznam zorientovaných hran bez vodorovných úseků
                edges.add(e);
            }
            // najít minimum a maximum pro Y
            if (y==0) {
                minY=points.get(y).getY();
                maxY = minY;
            }else {
                // projet všechny body (list points) a najít minimální a maximální Y (optimalizační krok)
                minY = Math.min(minY, points.get(y).getY());
                maxY = Math.max(maxY, points.get(y).getY());
            }
        }

        for (y = minY; y <= maxY; y++) {
            int i;
            List<Integer> intersections = new ArrayList<>();
            // projít všechny hrany (list edges)
            for(Edge edge: edges){
                // pokud hrana má průsečík na daném Y...
                if (edge.hasIntersection(y)){
                    // ... tak vypočítat X hodnotu průsečíku a uložit ji do seznamu
                    // nyní je naplněný seznam průsečíků
                    intersections.add(edge.getIntersection(y));
                }

            }
            // setřídit průsečíky
            Collections.sort(intersections);
            // spojení vždy sudého s lichým (tj. 0. a 1.; 2. a 3.; 4. a 5....)
            for(i = intersections.size() - 1; i > 0; i -= 2) {
                for(int x = intersections.get(i - 1); x <= intersections.get(i); ++x) {
                    // kreslení úseček
                    this.raster.setPixel(x,y,this.fillColor);
                }
            }
        }


        // obtáhnout hranici tělesa definovaného pomocí listu points
        for(y = 0; y < this.points.size(); ++y) {
            x1 = this.points.get(y).x;
            y1 = this.points.get(y).y;
            x2 = this.points.get((y + 1) % this.points.size()).x;
            y2 = this.points.get((y + 1) % this.points.size()).y;
            this.trivialLineRasterizer.rasterize(x1, y1, x2, y2,this.borderColor);
        }
    }
}
