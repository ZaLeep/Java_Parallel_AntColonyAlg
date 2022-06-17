import java.util.Random;

public class Land {
    float[][] feramones;
    int[][] weights;
    int n;

    public Land(int n) {
        this.n = n;
        weights = new int[n][n];
        weightInit();
        feramones = new float[n][n];
        feramonesInit();
    }

    private void weightInit() {
        int bound = (n >= 80) ? (int)((float)n / 4.0f) : 20;
        Random rand = new Random(System.currentTimeMillis());
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(i == j)
                    weights[i][j] = 0;
                else {
                    weights[i][j] = rand.nextInt(1, bound);
                }
            }
        }
    }

    private void feramonesInit() {
        Random rand = new Random(System.currentTimeMillis());
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(i == j)
                    feramones[i][j] = 0;
                else {
                    feramones[i][j] = rand.nextFloat(0.0f, 0.5f);
                    //feramones[j][i] = feramones[i][j];
                }
            }
        }
    }

    public String toString() {
        String res = "";
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                res += String.format("%2d", weights[i][j]);
                res += " ";
            }
            res += "   ";
            for(int j = 0; j < n; j++) {
                res += String.format("%1.3f", feramones[i][j]);
                res += " ";
            }
            res += "\n";
        }
        return res + "\n";
    }

    public void updateFeramones(float p) {
        float q = 1 - p;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                feramones[i][j] *= q;
                if(feramones[i][j] < 0.00001f)
                    feramones[i][j] = 0.00001f;
            }
        }
    }
}
