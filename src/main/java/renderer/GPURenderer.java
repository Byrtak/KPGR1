package renderer;

import model3D.Scene;
import transforms.Mat4;

public interface GPURenderer {

    void draw(Scene scene);
//    void drawAx(Scene scene);

    void setModel(Mat4 model); //modelovaci (kombinace scale, rotace, posunuti)

    void setView(Mat4 view); //pohledova

    void setProjection(Mat4 projection); // projekcni

    void resetMatrix();

}
