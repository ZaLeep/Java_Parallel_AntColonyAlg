import java.util.Random;

public class Swarm implements ISwarm {
    private Random rand = new Random(System.currentTimeMillis());
    final long LIMIT_TIME;
    private Ant[] swarm;

    float Alpha = 4, Beta = 2, P = 0.6f;
    double qualityResult;
    int LminStart, Lmin;
    long timeResult;
    int[] Pmin;
    Land l;

    public Swarm(int m, Land l) {
        if(l.n < 250)
            LIMIT_TIME = 20;
        else
            LIMIT_TIME = (int)(30 * m * ((double)l.n / 500));
        swarm = new Ant[m];
        for(int i = 0; i < m; i++)
            swarm[i] = new Ant(this);

        this.l = l;
        if(l.n >= 50)
            greedSearch();
        else {   
            Pmin = new int[]{};
            Lmin = Integer.MAX_VALUE;
        }
        LminStart = Lmin;
    }

    public void search() {
        long timeStart = System.currentTimeMillis();
        long timeFinal = System.currentTimeMillis();
        boolean firstUpgradeFlag = true;
        while(System.currentTimeMillis() - timeFinal < LIMIT_TIME || firstUpgradeFlag) {
            for (Ant a : swarm) {
                a.go(rand.nextInt(0, l.n));
                if(a.lenght < Lmin) {
                    firstUpgradeFlag = false;
                    Lmin = a.lenght;
                    Pmin = a.copyPath();
                    timeFinal = System.currentTimeMillis();
                }
            }
            for (Ant a : swarm) {
                for(int i = 0; i < a.path.length - 1; i++) {
                    l.feramones[a.path[i]][a.path[i + 1]] += (float)(a.lenght)/Lmin;
                }
            }
            l.updateFeramones(P);
        }
        timeResult = timeFinal - timeStart;
        qualityResult = (double)LminStart / (double)Lmin;
    }

    private void greedSearch() {
        int[] path = new int[l.n + 1];
        boolean[] checkList = new boolean[l.n];
        int currPoint = rand.nextInt(0, l.n);
        for(int i = 0; i < l.n - 1; i++) {
            path[i] = currPoint;
            checkList[currPoint] = true;

            currPoint = minIndex(currPoint, checkList);
        }
        path[path.length - 2] = currPoint;
        checkList[currPoint] = true;
        path[path.length - 1] = path[0];
        Pmin = path;
        Lmin = sum(path);
    }
    private int minIndex(int from, boolean[] checkList) {
        int min = Integer.MAX_VALUE, ind = -1;
        for(int i = 0; i < l.n; i++) {
            if(!checkList[i] && l.weights[from][i] < min) {
                min = l.weights[from][i];
                ind = i;
            }
        }
        if(ind == -1)
            System.out.println("Unexpected error with finding minimum(");
        return ind;
    }
    private int sum(int[] path) {
        int sum = 0;
        for(int i = 0; i < path.length - 1; i++) {
            sum += l.weights[path[i]][path[i + 1]];
        }
        return sum;
    }

    
    public double getQR() { return qualityResult; }
    public long getTR() { return timeResult; }
    public int getLmin() { return Lmin; }
    public Land getLand() { return l; }
    public String pathToString() {
        String path = "";
        for(int p = 0; p < Pmin.length - 1; p++)
            path += ((Pmin[p] + 1) + "->");
        path += Pmin[Pmin.length - 1] + 1;
        return path;
    }
}