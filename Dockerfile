FROM openjdk:11.0.4-jre-slim

EXPOSE 9003
RUN mkdir /opt/service
COPY target/geoip-0.0.1-SNAPSHOT.jar /opt/service/geoip-0.0.1-SNAPSHOT.jar

RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends --no-install-suggests \
    curl \
    ca-certificates \
    unzip \
    wget \
    curl \
    xmlstarlet \
    vim \
    jq \
    coreutils

ADD docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh

WORKDIR /opt/service

ENTRYPOINT ["/docker-entrypoint.sh"]
