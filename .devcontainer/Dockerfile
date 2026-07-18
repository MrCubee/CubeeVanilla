FROM alpine AS buildtools
ARG SPIGOT_VERSION
ARG JAVA_MAJOR
RUN apk add --no-cache bash git openjdk8 openjdk17 openjdk21 openjdk25 && mkdir -p /opt/spigot-builder /build
ADD https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar /opt/spigot-builder/BuildTools.jar
WORKDIR /build
RUN java -jar /opt/spigot-builder/BuildTools.jar --rev 1.21.11
RUN java -jar /opt/spigot-builder/BuildTools.jar --rev 26.1
RUN java -jar /opt/spigot-builder/BuildTools.jar --rev 26.2

FROM alpine
RUN apk add --no-cache bash git openjdk8 openjdk17 openjdk21 openjdk25
COPY --from=buildtools /root/.m2 /root/.m2
