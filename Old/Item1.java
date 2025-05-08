public class Item1 {
  int variable, value;
  String label;

  public Item1(int variable, int value, String label) {
    this.label = label;
    this.variable = variable;
    this.value = value;
  }

  public Item1(int variable, int value) {
      this.label = "";
      this.variable = variable;
      this.value = value;
    }

  public boolean equalsTo(Item1 item) {
      if (this.variable == item.variable && this.value == item.value) {
          return true;
      } 
      return false;
  }
}
