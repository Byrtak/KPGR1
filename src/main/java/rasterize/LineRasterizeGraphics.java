package rasterize;

import java.awt.*;

public class LineRasterizeGraphics extends LineRasterizer{

    private final RasterBufferedImage rasterBI;

    public LineRasterizeGraphics(Raster raster) {
        super(raster);
        if (raster instanceof RasterBufferedImage) {
            this.rasterBI = ((RasterBufferedImage) raster);
        } else throw new RuntimeException("error");

    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {
        Graphics g =  rasterBI.getGraphics();
        g.setColor(new Color(color));
        g.drawLine(x1, y1, x2, y2);

    }
}
