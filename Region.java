public class Region {
  double x0, x1, x3, y = 1.0;
  String name;

  public Region() {
  };

  public Region(String name, double x0, double x1, double x3) {
    this.name = name;
    this.x0 = x0;
    this.x1 = x1;
    this.x3 = x3;
  }

  public double fuzzify(double X) {
    if (X == x1) { 
      return (1.0); 
    }

	if ( (X <= x0) || (X >= x3)) { 
      return (0.0); 
    }

    if (X < x1) {
      return ( (X - x0) * (y / (x1 - x0)));
    }

    if (X > x1) {
      return ( (x3 - X) * (y / (x3 - x1)));
    }

    return (y);

  }

  public Region clone(){
    Region d = new Region();
    d.x0 = this.x0;
    d.x1 = this.x1;
    d.x3 = this.x3;
    d.y = this.y;
    d.name = new String(this.name);

    return d;
  }

  public String getName(){
	  return (this.name);
  }
}
