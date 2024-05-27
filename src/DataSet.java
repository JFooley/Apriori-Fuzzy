import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSet{
    public String label; // Nome do DataSet
    public String archive; // Path do arquivo

    public int lin = 0, col = 0; // Tamanho do dataset

    public ArrayList<MemberFunction> membershipFunctions = new ArrayList<MemberFunction>(); // Lista de funções de pertinencia

    public String[] inputs = new String[0]; // Lista de quais atributos são entradas
    public String[] outputs = new String[0]; // Lista de quais atributos são saídas 

    public String[][] Data; // Matriz com os valores originais
    public String[][] outputData; // Matriz com as labels da classe com maior grau de pertinencia
    
    public DataSet(String archivePath, int membershipFunctionSize) {
        this.archive = archivePath;

        try {
            Scanner scanner = new Scanner(new File(archivePath));
            
            boolean dataStart = false;
            ArrayList<String> dataBuffer = new ArrayList<>(); // Buffer que guarda o Data

            // Lê os dados do arquivo
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                if (line.contains("@data")){
                    dataStart = true;
                }

                else if (line.contains("@relation") && !dataStart){
                    this.label = line.split(" ")[1];
                }

                else if (line.contains("@inputs") && !dataStart){
                    String[] parts = line.substring(line.indexOf(" ") + 1).split(", ");
                    this.inputs = parts;
                }

                else if (line.contains("@outputs") && !dataStart){
                    String[] parts = line.substring(line.indexOf(" ") + 1).split(", ");
                    this.outputs = parts;
                }

                else if (line.contains("@attribute") && !dataStart){
                    Pattern patternFuzzy = Pattern.compile("@attribute\\s+(\\w+)\\s+real\\s+\\[(\\d+\\.\\d+),\\s+(\\d+\\.\\d+)\\]");
                    Matcher matcherFuzzy = patternFuzzy.matcher(line);

                    Pattern patternClassic = Pattern.compile("@attribute\\s+(\\w+)\\s+\\{([^{}]+)\\}");
                    Matcher matcherClassic = patternClassic.matcher(line);

                    if (matcherFuzzy.find()) { // Atributo Fuzzy
                        String name = matcherFuzzy.group(1);
                        double lowerValue = Double.parseDouble(matcherFuzzy.group(2));
                        double upperValue = Double.parseDouble(matcherFuzzy.group(3));

                        MemberFunction funcObject = new MemberFunction(name, membershipFunctionSize, upperValue, lowerValue);
                        this.membershipFunctions.add(funcObject);
                    }

                    else if (matcherClassic.find()) { // Atributo Classico
                        String name = matcherClassic.group(1);
                        String[] values = matcherClassic.group(2).split("\\s*,\\s*");;

                        MemberFunction funcObject = new MemberFunction(name, values);
                        this.membershipFunctions.add(funcObject);
                    }

                    else {
                        System.out.println("Erro na criação do database: Atributo inválido");
                        System.out.println(line);
                        System.exit(1);
                    }
                }

                else if (dataStart) { // Preenche o buffer com o Data
                    dataBuffer.add(line);
                }
            }
            scanner.close();

            // Preenche a matriz de dados cru
            this.lin = dataBuffer.size();
            this.col = inputs.length + outputs.length;
            this.Data = new String[this.lin][this.col];
            this.outputData = new String[this.lin][this.col];

            for (int i = 0; i < this.lin; i++) {
                String[] values = dataBuffer.get(i).split(",\\s*");
                this.Data[i] = values;

            }

            // Gera a matriz fuzzyficada
            for (int i_lin = 0; i_lin < this.lin; i_lin++) {
                for (int j_col = 0; j_col < this.col; j_col++) { 
                    String outRegion = this.membershipFunctions.get(j_col).getClass(this.Data[i_lin][j_col]); // Escolhe a função de pertinencia especifica daquele atributo e calcula qual região ele pertence
                    this.outputData[i_lin][j_col] = outRegion;
                }
            }
            

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String[][] get_data() {
        return this.outputData;
    }

    public String[][] get_raw_data() {
        return this.Data;
    }

}