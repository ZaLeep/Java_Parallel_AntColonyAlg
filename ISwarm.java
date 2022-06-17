public interface ISwarm {
    public abstract void search();
    public String pathToString();
    public double getQR();
    public long getTR();
    public int getLmin();
    public Land getLand();
}
