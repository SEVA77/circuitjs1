#!/bin/bash

# Run `sh update_languages.sh` in the current directory

export TMP_DIR=templates
export LOCALES_DIR=src/main/java/com/lushprojects/circuitjs1/public
export LANGS="csx da de es fr it nb pl pt ru zh-tw zh ja"

cd ..

for LANG in $LANGS
do
cat $TMP_DIR/add_to_locale_$LANG.txt >> $LOCALES_DIR/locale_$LANG.txt
done
