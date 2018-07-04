#!/bin/bash
set -e
mvn site
cd ../davidmoten.github.io
git pull
mkdir -p word-wrap 
cp -r ../word-wrap/target/site/* word-wrap/
git add .
git commit -am "update site reports"
git push
