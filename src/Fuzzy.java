public class Fuzzy implements Logic{
  double x0, x1, x3, y;
  String name;

  public Fuzzy() {
  }

  public Fuzzy(String name, float x0, float x1, float x3) {
    this.name = name;
    this.x0 = x0;
    this.x1 = x1;
    this.x3 = x3;
  }

  @Override
  public double Fuzzifica(String strX) {
    Double X = Double.parseDouble(strX);

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

  public Fuzzy clone(){
    Fuzzy d = new Fuzzy();
    d.x0 = this.x0;
    d.x1 = this.x1;
    d.x3 = this.x3;
    d.y = this.y;
    d.name = new String(this.name);

    return d;
  }

  @Override
  public String getName(){
	  return (this.name);
  }
}
