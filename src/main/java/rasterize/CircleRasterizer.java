package rasterize;
public class CircleRasterizer extends LineRasterizer{


    public CircleRasterizer(Raster raster) {
        super(raster);
    }


    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {
        int r = (int)Math.round(Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));
        int x=0, y=r,d=3-(2*r);

        EightWaySymmetricPlot(x1,y1,x,y,color);
        while(x<=y) {

            if(d<=0)
            {
                d=d+(4*x)+6;
            }
            else
            {
                d=d+(4*x)-(4*y)+10;
                y=y-1;
            }
            x=x+1;
            EightWaySymmetricPlot(x1,y1,x,y,color);
        }
    }


    void  EightWaySymmetricPlot(int xc,int yc,int x,int y,int color)
    {

        raster.setPixel(x+xc,y+yc,0xf00f0f);
        raster.setPixel(x+xc,-y+yc,color);
        raster.setPixel(-x+xc,-y+yc,0xf00f0f);
        raster.setPixel(-x+xc,y+yc,color);
        raster.setPixel(y+xc,x+yc,0xf00f0f);
        raster.setPixel(y+xc,-x+yc,color);
        raster.setPixel(-y+xc,-x+yc,0xf00f0f);
        raster.setPixel(-y+xc,x+yc,color);
    }
}
