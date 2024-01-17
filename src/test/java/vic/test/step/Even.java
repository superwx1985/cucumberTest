package vic.test.step;

public class Even {
    private String s;
    private int i;

    public Even(String s) {
        this.s = s;
        this.i = Integer.parseInt(s);
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return "Even{" +
                "s='" + s + '\'' +
                ", i=" + i +
                '}';
    }
}
