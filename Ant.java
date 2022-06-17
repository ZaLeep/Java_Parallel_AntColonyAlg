import java.util.Random;

public class Ant {
    Random rand;
    Swarm home;
    int[] path;
    int lenght;

    public Ant(Swarm s) {
        home = s;
        rand = new Random(System.currentTimeMillis());
    }

    public void go(int start) {
        int[] path = new int[home.l.n + 1];
        boolean[] checkList = new boolean[home.l.n];
        int currPoint = start;
        for(int i = 0; i < home.l.n - 1; i++) {
            path[i] = currPoint;
            checkList[currPoint] = true;

            currPoint = nextPoint(currPoint, checkList);
        }
        path[path.length - 2] = currPoint;
        checkList[currPoint] = true;
        path[path.length - 1] = start;
        this.path = path;
        this.lenght = sum(path);
    }
    private int nextPoint(int currPoint, boolean[] checkList) {
        double[] chance = new double[home.l.n];
        double[] look = look(currPoint, checkList);
        double sum = sum(look);
        for(int i = 0; i < home.l.n; i++) {
            if(checkList[i])
                chance[i] = 0;
            else {
                chance[i] = look[i] / sum;
            }
        }
        return select(chance);
    }
    private double[] look(int from, boolean[] checkList) {
        double[] look = new double[home.l.n];
        for(int i = 0; i < home.l.n; i++) {
            if(i != from && !checkList[i]) {
                look[i] = Math.pow(home.l.feramones[from][i], home.Beta) *  Math.pow((1.0 / home.l.weights[from][i]), home.Alpha);
            }
        }
        return look;
    }
    private double sum(double[] l) {
        double sum = 0;
        for (double d : l)
            sum += d;
        return sum;
    }
    private int sum(int[] path) {
        int sum = 0;
        for(int i = 0; i < path.length - 1; i++) {
            sum += home.l.weights[path[i]][path[i + 1]];
        }
        return sum;
    }
    private int select(double[] chance) {
        double r = rand.nextDouble(0, 1);
        double sum = 0;
        for(int i = 0; i < home.l.n; i++) {
            if(r >= sum && r < sum + chance[i]) {
                return i;
            }
            else {
                sum += chance[i];
            }
        }
        System.out.println("Something wrong with selecting by chance(");
        return -1;
    }
    
    public int[] copyPath() {
        int[] copy = new int[path.length];
        for(int i = 0; i < path.length; i++) {
            copy[i] = path[i];
        }
        return copy;
    }
}
