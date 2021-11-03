package control;

import fill.SeedFiller;
import model.Point;
import model.Polygon;
import rasterize.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller2D implements Controller {

    private final Panel panel;
    private final Raster raster;
    private  RasterBufferedImage rasterBufferedImage;

    private LineRasterizer trivialLineRasterizer, dottedLineRasterizer, dashedLineRasterizer, dashAndDottedLineRasterizer;
    private SeedFiller seedFiller;
    private  Polygon polygon;
    private PolygonRasterizer polygonRasterizer;
    private CircleRasterizer circleRasterizer;


    private int x, y, i,xx,yy = 0;

    public Controller2D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        initObjects(panel.getRaster());
        initListeners(panel);

//        raster.setPixel(20, 100, 0x00ff00);
//        raster.setPixel(60, 60, Color.GREEN.getRGB());
//
//        for (int y = 100; y <= 300; y++) {
//            raster.setPixel(100, y, 0xffff00);
//        }
    }

    private void initObjects(Raster raster) {
        trivialLineRasterizer = new FilledLineRasterizer(raster);
        dottedLineRasterizer = new DottedLineRasterizer(raster);
        dashedLineRasterizer = new DashedLineRasterizer(raster);
        dashAndDottedLineRasterizer = new DashAndDottedLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(raster);
        circleRasterizer = new CircleRasterizer(raster);
        seedFiller = new SeedFiller(raster);
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                //polygon.addPoints(new Point(x,y));
                if(i==0){
                    polygonRasterizer.setPoint(x, y);
                    i++;
                }

            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)){
                    //raster.clear();
                  //  trivialLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xf0ff0f);
                    polygonRasterizer.setPoint(e.getX(), e.getY());
                    if (i>2) polygonRasterizer.rasterize(polygonRasterizer.getPointX(i-1), polygonRasterizer.getPointY(i-1),polygonRasterizer.getPointX(0), polygonRasterizer.getPointY(0),0x000000 );
                    if (i==1){
                        polygonRasterizer.rasterize(polygonRasterizer.getPointX(0), polygonRasterizer.getPointY(0),polygonRasterizer.getPointX(i), polygonRasterizer.getPointY(i),0xf0ff0f );
                        i++;
                    } else {
                        polygonRasterizer.rasterize(polygonRasterizer.getPointX(i-1), polygonRasterizer.getPointY(i-1),polygonRasterizer.getPointX(i), polygonRasterizer.getPointY(i),0xf0ff0f );
                        polygonRasterizer.rasterize(polygonRasterizer.getPointX(i), polygonRasterizer.getPointY(i),polygonRasterizer.getPointX(0), polygonRasterizer.getPointY(0),0xf0ff0f );
                        i++;
                    }

                }if (SwingUtilities.isRightMouseButton(e)){
                    xx = e.getX();
                    yy = e.getY();
                    circleRasterizer.rasterize(x, y,e.getX(),e.getY(),0xf0ff0f);
                    trivialLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xf00f0f);

                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //x = e.getX();
                        //y = e.getY();
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                       seedFiller.setSeed(new Point(e.getX(), e.getY()));
                        seedFiller.setFillColor(Color.YELLOW.getRGB());
                        seedFiller.fill();
                    }
//                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    //raster.clear();
                    //dottedLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xffffff);
                   // circleRasterizer.rasterize(x, y,e.getX(),e.getY(),0xf0ff0f);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //raster.clear();
                    //dashedLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xffffff);
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    raster.clear();
                    dashAndDottedLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xffffff);
                }
                update();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // na klávesu C vymazat plátno
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    polygonRasterizer.clearPoints();
                    i=0;
                    raster.clear();
                    update();

                }
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());



            }
        });
    }

    private void update() {
//        panel.clear();
        //TODO
        panel.repaint();
    }

    private void hardClear() {
        panel.clear();
    }

}
