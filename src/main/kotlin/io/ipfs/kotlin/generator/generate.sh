#!/bin/bash
 
args=$*
argnumber=$#

script=`basename $0`

what=$1
shift;ELEMENT=$1
shift;ABBREVIATION=$1

last=3

if [ $argnumber -ne $last ] 
then 
    echo "number of arguments is $argnumber instead of $last for $script ${this}"
    exit
fi

What=`capitalize_first.sh $what`
file="${ELEMENT}${What}.kt"

sed -e "s/{{ELEMENT}}/${ELEMENT}/g" \
    -e "s/{{ABBREVIATION}}/${ABBREVIATION}/g" \
    register.template > ${file}

echo "cat $file"
