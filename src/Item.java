package src;
public record Item(int x, int y) {
    
    @Override
    public String toString() {
        return "Ant: " + this.x + ", Label: " + this.y;
    }
}