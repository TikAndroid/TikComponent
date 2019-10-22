#!/bin/bash

updateint=3

if [[ $updateint = 0 ]]; then
    exit 0
fi

cat versions.gradle | while read line
do
	if [[ $line =~ "build_versions.version_code" ]]; then
		num=${line#*=}
		num=$(($num+1))
		echo "versionCode更新为"${num}
		sed -i ".bak" "s/${line}/build_versions.version_code = "${num}"/" versions.gradle
	fi
	if [[ $line =~ "build_versions.version_name" ]]; then
		name=${line#*=}
		name=${name#*\'}
		name=${name%*\'}
		OLD_IFS="$IFS" 
		IFS="." 
		arr=($name) 
		IFS="$OLD_IFS"
		index=$(($updateint-1))
		arr[$index]=$((${arr[$index]}+1))
		if [[ ${index} = 3 ]]; then
		    nowName=${arr[0]}"."${arr[1]}"."${arr[2]}"."${arr[3]}
		elif [[ ${index} = 2 ]]; then
			nowName=${arr[0]}"."${arr[1]}"."${arr[2]}
        elif [[ ${index} = 1 ]]; then
			nowName=${arr[0]}"."${arr[1]}".0"
		else
        	nowName=${arr[0]}".0.0"
		fi
		echo "versionName更新为"${nowName}
		sed -i ".bak" "s/${line}/build_versions.version_name = '"${nowName}"'/" versions.gradle
	fi
done
