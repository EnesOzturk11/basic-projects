import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 * the shortest path from a city to another city by showing on the Turkey map
 * @author enes.ozturk */
public class EnesOzturk {
    public static void main(String[] args) throws FileNotFoundException {
        int width=2377/2;
        int heigth=1055/2;

        StdDraw.setCanvasSize(width,heigth); //set canvas
        StdDraw.setXscale(0,2377); //scale
        StdDraw.setYscale(0,1055); //scale

        StdDraw.picture(2377/2.0,1055/2.0,"./src/map.png",2377,1055); //locate the Turkiye map

        File file=new File("./src/city_coordinates.txt"); //open coordinates file
        Scanner inputFile=new Scanner(file);

        City[] cities=new City[81]; //class array
        ArrayList<String> citynames=new ArrayList<>(); //String arraylist which contains names of cities
        int counter=0;



        while (inputFile.hasNextLine()){ //reading datas from coordinate text
            String line=inputFile.nextLine();
            String[] splitLine=line.split(",");
            cities[counter]=new City(splitLine[0],Integer.parseInt(splitLine[1].strip()),Integer.parseInt(splitLine[2].strip()));
            citynames.add(splitLine[0]);
            counter++;
        }

        inputFile.close(); //close coordinates file

        file=new File("./src/city_connections.txt"); //open connections file
        inputFile=new Scanner(file);

        while (inputFile.hasNextLine()){  //read datas from connections text file
            String line=inputFile.nextLine();
            String[] lineSplit=line.split(",");
            String firstCity=lineSplit[0]; //first city name
            String secondCity=lineSplit[1]; //second city name
            int firstCityIndex=citynames.indexOf(firstCity); //first city's index at arraylist
            int secondCityIndex=citynames.indexOf(secondCity); //second city's index at arraylist
            double firstX=cities[firstCityIndex].x; //first city's x value
            double firstY=cities[firstCityIndex].y; //first city's y value
            double secondX=cities[secondCityIndex].x; //second city's x value
            double secondY=cities[secondCityIndex].y; //second city's y value

            cities[firstCityIndex].connections.add(secondCity);
            cities[secondCityIndex].connections.add(firstCity);


            StdDraw.setPenColor(StdDraw.GRAY);  //drawing lines between cities
            StdDraw.setPenRadius(0.001);
            StdDraw.line(firstX,firstY,secondX,secondY);

        }
        inputFile.close();  //close connections file

        Scanner scanner=new Scanner(System.in);  //input for starting point and end point
        String start;
        String end;
        ArrayList<String> path=new ArrayList<>();


        while (true){ //input taking for starting city
            System.out.print("Enter starting city: "); //Write input taking question
            start=scanner.nextLine();  //input taking

            if (citynames.contains(start))  //if input is valid pass the next input taking question
                break;
            System.out.printf("City named '%s' not found. Please enter a valid city name.",start);  // if input is invalid write error message to the console
            System.out.println();
        }

        while (true){  //input taking for destination city
            System.out.print("Enter destination city: "); //Write input taking question
            end=scanner.nextLine();   //input taking

            if (citynames.contains(end))
                break;
            System.out.printf("City named '%s' not found. Please enter a valid city name.",end);  // if input is invalid write error message to the console
            System.out.println();
        }

        double infinity=Double.MAX_VALUE; //max value that will use in the algorithm
        String[] previousCity=new String[81]; //array of previous city
        double[] distanceList=new double[81]; //array of the distance between starting city and final city
        ArrayList<String> visited=new ArrayList<>(); //array of path which is the shortest
        double number=-1.0; //default parameter for the minimum method

        Arrays.fill(distanceList,infinity); //fill distance array with max value
        Arrays.fill(previousCity,"None"); //fill previous array with none

        int startIndex= citynames.indexOf(start); //index of starting city in object array and citynames arraylist
        distanceList[startIndex]=0; //set the distance from starting city to starting city zero


        while (visited.size()!=81){ //while there are unvisited city
            int currentCityIndex=findIndex(distanceList,findmin(distanceList,number)); //find the min distance city' index from starting city
            if (findmin(distanceList,number)==infinity){ //there are no way condition
                break;
            }
            for (String neighbour:cities[currentCityIndex].connections){ //choosing current city's neighbours
                int neighbourIndex=citynames.indexOf(neighbour); //find it index
                if (!visited.contains(neighbour)){ //control whether the neighbour was visited
                    double distance=calculateDistance(cities[currentCityIndex].x,cities[currentCityIndex].y,cities[neighbourIndex].x,cities[neighbourIndex].y); //calculate the distance between neighbour and current city
                    if (distance+distanceList[currentCityIndex]<distanceList[neighbourIndex]){ //if result is less than previous distance
                        distanceList[neighbourIndex]=distance+distanceList[currentCityIndex]; //change the previous value with new distance value
                        previousCity[neighbourIndex]=citynames.get(currentCityIndex); //set the previous city
                    }
                }
            }
            visited.add(citynames.get(currentCityIndex)); //add the city to visited cities
            number=distanceList[currentCityIndex]; //previous min distance from starting city and a city in the city list
        }


        String currentCityName=end; //variable to find path from reverse
        String fixedEnd=end; //final city that can't be changed

        if (distanceList[citynames.indexOf(fixedEnd)]==infinity){ //condition when there is no way between starting city and final city
            System.out.println("There is no way from "+start+" to "+fixedEnd);
            System.exit(0); //finish run the cod
        }


        while (!citynames.get(startIndex).equals(currentCityName)){ // while loop for assigning the path into an arraylist
            path.add(0,currentCityName); //adding current city
            currentCityName=previousCity[citynames.indexOf(end)]; //setting the new current city
            end=currentCityName;
        }
        path.add(0,start);



        //Drawing part
        String startCity=start;
        for (String city:path){ //drawing part the shortest path on the map
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE); //set pen color
            StdDraw.setFont(new Font("Helvetica",Font.PLAIN,12)); //set city name fon
            StdDraw.text(cities[citynames.indexOf(city)].x,cities[citynames.indexOf(city)].y+12,city); //set city name position
            StdDraw.filledCircle(cities[citynames.indexOf(city)].x,cities[citynames.indexOf(city)].y,5.0); //the point is under the city name
            StdDraw.setPenRadius(0.005);
            StdDraw.line(cities[citynames.indexOf(startCity)].x,cities[citynames.indexOf(startCity)].y,cities[citynames.indexOf(city)].x,cities[citynames.indexOf(city)].y); //line between two neighbour cities
            startCity=city;
        }

        //Console output part
        System.out.printf("Total Distance: %.2f",distanceList[citynames.indexOf(fixedEnd)]); //write the total distance
        System.out.print(". Path:"); //write the path
        for (String city:path){ //take cities names from the arraylist
            if (city.equals(startCity)){ //end condition of the for loop
                System.out.print(city);
            }
            else {
                System.out.print(city+"->"); //write the city name and -> sign
            }
        }

    }
/**
 * find the distance
 * @param x1: first city's x coordinate
 * @param x2: second city's x coordinate
 * @param y1: first  city's y coordinate
 * @param y2: second city's y coordinate
 * @return distance between first and second city*/
    public static double calculateDistance(int x1, int y1, int x2, int y2){
        double result=Math.pow((x1-x2),2)+Math.pow((y1-y2),2); //pisagor
        return Math.sqrt(result);
    }
/**
 * find the min value greater than previous min of the array
 * @param array: distancelist
 * @param number: previous min value of the distancelist
 * @return min value*/
    public static double findmin(double[] array, double number){
        double min = Double.MAX_VALUE; //default value to compare with distance at first
        for (int i = 0; i < array.length; i++) { //find the min value but greater than number parameter which is the previous min value of the array
            if (array[i] < min && array[i] > number) {
                min = array[i];
            }
        }
        return min; //return min value
    }
 /**
  * find the index of the min value of the array
  * @param array: distancelist
  * @param element: min value of the array
  * @return index of the min value*/
    public static int findIndex(double[] array, double element){
        for (int i=0; i<array.length; i++){ //search the min value is at which index
            if (array[i]==element){
                return i; //return index
            }
        }
        return -1; //if method couldn't find the min value's index return -1
    }

}
