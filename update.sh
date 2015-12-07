#! /bin/bash

CURRENT_DIR=`pwd`

cd $1
mvn package

cp ./target/my-project-0.0.1-SNAPSHOT.jar $CURRENT_DIR/