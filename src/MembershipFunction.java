package src;
public class MembershipFunction {
    int atribute;
    int size;
    Region[] regions;
    DoublePair atr_limits;

    public MembershipFunction(DataSet data, int var, int shape) {
        // Faster construtor  
        this(var, data.getAttributeLabelCounts().get(var), data.attributeLabels.get(var).toArray(new String[data.getAttributeLabelCounts().get(var)]), data.attributeLimits.get(var), shape);
    };

    public MembershipFunction(int atribute, int size, String[] labels, DoublePair atr_limits, int shape) {
        this.atribute = atribute;
        this.size = size;

        // atribute limites = (lower limit = x, upper limit = y)
        this.atr_limits = atr_limits;
        
        double range = this.atr_limits.y() - this.atr_limits.x();
        double offset_tri = range / (this.size - 1);
        double offset_tra = range / (this.size - 1); // Alterar o calcul
        double x0, x1, x2, x3;

        // Cria uma region para cada label, seguindo o offset.
        this.regions = new Region[size];
        for (int label_index = 0; label_index < this.size; label_index++) {
            if (shape == Region.TRAPEZOIDAL) {
                x0 = this.atr_limits.x() + (label_index - 1) * offset_tra;
                x1 = this.atr_limits.x() + label_index * offset_tra; // CONSERTAR O CALCULO PARA O TRAPEZOIDAL
                x2 = this.atr_limits.x() + label_index * offset_tra; // CONSERTAR O CALCULO PARA O TRAPEZOIDAL
                x3 = this.atr_limits.x() + (label_index + 1) * offset_tra;
                this.regions[label_index] = new Region(label_index, x0, x1, x2, x3); // Trapezoidal

            } else {
                x0 = this.atr_limits.x() + (label_index - 1) * offset_tri;
                x1 = this.atr_limits.x() + label_index * offset_tri;
                x3 = this.atr_limits.x() + (label_index + 1) * offset_tri;
                this.regions[label_index] = new Region(label_index, x0, x1, x3); // Triangular
            }
        }
    }

    // Seleciona a label com maior grau de pertinencia
    public int getBestLabel(double X) {
        int outLabel = -1;
        double outValue = 0;

        // Passa pelas funções de pertinencia de cada label e retorna a com maior grau de pertencimento
        for (int i = 0; i < this.regions.length; i++) {
            if (this.regions[i].fuzzify(X) > outValue) {
              outValue = this.regions[i].fuzzify(X);
              outLabel = this.regions[i].label;
            }
        }
      
        return (outLabel);
    }

    // Retorna o grau de pertinencia de um valor na classe inserida
    public double getMembership(int label, double value) {
        double result = 0;

        for (int i = 0; i < this.size; i++) {
            if (regions[i].label == label) {
                result = regions[i].fuzzify(value);
                return result;
            }
        }

        return result;
    }
}
