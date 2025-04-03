public class Classic implements Logic{
    String name;
    String value;
  
    public Classic() {
    }
  
    public Classic(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public double Fuzzifica(String X) {
        if (this.value.equals(X)) {
            return 1;
        } else {
            return 0;
        }
    }
  
    public Classic clone(){
        Classic d = new Classic();
        d.name = new String(this.name);
        d.value = this.value;
  
        return d;
    }
  
    @Override
    public String getName(){
        return (this.name);
    }
}
