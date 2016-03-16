#!/bin/bash
for f in `ls original/`; do
  java -jar jarjar-1.0.jar process jarjar-rules "original/$f" "lib/${f:0:-4}-repacked.jar"
done
