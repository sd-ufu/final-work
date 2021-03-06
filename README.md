LINK NO GITHUB [here](https://github.com/sd-ufu/final-work.git)

ETAPAS PARA COMPILAR E RODAR O PROJETO
---------

1) Baixar mvn [here](https://maven.apache.org/download.cgi).

2) Instalar mvn [here](https://maven.apache.org/install).

3) Abra a pasta do projeto.

4) Execute este comando para transformar os **proto files** em **java files**: `mvn clean generate-sources`.

5) Execute este comando para gerar o **.jar** para rodar as máquinas de estado: `mvn -P ratis install`.

6) Execute este comando para gerar o **.jar** para rodar o servidor: `mvn -P server install`.

7) Execute este comando para gerar o **.jar** para rodar o cliente: `mvn -P client install`.

8) Escreva no arquivo **files/address** as máquinas de estado (descrição a baixo de como fazer isso)

9) Execute este comando para iniciar uma máquina de estado: `java -jar target/finalwork-ratis.jar {{NOME_DA_MAQUINA}}`.

10) Execute este comando para iniciar o servidor (Execute esse comando após ter iniciado todas as máquinas de estado): `java -jar target/finalwork-server.jar {{PORTA}}`.

11) Execute este comando para iniciar o cliente e executar ele lendo os arquivos na pastsa **files/input** (ao final do processo, um arquivo de log para cada arquivo de entrada é gerado na pasta **files/output**): `SCOPE=FILE java -jar target/finalwork-client.jar {{HOST}} {{PORTA}}`.

11) Execute este comando para iniciar o cliente e executar a interface de terminal: `SCOPE=TERMINAL java -jar target/finalwork-client.jar {{HOST}} {{PORTA}}`.


FORMATOS PARA O ARQUIVO DE FILES/ADDRESS
---------
Cada linha sera uma máquina de estado
`{{NOME}} {{HOST}} {{PORTA}}`

Ex: Se eu quero 3 máquinas de estado, meu arquivo deve conter 3 linhas

```
MAQUINA_1 127.0.0.1 3000
MAQUINA_2 127.0.0.1 3500
MAQUINA_3 127.0.0.1 4000
```

FORMATOS PARA OS ARQUIVOS DE ENTRADAS
---------
Cada linha um comando seja para ser enviado ao servidor

Os arquivos se encontram em **files/input/{{fileName}}**

OBS: O `{{D}}` não pode conter espaços

**SET** => `1 {{K}} {{D}}`
* Exemplo: Quero inserir na chave `20` o valor `NOME` => `1 20 NOME`

**GET** => `2 {{K}}`
* Exemplo: Quero ver o valor da chave `20` => `2 20`

**DEL** => `3 {{K}}`
* Exemplo: Quero apagar a chave `20` => `3 20`

**DEL BY KEY AND VERSION** => `4 {{K}} {{VERS}}`
* Exemplo: Quero apagar a chave `20` com a versão `10` => `4 20 10`

**TEST AND SET** => `5 {{K}} {{VERS}} {{D}}`
* Exemplo: Quero atualizar a chave `20` com a versão `10` => `5 20 10 NEW_DATA_20`


Ex: Se eu quero executar um **SET**, depois um **GET** e depois um **DEL**

```
1 1 VALOR_DA_CHAVE_1_SEM_ESPACO
2 1
3 1
```


INTEGRANTES
---------
Nome: Ivan Guimarães Monte

Matrícula: 11521BSI210


Nome: Paulo Victor da Silva Oliveira

Matrícula: 11521bsi219


Nome: Ricardo Pereira

Matrícula: 11521BSI220


Nome: Fabricio Leyes

Matrícula: 11421BSI222