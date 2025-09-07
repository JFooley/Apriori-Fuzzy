package src;
public class MembershipFunction {
    String atribute_name;
    int atribute_index;
    int size;
    Region[] regions;
    DoublePair atr_limits;

    public MembershipFunction(DataSet data, int var_index, int shape) {
        this(data.getAttributeNames().get(var_index)[0], var_index, data.getAttributeLabelCounts().get(var_index), data.attributeLimits.get(var_index), shape);
    };

    public MembershipFunction(String name, int index, int size, DoublePair atr_limits, int shape) {
        this.atribute_name = name;
        this.atribute_index = index;
        this.size = size;

        this.atr_limits = atr_limits;
        
        double range = this.atr_limits.y() - this.atr_limits.x();
        double offset_tri = range / (this.size - 1);
        double offset_tra = range / ((2 * this.size) - 1);

        // Cria uma region para cada label, seguindo o offset.
        this.regions = new Region[size];
        for (int label_index = 0; label_index < this.size; label_index++) {
            if (shape == Region.TRAPEZOIDAL) {
                double x0 = this.atr_limits.x() + (label_index - 1) * offset_tra;
                double x1 = this.atr_limits.x() + (label_index + 0) * offset_tra;
                double x2 = this.atr_limits.x() + (label_index + 1) * offset_tra;
                double x3 = this.atr_limits.x() + (label_index + 2) * offset_tra;
                this.regions[label_index] = new Region(label_index, x0, x1, x2, x3); // Trapezoidal

            } else if (shape == Region.TRIANGULAR) {
                double x0 = this.atr_limits.x() + (label_index - 1) * offset_tri;
                double x1 = this.atr_limits.x() + label_index * offset_tri;
                double x3 = this.atr_limits.x() + (label_index + 1) * offset_tri;
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
