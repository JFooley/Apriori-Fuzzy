public class MemberFunction {
  String name;
  Logic[] regions; // Contem todas as regiões (seja fuzzy ou classica) da função de pertinencia
  int size = 0;

  // Cria uma função de pertinencia pra logica fuzzy
  public MemberFunction(String name, int membershipFunctionSize, double upperLimit, double lowerLimit) {
    this.size = membershipFunctionSize;
    this.name = name;

    // ########## VER COMO DEFINIR AUTOMATICAMENTE AS REGIÕES ########## 
    float x0 = 0, x1 = 0, x3 = 0;

    regions = new Fuzzy[this.size];
    for (int i = 0; i < this.size; i++) {
      Fuzzy object = new Fuzzy(name + i + "/" + this.size, x0, x1, x3); 
      this.regions[i] = object;
    }
  }

  // Cria uma função de pertinencia pra logica classica
  public MemberFunction(String name, String[] values) {
    this.size = values.length;
    this.name = name;

    regions = new Classic[this.size];
    for (int i = 0; i < this.size; i++) {
      Classic object = new Classic(name + i + "/" + this.size, values[i]); 
      this.regions[i] = object;
    }
  }

  // Seleciona a classe com maior grau de pertinencia
  public String getClass(String X) {
    String outClass = "";
    double outValue = 0;

    for (int i = 0; i < this.regions.length; i++) {
      if (this.regions[i].Fuzzifica(X) > outValue) {
        outValue = this.regions[i].Fuzzifica(X);
        outClass = this.regions[i].getName();
      }
    }

    return (outClass);
  }

  // Retorna o grau de pertinencia na classe inserida
  public double getMembership(String value, String classLabel) {
    double result = 0;

    for (int i = 0; i < this.size; i++) {
      if (regions[i].getName() == classLabel) {
        result = regions[i].Fuzzifica(value);
      }
    }

    return result;
  }

  public String getName() {
    return (this.name);
  }
}
