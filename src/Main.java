public class Main {
    public static void main(String[] args) throws Exception {        
        // TESTE DA MEMBERSHIP FUNCTION
        MemberFunction teste = new MemberFunction("Altura", 5, 35, 10);
        
        System.out.println(("Name: " + teste.name));
        System.out.println(("Tamanho: " + teste.size));
        System.out.println("De 10 a 35 com 5 labels\n");

        for (int i = 0; i < teste.size; i++){
            Fuzzy fuzzyObject = (Fuzzy) teste.regions[i];
            System.out.println(("Label: " + fuzzyObject.getName()));
            System.out.println(("x0: " + fuzzyObject.x0));
            System.out.println(("x1: " + fuzzyObject.x1));
            System.out.println(("x3: " + fuzzyObject.x3));
        }

        System.out.println("\nDado o valor: 20");
        for (int i = 0; i < teste.size; i++){
            System.out.println((teste.regions[i].getName() + " - " + teste.getMembership(Integer.toString(20), teste.regions[i].getName())));
        }

        System.out.println("Classe pertencente: " + teste.getClass(Integer.toString(20)));
        // TESTE DA MEMBERSHIP FUNCTION

        // 

        // Lê o dataset e transforma em um dataset com as classe fuzzy
            // Definir automaticamente as regiões fuzzy e seus valores de pontas e pico
            // Calcular o grau de pertinencia do valor do atributo para cada região da função de pertinencia e selenciona a com maior grau
            // Transformar a tabela com os valores originais em uma em que cada valor é substituido pela região selecionada

        // Executa o apriori para gerar as regras de associação

        // Utiliza o algorítimo genético para pegar a melhor a base de regras gerada

        // Faz o processo de inferencia usando a base de regras
    }    
}
