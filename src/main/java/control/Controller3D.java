package control;

import model3D.*;
import rasterize.Raster;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Panel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller3D implements Controller{

    private Camera camera ;
    private Mat4 projection;
    private Mat4 view;
    private final Raster raster;
    private final GPURenderer renderer;
    private final Scene mainScene;
    private final Scene axisScene;
    double oX,oY,nX,nY;
    double speedW,speedA,speedS,speedD = 0.2D;
    double sX, sY;

    public Controller3D(Panel panel) {
        this.raster = panel.getRaster();
        this.renderer = new Renderer3D(raster);



        camera = new Camera().withPosition(new Vec3D(35, 50, -15.0)).withAzimuth(Math.toRadians(210)).withZenith(-15).withRadius(30D);
        view = camera.getViewMatrix();


        projection = new Mat4PerspRH(
                0.77D,
                (double) raster.getHeight() / (double) raster.getWidth(),
                0.8D,
                10D);

        axisScene = new Scene();
        mainScene = new Scene();
        initObjects();
        initListeners(panel);
        display();
    }
    private void initObjects(){

        mainScene.getSolids().add(new Octagon(Color.YELLOW.getRGB()));//obj 0
        mainScene.getSolids().add(new Pyramid(0xf30f7f));//obj 1

        mainScene.getSolids().add(new Ferguson(0xf14f71));
        mainScene.getSolids().add(new Coon(Color.CYAN.getRGB()));
        mainScene.getSolids().add(new Bezier(Color.orange.getRGB()));

        axisScene.getSolids().add(new AxisLineX());
        axisScene.getSolids().add(new AxisLineY());
        axisScene.getSolids().add(new AxisLineZ());

    }

    private void display() {
        raster.clear();
        for (Solid s : mainScene.getSolids()) {
            s.setCanRotate();
        }
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);
        renderer.draw(mainScene);
        renderer.draw(axisScene);

    }
    private void redraw() {
        raster.clear();
        renderer.draw(mainScene);
        renderer.draw(axisScene);

    }

    private void reset(){
        raster.clear();
        speedW = 0.2d;
        speedA =  speedW;
        speedD = 0.2d;
        speedS = 0.2d;
        renderer.resetMatrix();

        renderer.setProjection(new Mat4PerspRH(
                0.77D,
                (double) raster.getHeight() / (double) raster.getWidth(),
                0.8D,
                100D));
        camera = new Camera().withPosition(new Vec3D(35, 50, -15.0)).withAzimuth(Math.toRadians(210)).withZenith(-15).withRadius(30D);

        renderer.setView(view = camera.getViewMatrix());
        renderer.draw(mainScene);
    }


    @Override
    public void initListeners(Panel panel) {

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oY=e.getY();
                oX=e.getX();
                nY = oY;
                nX = oX;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                nX = e.getX();
                nY = e.getY();
                Solid s = mainScene.getSolids().get(0);
                Solid s1 = mainScene.getSolids().get(1);
                if (SwingUtilities.isRightMouseButton(e)){
                    if (s.CanRotate()){
                        renderer.setModel(new Mat4RotY(Math.PI * (nY - oY) / (double) raster.getHeight() )
                                .mul(new Mat4RotZ(Math.PI *  (nX - oX) / (double) (raster.getWidth()))));
                    }
                    if (s1.CanRotate()){
                        renderer.setModel1( new Mat4RotY(Math.PI * (nY - oY) / (double) raster.getHeight() )
                                .mul(new Mat4RotZ(Math.PI *  (nX - oX) / (double) raster.getWidth() )));
                    }
                }
                if (SwingUtilities.isLeftMouseButton(e)){
                    double dX = sX - nX;
                    camera = camera.addAzimuth( (dX / 1000) );
                    renderer.setView(camera.getViewMatrix());

                }

                sX = nX;
                sY = nY;

                redraw();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Solid s = mainScene.getSolids().get(0);
                Solid s1 = mainScene.getSolids().get(1);


                if (e.isShiftDown()){
                    switch (e.getKeyCode()){
                        /*====================Transformation of translation===============*/
                        //Shift+ PgUp button
                        case 33->{
                            if (s.CanRotate()){
                                renderer.setModel( new Mat4Transl(1.0D, 0.0D, 0.0D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1( new Mat4Transl(1.0D, 0.0D, 0.0D));
                            }
                        }
                        //Shift+ PgDn button
                        case 34->{
                            if (s.CanRotate()){
                                renderer.setModel( new Mat4Transl(-1.0D, 0.0D, 0.0D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1( new Mat4Transl(-1.0D, 0.0D, 0.0D));
                            }
                        }
                        //Shift+ left Arrow
                        case 37->{
                            if (s.CanRotate()){
                                renderer.setModel( new Mat4Transl(0.0D, 1.0D, 0.0D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1( new Mat4Transl(0.0D, 1.0D, 0.0D));
                            }
                        }
                        //Shift+ up Arrow
                        case 38->{
                            if (s.CanRotate()){
                                renderer.setModel( new Mat4Transl(0.0D, 0.0D, 1.0D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4Transl(0.0D, 0.0D, 1.0D));
                            }
                        }
                        //Shift+ right Arrow
                        case 39->{
                            if (s.CanRotate()){
                                renderer.setModel( new Mat4Transl(0.0D, -1.0D, 0.0D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4Transl(0.0D, -1.0D, 0.0D));
                            }
                        }
                        //Shift+ down Arrow
                        case 40->{
                            if (s.CanRotate()){
                                renderer.setModel( new Mat4Transl(0.0D, 0.0D, -1.0D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1( new Mat4Transl(0.0D, 0.0D, -1.0D));
                            }
                        }
                        /*================================================================*/

                        /*==============Transformation of rotation========================*/
                        //Shift+A Z-rotation left
                        case 65 -> {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4RotZ(-0.042D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4RotZ(-0.042D));
                            }
                        }
                        //Shift+D Z-rotation right
                        case 68 -> {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4RotZ(0.042D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4RotZ(0.042D));
                            }
                        }
                        //Shift+S Y-rotation down
                        case 83 ->{
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4RotY(0.042D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4RotY(0.042D));
                            }
                        }
                        //Shift+W Y-rotation up
                        case 87 -> {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4RotY(-0.042D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4RotY(-0.042D));
                            }
                        }
                        //Shift+Q X-rotation
                        case 81 -> {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4RotX(0.042D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4RotX(0.042D));
                            }
                        }
                        //Shift+E X-rotation
                        case 69 -> {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4RotX(-0.042D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4RotX(-0.042D));
                            }
                        }
                        /*================================================================*/
                    }
                }else {
                    switch (e.getKeyCode()) {

                        //1 cube
                        case 49-> s.setCanRotate();
                        //2 pyramid
                        case 50-> s1.setCanRotate();
                        //3
                        case 51->{
                            projection = new Mat4OrthoRH((double) raster.getHeight()/ 20,(double) raster.getWidth()/20,0.1D,100D);
                            renderer.setProjection(projection);

                        }
                        /*=================Camera==================================*/
                        //A
                        case 65 -> {
                            view=  camera.left(speedA).getViewMatrix();
                            renderer.setView(view);
                            speedA += 0.2D;
                        }
                        //D
                        case 68 -> {
                            view=  camera.right(speedD).getViewMatrix();
                            renderer.setView(view);
                            speedD += 0.2D;
                        }
                        //S
                        case 83 ->{
                            renderer.setView( camera.forward(speedS).getViewMatrix());
                            speedS += 0.2D;
                        }
                        //W
                        case 87 -> {
                            renderer.setView(camera.backward(speedW).getViewMatrix());
                            speedW += 0.2D;
                        }
                        /*================================================================*/

                        /*==============Transformation of scale========================*/
                        //Q scale +
                        case 81 -> {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4Scale(1.1D, 1.1D, 1.1D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4Scale(1.1D, 1.1D, 1.1D));
                            }
                        }
                        //E scale -
                        case 69 -> {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4Scale(0.9D, 0.9D, 0.9D));
                            }
                            if (s1.CanRotate()){
                                renderer.setModel1(new Mat4Scale(0.9D, 0.9D, 0.9D));
                            }
                        }
                        /*================================================================*/

                        //r
                        case 82 -> {

                        }
                        //C clear/reset
                        case 67 -> reset();

                    }
                }


                redraw();
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects();
                display();
                redraw();
            }
        });

    }



}