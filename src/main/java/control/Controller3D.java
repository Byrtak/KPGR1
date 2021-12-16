package control;

import model3D.*;
import rasterize.Raster;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.event.*;

public class Controller3D implements Controller{

    private final Camera camera ;
    private final Mat4 projection;
    private Mat4 model;
    private Mat4 view;
    private final Raster raster;
    private final GPURenderer renderer;
    private final Scene mainScene;
    private final Scene axisScene;
    double oX,oY,nX,nY;

    public Controller3D(Panel panel) {
        this.raster = panel.getRaster();
        this.renderer = new Renderer3D(raster);
        initListeners(panel);
// 9,-13,0,90,/-15
        camera = new Camera().withPosition(new Vec3D(35, 50, -5.0)).withAzimuth(Math.toRadians(210)).withZenith(-15);
        //camera = new Camera().withPosition(new Vec3D(10, -12, -12.0)).withAzimuth(Math.toRadians(10)).withZenith();

        projection = new Mat4PerspRH(
                Math.PI /3,
                raster.getHeight() / (float) raster.getWidth(),
                0.1,
                50);



        axisScene = new Scene();
        mainScene = new Scene();
        mainScene.getSolids().add(new Cube(0xf00f0f));
        mainScene.getSolids().add(new Pyramid(0xf30f7f));
        axisScene.getSolids().add(new AxisLineX());
        axisScene.getSolids().add(new AxisLineY());
        axisScene.getSolids().add(new AxisLineZ());
        display();

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
//        renderer.setView(view);
        renderer.draw(mainScene);
        renderer.draw(axisScene);
    }

    private void reset(){
        raster.clear();
        renderer.resetMatrix();
        renderer.draw(mainScene);
    }


    @Override
    public void initListeners(Panel panel) {
        //todo
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
                nX = e.getY();
                nY = e.getX();
                if (SwingUtilities.isRightMouseButton(e)){
                    renderer.setModel(new Mat4RotY(Math.PI *  (oY - nY) / (double) (raster.getHeight()))
                            .mul(new Mat4RotX(Math.PI *  (oX - nX) / (double) (raster.getWidth()))));

                }
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
                                renderer.setModel1(new Mat4Transl(0.0D, 0.0D, 1.0D));;
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
                }

                switch (e.getKeyCode()) {

                    //1 cube
                    case 49->{
                        s.setCanRotate();
                    }//2 pyramid
                    case 50->{
                        s1.setCanRotate();

                    }
                    //A
                    case 65 -> {

                    }
                    //D
                    case 68 -> {

                    }
                    //S
                    case 83 ->{

                    }
                    //W
                    case 87 -> {

                    }
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

                    }//C clear/reset
                    case 67 ->{
                        reset();
                    }

                }
                redraw();
            }
        });
    }



}