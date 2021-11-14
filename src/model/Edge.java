package model;


public class Edge {

    private int x1, y1, x2, y2;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Zjistí, zda je hrana vodorovná
     *
     * @return true pokud neni vodorovná, jinak false
     */
    public boolean isHorizontal() {
        // TODO test na rovnost mezi y1 a y2
        return y1 != y2;
    }

    /**
     * Zorientuje hranu odshora dolů
     */
    public void orientate() {
        // TODO prohození hodnot, pokud y1 je větší než y2
        int  tempx, tempy =0;
        tempx = x1;
        tempy = y1;
        x1=x2;
        y1=y2;
        x2=tempx;
        y2= tempy;
        }

    /**
     * Zjistí, zda existuje průsečík scan-line s touto hranou
     *
     * @param y Y souřadnice vodorovné přímky (scan-line)
     * @return true pokud existuje průsečík
     */
    public boolean hasIntersection(int y) {
        // TODO y, y1,y2 - porovnat, zda je y v rozsahu ((y >= y1) && (y < y2))
        return ((y >= y1) && (y < y2));
    }

    /**
     * @param y Y souřadnice vodorovné přímky (scan-line)
     * @return vrátí X souřadnici průsečíku
     */
    public int getIntersection(int y) {
        // TODO vypočítat průsečík pomocí y, k, q (osa Y)
       float k = (float)(x2 - x1) / (y2 - y1);
       float q = x1 - k * y1;
        return (Math.round(k*y + q));
    }

    public boolean isInside(Point p) {
        // slide 23, přednáška C
        // tečný vektor
        Point t = new Point(x2 - x1, y2 - y1);

        // normálový vektor (záleží na orientaci polygonu)
        Point n = new Point(t.y, -t.x);
//        Point n = new Point(-t.y, t.x);

        // vektor k bodu
        Point v = new Point(p.x - x1, p.y - y1);

        return v.x * n.x + v.y * n.y < 0;
    }

    public Point getIntersection(Point p3, Point p4) {
        // slide 25, přednáška C
        int px = ((p3.x * p4.y - p3.y * p4.x) * (x1 - x2) - (x1 * y2 - y2 * x2) * (p3.x - p4.x)) / ((p3.x - p4.x) * (x1 - x2) - (x1 - x2) * (p3.y - p4.y));
        int py = ((p3.x * p4.y - p3.y * p4.x) * (x1 - x2) - (x1 * y2 - y2 * x2) * (p3.y - p4.y)) / ((p3.x - p4.x) * (x1 - x2) - (x1 - x2) * (p3.y - p4.y));
        return new Point(px, py);
    }

}
