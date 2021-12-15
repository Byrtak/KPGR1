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
                    renderer.setModel(new Mat4RotY(  (oY - nY) / (double) (raster.getHeight()))
                            .mul(new Mat4RotX( (oX - nX) / (double) (raster.getWidth()))));

                }
                redraw();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {

                    case 37->{

                        //                        renderer.setView(view);
                    }
                    case 38->{
                        camera.up(0.2f);
                    }
                    case 39->{
                        camera.right(0.2f);
                    }
                    case 40->{
                        camera.down(0.2f);
                    }
                    //1
                    case 49->{
                        Solid s = mainScene.getSolids().get(0);
                        s.setCanRotate();


                    }//2
                    case 50->{
                        Solid s = mainScene.getSolids().get(1);
                        s.setCanRotate();
                        if (!s.CanRotate()) {
                            renderer.setOldModel();
                        }

                    }
                    //A rotation left
                    case 65 -> {
//                        model = new Mat4RotZ(-0.042D);
//                        renderer.setModel(model);
                        for (Solid s : mainScene.getSolids()) {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4RotZ(-0.042D));
                            }
                        }
                    }
                    //D rotation right
                    case 68 -> {
                        for (Solid s : mainScene.getSolids()) {
                            if (s.CanRotate()){
                                renderer.setModel(new Mat4RotZ(0.042D));
                            }
                        }
                    }
                    //S rotation down
                    case 83 ->{
                        renderer.setModel(new Mat4RotY(0.042D));
                    }

                            //renderer.setProjection(new Mat4PerspRH(0.7853981633974483D, (double) ((float) raster.getHeight() / (float) raster.getWidth()), 0.01D, 10.0D));
                    //W rotation up
                    case 87 -> {
                        renderer.setModel(new Mat4RotY(-0.042D));
                    }
                    //Q scale +
                    case 81 -> {
                        renderer.setModel(new Mat4Scale(1.1D, 1.1D, 1.1D));
                    }
                    //E scale -
                    case 69 -> {
                        renderer.setModel(new Mat4Scale(0.9D, 0.9D, 0.9D));
                    }//r
                    case 82 -> {
                        renderer.setModel( new Mat4Transl(0.0D, -1.0D, 0.0D));
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