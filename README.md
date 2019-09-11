# POC-Vertx-GraalVM
A POC to evaluate Vert.x 4.x backend and client applications being built for GraalVM and Alpine Linux.


## Requirements:

- Maven 3.6.1
- Docker v19.03.2

## Building

  The idea is use Maven to call a docker multi-stage building process that will end with a small Alpine image.

So, in order to build do this:
- Clone the repository: `git clone git@github.com:cvgaviao/POC-Vertx-GraalVM.git`
- Open a terminal, go to one of the provided projects and call: `mvn clean package -withDocker`
- After the build finish you can run a container using: `docker run cvgaviao/vertx-server-on-graalvm:0.0.1-SNAPSHOT`
