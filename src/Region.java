package src;

public class Region {
  public static final int TRIANGULAR = 0;
  public static final int TRAPEZOIDAL = 1;

  double x0, x1, x2, x3, y = 1.0;
  int label;
  int shape;

  public Region(int label, double x0, double x1, double x3) { // Triangular
    this.label = label;
    this.x0 = x0;
    this.x1 = x1;
    this.x2 = x1;
    this.x3 = x3;
    this.shape = Region.TRIANGULAR;
  }

  public Region(int label, double x0, double x1, double x2, double x3) { // Trapezoidal
    this.label = label;
    this.x0 = x0;
    this.x1 = x1;
    this.x2 = x2;
    this.x3 = x3;
    this.shape = Region.TRAPEZOIDAL;
  }

  // Retorna o grau de pertinencia de um valor nessa função
  public double fuzzify(double X) {
    if (X >= x1 && X <= x2) {
      return y;
    }
    if ((X <= x0) || (X >= x3)) {
      return 0.0;
    }
    if (X < x1) {
      return (X - x0) * (y / (x1 - x0));
    }
    if (X > x2) {
      return (x3 - X) * (y / (x3 - x2));
    }
    throw new IllegalArgumentException("Valores inválidos para x0, x1, x2, x3 ou X.");
  }

  // Retorna uma cópia do objeto
  public Region clone() {
    return new Region(this.label, this.x0, this.x1, this.x2, this.x3, this.shape, this.y);
  }
  
  private Region(int label, double x0, double x1, double x2, double x3, int shape, double y) { // Clone
    this.label = label;
    this.x0 = x0;
    this.x1 = x1;
    this.x2 = x2;
    this.x3 = x3;
    this.shape = shape;
    this.y = y;
  }
}
