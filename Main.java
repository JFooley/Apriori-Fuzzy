public class Main {
    public static void main(String[] args) {
        // Caminho do arquivo .dat (substitua pelo caminho correto do seu arquivo)
        String filePath = "execution/datasets/abalone9-18/abalone9-18-5-1tra.dat";

        // Criar uma instância do ARFFReader para ler o arquivo
        DataSet reader = new DataSet(filePath);

        // Exibir informações lidas do arquivo .dat
        System.out.println("=== Informações do Arquivo .dat ===");
        System.out.println("Nomes dos atributos: " + reader.getAttributeNames());
        System.out.println("Quantidade de labels por atributo: " + reader.getAttributeLabelCounts());
        System.out.println("Labels dos atributos: " + reader.getAttributeLabels());
        System.out.println("Dados lidos:");

        // Exibir os dados
        for (String[] row : reader.getData()) {
            for (String value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}