#!/bin/bash
dir=./db/src/main/resources/db/migration
filename=V$(date +"%Y%m%d%H%M%S")__$1.sql
touch $dir/$filename

