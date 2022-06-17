public class Main {
    public static void main(String[] args) {
        Menu.mainMenu();
    }

    private static void tetsing() {
        boolean isParallel = false;
        int threadCount = 8;
        int attemptCount = 10;
        for(int n = 250; n <= 250; n += 250) {
            for(int m = 5; m <= 50; m += 5) {
                ISwarm preSwarm = (isParallel) ? new ModernSwarm(m, new Land(n), threadCount) : new Swarm(m, new Land(n));
                preSwarm.search();
                long sumTime = 0;
                double sumQuality = 0;
                for(int i = 0; i < attemptCount; i++) {
                    ISwarm swarm = (isParallel) ? new ModernSwarm(m, new Land(n), threadCount) : new Swarm(m, new Land(n));
                    swarm.search();
                    sumTime += swarm.getTR();
                    sumQuality += swarm.getQR();
                }
                long avgTime = sumTime / attemptCount;
                double avgQuality = sumQuality / attemptCount;
                System.out.println("City count: " + n + " Ant count: " + m);
                System.out.println("Average time: " + avgTime + "ms");
                System.out.println("Average quality: " + avgQuality);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
        }
    }
}