import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSet2 {
    public String label; // Nome do DataSet
    public String archive; // Path do arquivo
    public int nClasses;
    public int lin = 0, col = 0; // Tamanho do dataset

    public String[] inputs = new String[0]; // Atributos de entrada
    public String[] outputs = new String[0]; // Atributos de saída

    public String[][] Data;
    public double[][] limits; // Limites de atributos fuzzy e nominais
    public Map<String, Map<String, Integer>> nominalMappings = new HashMap<>(); // Mapeamento de valores nominais

    public DataSet2(String archivePath) {
        this.archive = archivePath;
        
        try {
            Scanner scanner = new Scanner(new File(archivePath));
            boolean dataStart = false;
            List<String> dataBuffer = new ArrayList<>();

            List<double[]> attributeLimits = new ArrayList<>(); // Lista temporária para armazenar os limites

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("@data")) {
                    dataStart = true;
                } else if (line.contains("@relation") && !dataStart) {
                    this.label = line.split(" ")[1];
                } else if (line.contains("@input") && !dataStart) {
                    this.inputs = line.substring(line.indexOf(" ") + 1).split(",\\s*");
                } else if (line.contains("@output") && !dataStart) {
                    this.outputs = line.substring(line.indexOf(" ") + 1).split(",\\s*");
                    this.nClasses = outputs.length;
                } else if (line.contains("@attribute") && !dataStart) {
                    // Verifica se o atributo é fuzzy ou nominal
                    if (line.contains("real") || line.contains("integer")) {
                        Pattern patternFuzzy = Pattern.compile("@attribute\\s+([\\w-]+)\\s+(real|integer)\\s*\\[([\\d\\.E\\-]+),\\s*([\\d\\.E\\-]+)\\]");
                        Matcher matcher = patternFuzzy.matcher(line);
                        if (matcher.find()) {
                            String name = matcher.group(1);
                            double lowerValue = Double.parseDouble(matcher.group(3));
                            double upperValue = Double.parseDouble(matcher.group(4));

                            // Armazena os limites do atributo fuzzy
                            attributeLimits.add(new double[] { lowerValue, upperValue });
                        }
                    } 
                    // Atributo nominal
                    else if (line.contains("{")) {
                        Pattern patternClassic = Pattern.compile("@attribute\\s+([\\w-]+)\\s+\\{([^{}]+)\\}");
                        Matcher matcher = patternClassic.matcher(line);
                        if (matcher.find()) {
                            String name = matcher.group(1);
                            String[] values = matcher.group(2).split("\\s*,\\s*");

                            // Cria mapeamento de valores nominais
                            createNominalMapping(name, values);

                            // Armazena limites nominais (0 a n-1)
                            attributeLimits.add(new double[] { 0, values.length - 1 });
                        }
                    }
                } else if (dataStart) {
                    dataBuffer.add(line);
                }
            }

            // Preenche a matriz de limites
            this.limits = new double[attributeLimits.size()][2];
            for (int i = 0; i < attributeLimits.size(); i++) {
                this.limits[i] = attributeLimits.get(i);
            }

            // Preenche a matriz de dados com os valores traduzidos
            this.lin = dataBuffer.size();
            this.col = inputs.length + outputs.length;
            this.Data = new String[this.lin][this.col];

            for (int i = 0; i < this.lin; i++) {
                String[] values = dataBuffer.get(i).split(",\\s*");
                for (int j = 0; j < values.length; j++) {
                    String attrName = (j < inputs.length) ? inputs[j] : outputs[j - inputs.length];
                    if (nominalMappings.containsKey(attrName)) {
                        values[j] = String.valueOf(nominalMappingsnominalMappings.get(attrName).get(values[j]));
                    }
                }
                this.Data[i] = values;
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para criar mapeamento de valores nominais
    private void createNominalMapping(String name, String[] values) {
        Map<String, Integer> mapping = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            mapping.put(values[i], i);
        }
        nominalMappings.put(name, mapping);
    }
}
