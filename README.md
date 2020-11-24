# final-work

1) Download mvn [here](https://maven.apache.org/download.cgi).

2) Install mvn [here](https://maven.apache.org/install).

3) Go to project root.

4) Run command to transform **proto files** in **java files**: `mvn clean generate-sources`.

5) Run command to generate server jar file: `mvn -P server install`.

6) Run command to generate cliet jar file: `mvn -P client install`.

7) Run command to start server: `java -jar target/finalwork-server.jar`.

8) Run command to start client: `java -jar target/finalwork-client.jar`.
