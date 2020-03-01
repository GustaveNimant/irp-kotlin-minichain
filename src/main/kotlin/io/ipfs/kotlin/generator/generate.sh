#!/bin/bash
 
args=$*
argnumber=$#

script=`basename $0`

what=`echo $1 | tr '[:upper:]' '[:lower:]'`
shift;ELEMENT=$1
shift;ABBREVIATION=$1

last=3

if [ $argnumber -ne $last ] 
then 
    echo "number of arguments is $argnumber instead of $last for $script ${this}"
    echo "Usage : ${script} provider|register|type|value element abbreviation"
    exit
fi

What=`capitalize_first.sh $what`
file="${ELEMENT}${What}.kt"

sed -e "s/{{ELEMENT}}/${ELEMENT}/g" \
    -e "s/{{ABBREVIATION}}/${ABBREVIATION}/g" \
    ${what}.template > ${file}

if [ "$what" == "type" ]
then
    for i in $(seq 1 ${});do
    sed -e "s/SUB01/${SUB01}/g" \
fi
echo "cat $file"
