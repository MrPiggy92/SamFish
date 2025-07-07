#!/bin/bash
#javac -d ~/Documents/Java/SamFish/out ~/Documents/Java/SamFish/src/**/*.java
javac -d ~/Documents/Java/SamFish/out $(find ~/Documents/Java/SamFish/src -name "*.java")

