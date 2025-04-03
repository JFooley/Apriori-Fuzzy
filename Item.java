
// Realmente necessária?
public class Item {
    int variable, value;
    String label;
  
    public Item(int variable, int value, String label) {
      this.label = label;
      this.variable = variable;
      this.value = value;
    }

    public Item(int variable, int value) {
        this.label = "";
        this.variable = variable;
        this.value = value;
      }
  
    public boolean equalsTo(Item item) {
        if (this.variable == item.variable && this.value == item.value) {
            return true;
        } 
        return false;
    }
  }
