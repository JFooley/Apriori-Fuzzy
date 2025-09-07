package src;
public record Item(int variable, int label) {
    
    @Override
    public String toString() {
        return "Ant: " + this.variable + ", Label: " + this.label;
    }
}