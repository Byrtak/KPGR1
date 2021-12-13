package control;

import fill.ScanLine;
import fill.SeedFiller;
import model.Point;
import model.Polygon;
import rasterize.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller2D implements Controller {

    private final Panel panel;
    private final Raster raster;
    private  RasterBufferedImage rasterBufferedImage;
    List<Point> points = new ArrayList();
    private LineRasterizer trivialLineRasterizer, dottedLineRasterizer, dashedLineRasterizer, dashAndDottedLineRasterizer;
    private SeedFiller seedFiller;
    private ScanLine scanLine;
    private  Polygon polygon;
    private CircleRasterizer circleRasterizer;


    private int x, y, i = 0;
    private boolean open =false;

    public Controller2D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        initObjects(panel.getRaster());
        initListeners(panel);
    }

    private void initObjects(Raster raster) {
        trivialLineRasterizer = new FilledLineRasterizer(raster);
        dottedLineRasterizer = new DottedLineRasterizer(raster);
        dashedLineRasterizer = new DashedLineRasterizer(raster);
        circleRasterizer = new CircleRasterizer(raster);
        seedFiller = new SeedFiller(raster);
        polygon = new Polygon(raster,points);
        scanLine = new ScanLine(raster,points,0xf00f0f,0x11F314);

    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                if (SwingUtilities.isLeftMouseButton(e)){
                    if (points.size() ==  0) {
                        points.add( new Point(e.getX(), e.getY()));
                    }
                    open = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)){
                    points.add( new Point(e.getX(), e.getY()));
                    open = false;
                    polygon.drawPoly();
                    update();
                }if (SwingUtilities.isRightMouseButton(e)){
                      dashedLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xf00f0f);
                }
                update();
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                    if (SwingUtilities.isLeftMouseButton(e)) {

                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        scanLine.fill();

                    }
               update();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    if (SwingUtilities.isMiddleMouseButton(e)){
                        scanLine.fill();
                    }
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                   polygon.drawPoly();
                    if (open){
                        polygon.drawPoly();
                        if (points.size() > 0 ){
                            i = points.size();
                            dottedLineRasterizer.rasterize(points.get(i-1).x,points.get(i-1).y,e.getX(),e.getY(),0xf00f0f);
                            dottedLineRasterizer.rasterize(points.get(0).x,points.get(0).y,e.getX(),e.getY(),0xf00f0f);
                        }
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    panel.clear();
                    points.clear();
                    circleRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xffffff);
                    dashedLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xffffff);
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    seedFiller.setSeed(new Point(e.getX(), e.getY()));
                    seedFiller.setFillColor(Color.YELLOW.getRGB());
                    seedFiller.fill();
                }
                update();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // na klávesu C vymazat plátno
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    points.clear();
                    panel.clear();
                    update();

                }
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
                update();
            }
        });
    }

    private void update() {
        panel.repaint();
    }


}
