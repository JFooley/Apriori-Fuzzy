public class Item {
  int attribute, value;
  String label;

  public Item(int attribute, int value, String label) {
    this.label = label;
    this.attribute = attribute;
    this.value = value;
  }

  public String toString() {
    return label;
  }
}