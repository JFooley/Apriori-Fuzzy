import os
import re

# Função para encontrar os pares de arquivos .dat em uma subpasta
def encontrar_pares_dat(caminho_subpasta):
    pares = {}
    for arquivo in os.listdir(caminho_subpasta):
        nome, extensao = os.path.splitext(arquivo)
        if nome.endswith("tst") or nome.endswith("tra"):
            chave = nome[:-3]  # Remove a parte "tst" ou "tra" do nome
            if chave in pares:
                pares[chave].append(arquivo)
            else:
                pares[chave] = [arquivo]
    return pares

# Caminho para a pasta principal
caminho_pasta_principal = "../datasets"

# Caminho para a nova pasta onde as subpastas serão clonadas
novo_caminho_pasta = "../configs"

# Iterar sobre as subpastas na pasta principal
for subpasta in os.listdir(caminho_pasta_principal):
    caminho_subpasta_original = os.path.join(caminho_pasta_principal, subpasta)
    caminho_subpasta_novo = os.path.join(novo_caminho_pasta, subpasta)

    # Verificar se é uma subpasta
    if os.path.isdir(caminho_subpasta_original):
        # Criar a subpasta no novo caminho
        os.makedirs(caminho_subpasta_novo, exist_ok=True)

        # Encontrar os pares de arquivos .dat na subpasta original
        pares_dat = encontrar_pares_dat(caminho_subpasta_original)

        # Criar os arquivos "config(numero - 1).txt" na subpasta correspondente do novo diretório
        for chave, arquivos in pares_dat.items():
            numero = int(chave.split('-')[-1][0])
            with open(os.path.join(caminho_subpasta_novo, f"config{numero - 1}.txt"), 'w') as arquivo_config:
                arquivo_config.write(f"Training = \"../datasets/{subpasta}/{chave}tra.dat\"\n")
                arquivo_config.write(f"Testing = \"../datasets/{subpasta}/{chave}tst.dat\"\n")
                arquivo_config.write("Minimum Support = 0.05\n")
                arquivo_config.write("Depth = 3\n")
                arquivo_config.write("Default Attribute = 3 Triangular\n")

print("Criação finalizada")
input("Pressione Enter para fechar o programa...")
