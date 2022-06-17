import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ModernSwarm implements ISwarm {
    private Random rand = new Random(System.currentTimeMillis());
    private final long LIMIT_TIME;
    private ModernAnt[] mSwarm;
    private int threadsCount;

    float Alpha = 4, Beta = 2, P = 0.6f;
    long timeFinal, timeResult;
    boolean firstUpgradeFlag;
    double qualityResult;
    int LminStart, Lmin;
    int[] Pmin;
    Land l;

    public ModernSwarm(int m, Land l, int threadsCount) {
        LIMIT_TIME = (l.n < 250) ? 100 : (int)(30 * m * ((double)l.n / 500));
        this.threadsCount = threadsCount;
        mSwarm = new ModernAnt[m];
        for(int i = 0; i < m; i++)
            mSwarm[i] = new ModernAnt(this);

        this.l = l;
        greedSearch();
        LminStart = Lmin;
    }

    public void search() {
        long timeStart = System.currentTimeMillis();
        timeFinal = System.currentTimeMillis();
        firstUpgradeFlag = true;
        while(System.currentTimeMillis() - timeFinal < LIMIT_TIME || firstUpgradeFlag) {
            ThreadPoolExecutor antExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(threadsCount);
            for (ModernAnt a : mSwarm) {
                a.setStart(rand.nextInt(0, l.n));
                antExecutor.execute(a);
            }

            try {
                do {
                    antExecutor.awaitTermination(75, TimeUnit.MILLISECONDS);
                } while (antExecutor.getActiveCount() > 0);
                antExecutor.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            l.updateFeramones(P);
        }
        timeResult = timeFinal - timeStart;
        qualityResult = (double)LminStart / (double)Lmin;
    }
    public synchronized void upgradeByAnt(ModernAnt a) {
        if(a.lenght < Lmin) {
            firstUpgradeFlag = false;
            Lmin = a.lenght;
            Pmin = a.copyPath();
            timeFinal = System.currentTimeMillis();
        }

    }
    public void updateByAnt(int[] path, float pLenght) {
        for(int i = 0; i < l.n; i++) {
            synchronized(l) {
                l.feramones[path[i]][path[i + 1]] += pLenght/Lmin;
            }
        }
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
