#!/bin/bash
 
args=$*
argnumber=$#

script=`basename $0`

<<<<<<< HEAD
what=$1
=======
what=`echo $1 | tr '[:upper:]' '[:lower:]'`
>>>>>>> 711622841ee258c69f8a30de57cf54217945111b
shift;ELEMENT=$1
shift;ABBREVIATION=$1

last=3

if [ $argnumber -ne $last ] 
then 
    echo "number of arguments is $argnumber instead of $last for $script ${this}"
<<<<<<< HEAD
=======
    echo "Usage : ${script} provider|register|type|value element abbreviation"
>>>>>>> 711622841ee258c69f8a30de57cf54217945111b
    exit
fi

What=`capitalize_first.sh $what`
file="${ELEMENT}${What}.kt"

sed -e "s/{{ELEMENT}}/${ELEMENT}/g" \
    -e "s/{{ABBREVIATION}}/${ABBREVIATION}/g" \
<<<<<<< HEAD
    register.template > ${file}

=======
    ${what}.template > ${file}

if [ "$what" == "type" ]
then
    for i in $(seq 1 ${});do
    sed -e "s/SUB01/${SUB01}/g" \
fi
>>>>>>> 711622841ee258c69f8a30de57cf54217945111b
echo "cat $file"
