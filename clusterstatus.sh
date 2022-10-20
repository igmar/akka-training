#!/bin/sh
wget -qO- http://localhost:8558/cluster/members/ | jq .
