#!/usr/bin/env bash
sharedDir="$1"
set -x

java \
 -Dconfig.file=./config/localcluster/application.conf \
 -Dakka.remote.artery.canonical.port=2551 \
 -Dakka.management.cluster.bootstrap.contact-point-discovery.port-name=8560 \
 -Dakka.management.http.port=8560 \
 -DNodeId=3 \
 -jar target/akka-training-1.0-allinone.jar
