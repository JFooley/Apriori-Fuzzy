public class Debug {
    public Debug() {
    }

    public void run_tests(){
            // TESTE DA MEMBERSHIP FUNCTION
            System.out.println("Exemplo Fuzzy");
            MemberFunction teste = new MemberFunction("Altura", 5, 35, 10);
            
            System.out.println(("Name: " + teste.getName()));
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
    
            System.out.println("\nExemplo classic");
            String[] sexoValues = {"M", "F", "I"};
            MemberFunction teste2 = new MemberFunction("Sexo", sexoValues);
    
            System.out.println("Name: " + teste2.getName());
            System.out.println("Tamanho: " + teste2.size);
            System.out.println("Sexo M, F e I\n");
    
            for (int i = 0; i < teste2.size; i++){
                System.out.println(("Label: " + teste2.regions[i].getName()));
            }
    
            System.out.println("\nDado o valor: F");
            for (int i = 0; i < teste2.size; i++){
                System.out.println((teste2.regions[i].getName() + " - " + teste2.getMembership("F", teste2.regions[i].getName())));
            }
    
            System.out.println("Classe pertencente: " + teste2.getClass("F"));
            // TESTE DA MEMBERSHIP FUNCTION
    }
}
