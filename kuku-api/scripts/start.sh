#!/bin/bash
app_name="hana-ground"
app_file="hana-ground.jar"
profile="develop"

# application start.
echo "$app_name is start."
nohup java -Dserver.port=5000 -Dspring.profiles.active=${profile} -jar ${app_file} > /dev/null &
