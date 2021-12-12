package control;

import model3D.Cube;
import model3D.Pyramid;
import model3D.Scene;
import rasterize.Raster;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller3D implements Controller{

    private final Camera camera;
    private final Mat4 projection;
    private Mat4 model;
    private Raster raster;
    private GPURenderer renderer;
    private Scene mainScene, axisScene;

    public Controller3D(Panel panel) {
        this.raster = panel.getRaster();
        this.renderer = new Renderer3D(raster);
        initListeners(panel);
// 9,-13,0,90,/-15
        camera = new Camera().withPosition(new Vec3D(9,-13,0)).withAzimuth(Math.toRadians(90)).withZenith(-15);

        projection = new Mat4PerspRH(
                Math.PI /3,
                raster.getHeight() / (float) raster.getWidth(),
                0.1,
                50);




        mainScene = new Scene();
        mainScene.getSolids().add(new Cube(0xf00f0f));
        mainScene.getSolids().add(new Pyramid(0x11F314));
        display();

    }

    private void display() {
        raster.clear();
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);
        renderer.draw(mainScene);

    }
    private void redraw() {
        raster.clear();
        renderer.setView(camera.getViewMatrix());
        renderer.draw(mainScene);
    }


    @Override
    public void initListeners(Panel panel) {
        //todo

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    //A rotation left
                    case 65 -> {
                        model = new Mat4RotZ(-0.042D);
                        renderer.setModel(model);
                    }
                    //D rotation right
                    case 68 -> {
                        renderer.setModel(new Mat4RotZ(0.042D));

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
                    }
                }
                redraw();
            }
        });
    }



}