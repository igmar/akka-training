#!/usr/bin/env bash
sharedDir="$1"
set -x

java \
 -Dconfig.file=./config/localcluster/application.conf \
 -Dakka.management.cluster.bootstrap.contact-point-discovery.port-name=8559 \
 -Dakka.management.http.port=8559 \
 -DNodeId=2 \
 -jar target/akka-training-1.0-allinone.jar
