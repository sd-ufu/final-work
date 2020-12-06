LINK NO GITHUB [here](https://github.com/sd-ufu/final-work.git)

ETAPAS PARA COMPILAR E RODAR O PROJETO
---------

1) Baixar mvn [here](https://maven.apache.org/download.cgi).

2) Instalar mvn [here](https://maven.apache.org/install).

3) Abra a pasta do projeto.

4) Execute este comando para transformar os **proto files** em **java files**: `mvn clean generate-sources`.

5) Execute este comando para gerar o **.jar** para rodar o servidor: `mvn -P server install`.

6) Execute este comando para gerar o **.jar** para rodar o cliente: `mvn -P client install`.

7) Execute este comando para iniciar o servidor: `java -jar target/finalwork-server.jar`.

8) Execute este comando para iniciar o cliente e executar ele lendo os arquivos na pastsa **files/input**: `SCOPE=FILE java -jar target/finalwork-client.jar`.

8) Execute este comando para iniciar o cliente e executar a interface de terminal: `SCOPE=TERMINAL java -jar target/finalwork-client.jar`.


FORMATOS PARA OS ARQUIVOS DE ENTRADAS
---------
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


INTEGRANTES
---------
Nome: Ivan Guimarães Monte

Matrícula: 11521BSI210


Nome: Paulo Victor da Silva Oliveira

Matrícula: 11521bsi219


Nome: Ricardo Pereira

Matrícula: 11521BSI220
