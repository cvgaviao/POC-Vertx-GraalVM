# POC-Vertx-GraalVM
A POC to evaluate Vert.x 4.x backend server and client applications being built for GraalVM and Alpine Linux.


## Requirements:

- Maven 3.6.1
- Docker v19.03.2

## Building

  The idea is build the Vert.x application's fat jar using Maven and then to call a multi-stage docker building process that will compile the application jar with GraalVM/SubstrateVM and then generate a small Alpine image containing the resulting native binary.

So, in order to build do this:
- Clone the repository: `git clone git@github.com:cvgaviao/POC-Vertx-GraalVM.git`
- Open a terminal, go to one of the provided projects and call: `mvn clean package -withDocker`
- After the build finish you can run a container using: `docker run cvgaviao/vertx-server-on-graalvm:0.0.1-SNAPSHOT`
