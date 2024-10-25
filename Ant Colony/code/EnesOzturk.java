import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * The shortest cycle path from migros node to migros node by using two different approach
 * @author enes.ozturk
 */
public class EnesOzturk {
    private static double minDistance=Double.MAX_VALUE;

    public static void main(String[] args) throws FileNotFoundException {
        int width=800;
        int height=800;

        String shortestPathText="[";
        double time;
        int chosenMethod=2;  //if number is 1, method is brute force; else method is ant-colony
        int shortestOrPheromone=1; //if number is 1, draw shortest path; else draw pheromone lines

        //Set Canvas
        StdDraw.setCanvasSize(width,height);
        StdDraw.setXscale(0.0,1.0);
        StdDraw.setYscale(0.0,1.0);

        //Input reading
        File file=new File("./src/input05.txt");
        Scanner inputFile=new Scanner(file);

        ArrayList<Nodes> nodesArrayList=new ArrayList<>(); //all nodes are in this arraylist
        int num=1;

        //Input file reading
        while (inputFile.hasNextLine()){
            String line=inputFile.nextLine();
            String[] splitLine=line.split(",");
            double x=Double.parseDouble(splitLine[0]);
            double y=Double.parseDouble(splitLine[1]);
            nodesArrayList.add(new Nodes(x,y,num));
            num++;
        }
        Nodes[] nodesArray = new Nodes[nodesArrayList.size()]; //All nodes are in this array
        for(int i = 0; i<nodesArrayList.size();i++){
            nodesArray[i] =nodesArrayList.get(i);
        }

        Nodes[] route=new Nodes[nodesArrayList.size()-1]; //Route array for brute force
        long startTime;
        long endTime;
        Nodes migros=nodesArrayList.get(0);

        //--------------------------Brute-Force Approach------------------------------
        if (chosenMethod==1){
            ArrayList<Nodes> nodesArrayListExcludingMigros=new ArrayList<>(nodesArrayList); //Arraylist of nodes but exclude migros
            nodesArrayListExcludingMigros.remove(0);

            Nodes[] allNodesArray= nodesArrayListExcludingMigros.toArray(new Nodes[nodesArrayList.size()-1]);

            startTime= System.currentTimeMillis();  //start the time
            route=permute(route,allNodesArray,0,migros);

            endTime=System.currentTimeMillis();  //stop the time
            time=(endTime-startTime)/1000.0;

            //STD DRAW PART FOR BRUTE FORCE
            //Plot the final route
            StdDraw.enableDoubleBuffering();
            Nodes preNode=migros;
            Nodes currentNode=migros;
            for (Nodes node: route){
                currentNode=node;
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.line(preNode.getX(),preNode.getY(),currentNode.getX(),currentNode.getY());  //Draw line between current node and previous node
                preNode=currentNode;
            }
            StdDraw.line(currentNode.getX(),currentNode.getY(),migros.getX(),migros.getY());

            //Plot nodes
            for (Nodes node:nodesArrayList){
                StdDraw.setPenColor(node.getColor());
                StdDraw.filledCircle(node.getX(),node.getY(),node.getRadius());
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.text(node.getX(),node.getY(),(""+node.getNum()));
            }

            StdDraw.show();

            //Set shortestPathText
            shortestPathText+="1, ";
            for(Nodes part:route){
                shortestPathText+=(""+part.getNum()+", ");
            }
            shortestPathText+="1]";


            //Writing texts on the console
            System.out.println("Method: Brute-Force Method");
            System.out.printf("Shortest Distance: %.5f\n",minDistance);
            System.out.println("Shortest Path: "+shortestPathText);
            System.out.println("Time it takes to find the shortest path: "+time+" seconds.");

        }

        //----------------------Ant Colony Optimization Approach----------------------
        else {
            final double antCount =50;  //The number of ant
            final double iterationCount = 100;  //The number of iteration
            final double Q=0.0001;  //Constant about pheromone intensity
            final double degradationConstant=0.7;  //Degradation constant
            double[][] pheromoneMatrix = new double[nodesArrayList.size()][nodesArrayList.size()];  //Pheromone matrix
            double[][] distanceMatrix = new double[nodesArrayList.size()][nodesArrayList.size()];  //Distance matrix

            for(int i=0;i<nodesArrayList.size();i++ ){
                for(int j=0; j<nodesArrayList.size();j++){
                    pheromoneMatrix[i][j] = 1;
                    distanceMatrix[i][j] = calculateDistanceTwoNodes(i,j,nodesArray);
                }
            }
            startTime =System.currentTimeMillis();  //Start the time
            for (int i = 0; i < iterationCount; i++) {
                for (int j = 0; j < antCount; j++) {
                    Ant ant = new Ant();
                    ArrayList<Nodes> visitedPoints = new ArrayList<>();
                    Nodes currentPoint = nodesArrayList.get(0);  //Firstly,ant started at the migros node
                    visitedPoints.add(currentPoint);  //Adding current node into visitednodes arraylist
                    while (visitedPoints.size() < nodesArrayList.size()) {
                        int nextPointIdx = ant.chooseNextPoint(currentPoint, nodesArray, pheromoneMatrix, distanceMatrix, visitedPoints);  //Ant choose next node by using method
                        Nodes nextPoint = nodesArrayList.get(nextPointIdx);
                        visitedPoints.add(nextPoint);
                        currentPoint = nextPoint;
                    }
                    visitedPoints.add(nodesArrayList.get(0));
                    if (Ant.antRouteMinDistance > calculateRouteDist(visitedPoints)) {
                        Ant.antRouteMinDistance = calculateRouteDist(visitedPoints);  //Changing the shortest route with previous shortest route
                        ant.antBestRoute = visitedPoints;
                    }
                    for (int k = 0; k < visitedPoints.size() - 1; k++) {
                        int currentIdx = visitedPoints.get(k).getIndex();
                        int nextIdx = visitedPoints.get(k + 1).getIndex();
                        pheromoneMatrix[currentIdx][nextIdx] += Q / calculateRouteDist(visitedPoints);  //Updating pheromone intensity of edges
                    }
                }
                for(int m = 0;m<nodesArrayList.size();m++){
                    for(int n = 0;n<nodesArrayList.size();n++){
                        pheromoneMatrix[m][n] *= degradationConstant;  //Updating pheromone intensity of edges due to vaporization
                    }
                }
            }
            endTime = System.currentTimeMillis();  //Stop the time
            time = (endTime-startTime)/1000.0;


            //SETTİNG TEXT
            ArrayList<Integer> bestRoute=new ArrayList<>();
            for (Nodes node: Ant.antBestRoute){
                bestRoute.add(node.getNum());
            }
            System.out.println("Method: Ant Colony Optimization");
            System.out.printf("Shortest Distance: %.5f\n",Ant.antRouteMinDistance);
            System.out.println("Shortest Path: "+bestRoute);
            System.out.println("Time it takes to find the shortest path: "+time+" seconds.");

            //STD DRAW PART FOR ANT COLONY OPTİMİZATİON
            StdDraw.enableDoubleBuffering();
            if (shortestOrPheromone==1){
                //Plot Lines
                Nodes preNode=Ant.antBestRoute.get(0);
                for(Nodes node:Ant.antBestRoute){
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.line(node.getX(),node.getY(),preNode.getX(),preNode.getY());  //Draw line between current node and previous node
                    preNode=node;
                }

                //Plot Nodes
                for(Nodes node:nodesArrayList){
                    StdDraw.setPenColor(node.getColor());
                    StdDraw.filledCircle(node.getX(),node.getY(),node.getRadius());
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.text(node.getX(),node.getY(),node.getNum()+"");
                }
                StdDraw.show();
            }

            else {
                //Plot Pheromone Lines
                int iterationSize=nodesArrayList.size();
                double radiusConstant=5;
                for(int i=0; i<iterationSize; i++){
                    for(int j=0; j<iterationSize; j++){
                        double pheromoneIntensity=pheromoneMatrix[i][j];
                        Nodes startNode=nodesArrayList.get(i);
                        Nodes endNode=nodesArrayList.get(j);
                        StdDraw.setPenColor(StdDraw.BLACK);
                        StdDraw.setPenRadius(pheromoneIntensity*radiusConstant);
                        StdDraw.line(startNode.getX(),startNode.getY(),endNode.getX(),endNode.getY());
                    }
                }
                //Plot Nodes
                for(Nodes node:nodesArrayList){
                    if (node.getIndex()==0)  //Migros' color is gray in the pheromone map
                        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                    else
                        StdDraw.setPenColor(node.getColor());
                    StdDraw.filledCircle(node.getX(),node.getY(),node.getRadius());
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.text(node.getX(),node.getY(),node.getNum()+"");
                }
                StdDraw.show();
            }

        }

    }
    /**
     * Calculate the distance of the brute force route
     * @param route: The result of the min distance permutation
     * @param migros: Migros node*/
    public static double calculateRouteDistance(Nodes[] route, Nodes migros) {
        double distance = 0;
        Nodes prevNode = migros;
        for (Nodes node : route) {
            distance += Math.sqrt(Math.pow(node.getX()-prevNode.getX(),2)+Math.pow(node.getY()-prevNode.getY(),2));
            prevNode = node;
        }
        distance += Math.sqrt(Math.pow(migros.getX() - prevNode.getX(), 2) +
                Math.pow(migros.getY() -prevNode.getY(),2));
        return distance;
    }
    /**
     * Permutation method that find the shortest distance from all permutations exclude migros node
     * @param bestRoute: result array
     * @param arr: contains permutation
     * @param k: control variable
     * @param migros: migros node*/
    public static Nodes[] permute(Nodes[] bestRoute, Nodes[] arr, int k, Nodes migros) {
        if (k == arr.length) {
            double distance = calculateRouteDistance(arr, migros);
            if (distance < minDistance) {
                minDistance = distance;
                System.arraycopy(arr, 0, bestRoute, 0, arr.length);
            }
        } else {
            for (int i = k; i < arr.length; i++) {
                Nodes temp = arr[i];
                arr[i] = arr[k];
                arr[k] = temp;
                permute(bestRoute, arr, k + 1,migros);
                temp = arr[k];
                arr[k] = arr[i];
                arr[i] = temp;
            }
        }
        return bestRoute;
    }
    /**
     * Calculate the distance between two nodes
     * @param i: first node index
     * @param j: second node index
     * @param nodesArray: Array that contains all nodes*/
    public static double calculateDistanceTwoNodes(int i, int j, Nodes[] nodesArray){
        return Math.sqrt(Math.pow(nodesArray[j].getX() - nodesArray[i].getX(), 2) + Math.pow(nodesArray[j].getY() - nodesArray[i].getY(), 2));
    }
    /**
     * Calculate the route distance for brute-force method
     * @param route: Arraylist that contains objects of the shortest path*/
    public static double calculateRouteDist(ArrayList<Nodes> route) {
        double distance = 0;
        for (int i = 0; i < route.size()-1; i++) {
            distance = distance + Math.sqrt(Math.pow(route.get(i).getX()- route.get(i+1).getX(), 2) + Math.pow(route.get(i).getY() - route.get(i+1).getY(), 2));
        }
        return distance;
    }
}
