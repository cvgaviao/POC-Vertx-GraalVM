# -- GLOBAL ARGS
## The graalvm version
ARG GRAALVM_VERSION=19.2.0

# The Alpine version
ARG ALPINE_VERSION=3.10

## Stage 1 : build with maven builder image with native capabilities
FROM oracle/graalvm-ce:${GRAALVM_VERSION} AS graalvm

ARG GRAALVM_VERSION
RUN ln -s /opt/graalvm-ce-${GRAALVM_VERSION} /opt/graalvm && \
    gu install native-image

ENV GRAALVM_HOME=/opt/graalvm \
    JAVA_HOME=/opt/graalvm \
    PATH=${GRAALVM_HOME}/bin:${PATH}

##-----------------------------------------------
## Stage 2 : create the docker final image

FROM graalvm as build-aot

# the jar file name of the application
ARG APP_JAR_FILE

# the name of the native image to be created
ARG APP_NATIVE_FILE

## Copy the built application to the image in order to run native-image with it
COPY ${APP_JAR_FILE} ./${APP_JAR_FILE}

## run the native-image tool
RUN native-image -jar ${APP_JAR_FILE} ${APP_NATIVE_FILE}


##-----------------------------------------------
## Stage 3 : create the docker final image

FROM alpine:${ALPINE_VERSION}

ARG GRAALVM_VERSION
ARG ALPINE_VERSION

# the name of the native image to be created
ARG APP_NATIVE_FILE

# the ID of the project being built
ARG APP_PROJECT_ID

# the version of the image
ARG APP_PROJECT_VERSION

# the image building date 
ARG APP_IMAGE_BUILD_DATE

# The UserName which is used to run the application
ARG USER_NAME=cvgaviao

# The GroupName which is used to run the application
ARG GROUP_NAME=c8tech

# The UserID which is used to run the application
ARG UID=1000

# The GroupID which is used to run the application
ARG GID=1000

# The HTTP port to be exposed
ARG HTTP_PORT=8080 

# where the container will be installed
ARG APP_HOME_DIR=app

# where configuration files are in the host
ARG HOST_CONFIG_PATH 

# the vertx application's main verticle
ARG APP_MAIN_VERTICLE

# where logback configuration file is kept
ENV APP_LOGBACK_CONFIG_FILE=${APP_CONFIG_PATH}/logback.xml \
# The HTTP port to be exposed
    HTTP_PORT=${HTTP_PORT} 

# Labels could be consumed by OpenShift
LABEL io.k8s.description="." \
      io.k8s.display-name="VxOSGi with Felix 3.0.6" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="VxOSGi Felix openJDK11 Alpine" \
      buildDate=${APP_IMAGE_BUILD_DATE} \
      maintainer=cvgaviao@c8tech.com.br \
      user.ID=${UID} \
      group.ID=${GID} \
      project.ID=${APP_PROJECT_ID} \
      project.version=${APP_PROJECT_VERSION} \
      alpine.version=${ALPINE_VERSION} \
      graalvm.version=${GRAALVM_VERSION}

RUN apk add --no-cache shadow sudo  ca-certificates && \
    if [ -z "`getent group $GID`" ]; then \
      addgroup -S -g $GID ${GROUP_NAME}; \
    else \
      groupmod -n ${GROUP_NAME} `getent group $GID | cut -d: -f1`; \
    fi && \
    if [ -z "`getent passwd $UID`" ]; then \
      adduser -D -S -u $UID -G ${GROUP_NAME} -h /home/${USER_NAME} ${USER_NAME}; \
    else \
      usermod -l ${USER_NAME} -g $GID -d /home/${USER_NAME} -m `getent passwd $UID | cut -d: -f1`; \
    fi && \
    echo "vxosgi ALL=(root) NOPASSWD:ALL" > /etc/sudoers.d/${USER_NAME} && \
    chmod 0440 /etc/sudoers.d/${USER_NAME}

USER ${USER_NAME}

RUN  mkdir -p /home/${USER_NAME} \
  && chmod a+s /home/${USER_NAME} \  
  && mkdir -p /home/${USER_NAME}/${APP_HOME_DIR} \
  && mkdir -p /home/${USER_NAME}/${APP_HOME_DIR}/logs \
  && mkdir -p /home/${USER_NAME}/${APP_HOME_DIR}/config
  
USER ROOT

WORKDIR /home/${USER_NAME}

# where logging files must be generated
# where configuration files must be dropped
VOLUME ["/home/${USER_NAME}/${APP_HOME_DIR}/logs","/home/${USER_NAME}/${APP_HOME_DIR}/config"]

COPY --chown=${USER_NAME}:${GROUP_NAME} --from=build-aot ${APP_NATIVE_FILE} ./${APP_HOME_DIR}/${APP_NATIVE_FILE}

COPY --chown=${USER_NAME}:${GROUP_NAME} ${HOST_CONFIG_PATH} ./${APP_HOME_DIR}/config/

EXPOSE ${HTTP_PORT} 

USER ${USER_NAME}

# Create a script to pass command line args to java
RUN echo "./${APP_HOME_DIR}/${APP_NATIVE_FILE} run ${APP_MAIN_VERTICLE} -Djavax.net.ssl.trustStore=graalvm/jre/lib/security/cacerts -Djavax.net.ssl.trustAnchors=graalvm/jre/lib/security/cacerts -Dlogback.configurationFile=${APP_LOGBACK_CONFIG_FILE}" > run_container.sh && \
    # Setting up permissions for the script to run
    chmod u+x,o-rwx run_container.sh && \
    # Setting up permissions for the script to the native image
    chmod u+x,o-rwx ./${APP_HOME_DIR}/${APP_NATIVE_FILE}


ENTRYPOINT ["/bin/sh", "run_container.sh"]
CMD [""]    