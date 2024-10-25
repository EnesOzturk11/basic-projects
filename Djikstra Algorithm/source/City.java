import java.awt.*;
import java.util.ArrayList;

public class City {

    public String name; //name of the city object
    public int x; //x coordinate of the city object
    public int y; //y coordinate of the city object
    public ArrayList<String> connections=new ArrayList<>(); //connections of the city object


    public City(String cityName, int x, int y){
        name=cityName;
        this.x=x;
        this.y=y;

        //drawing part the city on Turkey map
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledCircle(x,y,5.0);
        StdDraw.setFont(new Font("Helvetica",Font.PLAIN,12));
        StdDraw.text(x,y+12,name);
    }

}
