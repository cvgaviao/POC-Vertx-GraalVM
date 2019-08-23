# POC-Vertx-GraalVM
A POC to evaluate Vert.x backend application running on GraalVM


* Requirements:

- Maven 3.6.1
- GraalVM v19.1.1
        The host system must have the GraalVM and its dependencies properly installed. see: https://www.graalvm.org/docs/getting-started/

*Issues detected:

- Most of calls to Class.forName(String) won't work.
  For example, deploying a verticle using the class name will not work: vertx.deployVerticle("my.class")
