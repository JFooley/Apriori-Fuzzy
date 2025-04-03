import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSet {

    // Estruturas para armazenar informações dos atributos
    private Map<Integer, Integer> attributeLabelCounts = new HashMap<>(); // Quantidade de labels por atributo
    private Map<Integer, List<String>> attributeLabels = new HashMap<>(); // Nomes das labels por atributo
    private Map<Integer, Double[]> attributeLimits = new HashMap<>(); // Limites numérico das labels

    private List<String[]> attributeNames = new ArrayList<>(); // Nomes dos atributos

    private List<String[]> rawData = new ArrayList<>(); // Transações
    private List<String[]> data = new ArrayList<>(); // Transações

    private List<String> inputAttributes = new ArrayList<>(); // Lista de atributos de entrada
    private List<String> outputAttributes = new ArrayList<>(); // Lista de atributos de saída

    // Constantes
    private int default_atribute_size = 5; // Quantidade padrão de labels para atributos não nominais

    public DataSet(String filePath, int default_atribute_size) {
        this.default_atribute_size = default_atribute_size;
        readARFF(filePath);
    }

    // Método para ler o arquivo ARFF
    private void readARFF(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            boolean isDataSection = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Ignorar linhas vazias ou comentários
                if (line.isEmpty() || line.startsWith("%")) {
                    continue;
                }

                // Seção de dados
                if (line.toLowerCase().startsWith("@data")) {
                    isDataSection = true;
                    continue;
                }

                // Seção de atributos
                if (!isDataSection && line.toLowerCase().startsWith("@attribute")) {
                    processAttributeLine(line);
                }

                // Identificar atributos de entrada
                if (line.toLowerCase().startsWith("@input")) {
                    String[] inputs = line.substring(line.indexOf(" ") + 1).split(",\\s*");
                    inputAttributes.addAll(Arrays.asList(inputs));
                    continue;
                }

                // Identificar atributos de saída
                if (line.toLowerCase().startsWith("@output")) {
                    String[] outputs = line.substring(line.indexOf(" ") + 1).split(",\\s*");
                    outputAttributes.addAll(Arrays.asList(outputs));
                    continue;
                }

                // Ler dados
                if (isDataSection && !line.isEmpty()) {
                    rawData.add(line.split(",\\s*"));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + filePath);
            e.printStackTrace();
        }
    
        // Salva o raw data
        data = new ArrayList<>();
        for (String[] array : rawData) {
            data.add(array.clone()); // Adiciona o array clonado à nova lista
        }

        // Tratar os dados
        for (Integer i = 0; i < attributeNames.size(); i++) {
            if (attributeNames.get(i)[1] == "Nominal") { // verifica se o atributo é nominal
                for (String[] line : data) {
                    // Busca a lista de labels do atributo e substitui a linha pelo indice da label daquela linha ex: {M, F, I} o M na transação se torna 0
                    line[i] = Integer.toString(attributeLabels.get(i).indexOf(line[i]));
                }
            }
        }

    }

    // Método para processar uma linha de atributo
    private void processAttributeLine(String line) {
        String[] parts = line.split("\\s+", 3); // Dividir a linha em partes
        if (parts.length < 3) {
            return; // Ignorar linhas mal formatadas
        }

        String attributeName = parts[1]; // Nome do atributo
        String attributeType = parts[2]; // Tipo do atributo / valores limite

        // Processar atributos nominais
        if (attributeType.startsWith("{")) {
            attributeNames.add(new String[] {attributeName, "Nominal"});
            processNominalAttribute(attributeNames.size() - 1, attributeType);
            
        } else {
            attributeNames.add(new String[] {attributeName, "Numeric"});
            processNumericAttribute(attributeNames.size() - 1, attributeType);
        }
    }

    // Método para processar atributos nominais
    private void processNominalAttribute(Integer index, String attributeType) {
        // Extrair valores nominais
        String[] nominalValues = attributeType.replace("{", "").replace("}", "").split(",");

        // Armazenar quantidade de labels
        attributeLabelCounts.put(index, nominalValues.length);

        // Armazenar os limites
        attributeLimits.put(index, new Double[] { 0d, (double) nominalValues.length-1});

        // Armazenar nomes das labels
        List<String> labels = new ArrayList<>();
        for (String value : nominalValues) {
            labels.add(value.trim());
        }
        attributeLabels.put(index, labels);
    }

    // Método para processar atributos não nominais
    private void processNumericAttribute(Integer index, String attributeType) {
        Pattern pattern = Pattern.compile("\\[([\\d\\.E\\-]+),\\s*([\\d\\.E\\-]+)\\]");
        Matcher matcher = pattern.matcher(attributeType);
        if (matcher.find()) {
            double lowerLimit = Double.parseDouble(matcher.group(1));
            double upperLimit = Double.parseDouble(matcher.group(2));

            // Armazenar os limites
            attributeLimits.put(index, new Double[] { lowerLimit, upperLimit });
        }

        // Definir quantidade padrão de labels
        attributeLabelCounts.put(index, default_atribute_size);

        // Criar labels no formato L_0, L_1, ..., L_(default_atribute_size-1)
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < default_atribute_size; i++) {
            labels.add("L_" + i + "/" + default_atribute_size);
        }
        attributeLabels.put(index, labels);
    }

    // Métodos para acessar as informações
    public List<String[]> getAttributeNames() {
        return attributeNames;
    }

    public Map<Integer, Integer> getAttributeLabelCounts() {
        return attributeLabelCounts;
    }

    public Map<Integer, Double[]> getAttributeLimits() {
        return attributeLimits;
    }

    public Map<Integer, List<String>> getAttributeLabels() {
        return attributeLabels;
    }

    public List<String> getInputAttributes() {
        return inputAttributes;
    }

    public List<String> getOutputAttributes() {
        return outputAttributes;
    }

    public List<String[]> getData() {
        return data;
    }

    public List<String[]> getRawData() {
        return rawData;
    }

    // Exemplo de uso
    public static void main(String[] args) {
        DataSet reader = new DataSet("execution/datasets/abalone9-18/abalone9-18-5-1tra.dat", 3);

        System.out.println("Nomes dos atributos: ");
        for (String[] name : reader.getAttributeNames()) {
            System.out.print(name[0] + ", ");
        }
        System.out.print("\n");
        
        System.out.println("Quantidade de labels por atributo: " + reader.getAttributeLabelCounts());

        System.out.println("Limites das labels por atributo: ");
        for (Map.Entry<Integer, Double[]> entrada : reader.getAttributeLimits().entrySet()) {
            System.out.println(entrada.getKey() + " - " + entrada.getValue()[0] + "/" + entrada.getValue()[1]);
        }
        System.out.println("Labels dos atributos: ");
        for (Map.Entry<Integer, List<String>> entrada : reader.getAttributeLabels().entrySet()) {
            System.out.println(entrada.getKey() + " - " + entrada.getValue().toString());
        }
        System.out.println("Inputs: " + reader.getInputAttributes());
        System.out.println("Outputs: " + reader.getOutputAttributes());

        System.out.println("\nDados RAW: ");
        for (Integer j = 0; j < 10; j++) {
            String[] line = reader.getRawData().get(j);
            for (int i = 0; i < reader.attributeNames.size(); i++) {
                System.out.print(line[i]+", ");
            }
            System.out.print("\n");
        }

        System.out.println("\nDados tratados: ");
        for (Integer j = 0; j < 10; j++) {
            String[] line = reader.getData().get(j);
            for (int i = 0; i < reader.attributeNames.size(); i++) {
                System.out.print(line[i]+", ");
            }
            System.out.print("\n");
        }
    }
}