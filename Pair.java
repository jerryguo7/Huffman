/* Pair class
 * <Jerry Guo> <2019.11.13>
 */
public class Pair implements Comparable<Pair>{
    // declare all required fields
    private char value;
    private double prob;

    //constructor
    public Pair(char value, double prob) {
        this.value = value;
        this.prob = prob;
    }

    //getters
    public char getValue() {
        return this.value;
    }

    public double getProb() {
        return this.prob;
    }

    //setters
    public void setValue(char value) {
        this.value = value;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    //toString
    @Override
    public String toString() {
        return value + "\t\t" + prob;
    }

    /**
     The compareTo method overrides the compareTo method of the Comparable interface.
     */
    @Override
    public int compareTo(Pair p){
        return Double.compare(this.getProb(), p.getProb());
    }

}