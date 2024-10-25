import java.awt.*;
/**
 * Nodes class contains a node's x,y,radius,num,color,index values*/
public class Nodes {
    private double x;
    private double y;
    private double radius;
    private int num;
    private Color color;
    private int index;



    public Nodes(double x, double y, int num){
        this.x=x;
        this.y=y;
        this.num=num;
        radius=0.022;
        index=num-1;
        if (num==1)
            color=StdDraw.PRINCETON_ORANGE;

        else
            color=StdDraw.LIGHT_GRAY;

    }



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getNum() {
        return num;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public int getIndex() {
        return index;
    }
}
