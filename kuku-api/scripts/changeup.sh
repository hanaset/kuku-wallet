#!/bin/bash
app_name="hana-ground"
app_file="hana-ground.jar"
staging_file="hana-ground-staging.jar"

./shutdown.sh

# jar delete
if [ -f "$app_file" ]
then
	echo "$app_file found."
	rm ${app_file}
	echo "$app_file delete completed."
else
	echo "$app_file not found."
fi

# staging.war => war
if [ -f "$staging_file" ]
then
	echo "$staging_file => $app_file"
	mv ${staging_file} ${app_file}
fi

./start.sh
