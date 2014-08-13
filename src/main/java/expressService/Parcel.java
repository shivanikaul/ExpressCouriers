package expressService;

public class Parcel {
    public boolean isFragile() {
        return fragile;
    }

    public double getWeight() {
        return weight;
    }

    public States getState() {
        return state;
    }

    private boolean fragile;
    private double weight;
    private States state;

    public Parcel(boolean fragile,double weight,States state)
    {
        this.fragile = fragile;
        this.weight = weight;
        this.state = state;
    }
}
