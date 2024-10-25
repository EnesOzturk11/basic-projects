import java.util.ArrayList;
import java.util.Random;
/**
 * Ant class contains an ant's pheromone and some constant
 * Class contains best route and min distance of it*/
public class Ant {
    public Random random=new Random();
    public static ArrayList<Nodes> antBestRoute;
    public static double antRouteMinDistance=Double.MAX_VALUE;
    private double pheromone=1.2;
    public static double alpha=1.0;
    public static double beta=1.6;

    public Ant(){}


    public double getPheromone() {
        return pheromone;
    }
    /**
     * Update pheromone intensity of an edge
     * @param edgeDistance: length of the edge
     * @param pheromoneIntensity: Pheromone intensity of the edge*/
    private double computeEdgeValue(double pheromoneIntensity, double edgeDistance) {
        return Math.pow(pheromoneIntensity, alpha) * Math.pow(1.0 / edgeDistance, beta);
    }
    /**
     * Find the next point for the ant from all possibilities
     * @param allNodes: the array includes all nodes
     * @param currentPoint: current node
     * @param distanceMatrix: distance matrix
     * @param pheromoneMatrix: pheromone matrix
     * @param visitedNodes: arraylist contains visited nodes*/
    public int chooseNextPoint(Nodes currentPoint,Nodes[] allNodes,double[][] pheromoneMatrix, double[][] distanceMatrix,ArrayList<Nodes> visitedNodes) {
        ArrayList<Double> probabilities = new ArrayList<>();
        double totalValue = 0.0;
        for (Nodes node : allNodes) {
            if (!visitedNodes.contains(node)) {
                int nextPointIdx = node.getIndex();
                double edgeValue = computeEdgeValue(pheromoneMatrix[currentPoint.getIndex()][nextPointIdx], distanceMatrix[currentPoint.getIndex()][nextPointIdx]);
                probabilities.add(edgeValue);
                totalValue += edgeValue;
            } else {
                probabilities.add(0.0); // Assign zero probability to visited nodes
            }
        }
        for (int i = 0; i < probabilities.size(); i++) {
            probabilities.set(i, probabilities.get(i) / totalValue);
        }
        // Choose next node probabilistically
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < probabilities.size(); i++) {
            cumulativeProbability += probabilities.get(i);
            if (randomValue <= cumulativeProbability) {
                return i;
            }
        }
        System.out.println(probabilities);
        // This should not happen, but in case of precision issues or other errors
        return -1;
    }


}
