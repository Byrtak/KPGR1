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
        dashAndDottedLineRasterizer = new DashAndDottedLineRasterizer(raster);
        circleRasterizer = new CircleRasterizer(raster);
        seedFiller = new SeedFiller(raster);
        polygon = new Polygon();

    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                if (SwingUtilities.isLeftMouseButton(e)){
                    if (polygon.getSize() ==  0) {
                        polygon.setPoint(e.getX(), e.getY());
                    }
                    open = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)){
                    polygon.setPoint(e.getX(), e.getY());
                    open = false;
                    drawLine();
                    update();
                }if (SwingUtilities.isRightMouseButton(e)){
                      dashedLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xf00f0f);
                }
                update();
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //x = e.getX();
                        //y = e.getY();
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                    }
               update();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                   drawLine();
                    if (open){
                        drawLine();
                        if (polygon.getSize() > 0 ){
                            i = polygon.getSize();
                            dottedLineRasterizer.rasterize(polygon.getPointX(i-1), polygon.getPointY(i-1),e.getX(),e.getY(),0xf00f0f );
                            dottedLineRasterizer.rasterize(polygon.getPointX(0), polygon.getPointY(0),e.getX(),e.getY(),0xf00f0f );
                        }
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    panel.clear();
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
                    polygon.clearPoints();
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

    private void drawLine(){
        raster.clear();
        if (polygon.getSize() > 1){
            for (int i = 0; i < polygon.getSize(); i++){
                trivialLineRasterizer.rasterize(polygon.getPointX((i+1) % polygon.getSize()), polygon.getPointY((i+1) % polygon.getSize()), polygon.getPointX(i ), polygon.getPointY(i),0xffffff );
            }
        }
    }

    private void update() {
        panel.repaint();
    }

    private void hardClear() {
        panel.clear();
    }

}
