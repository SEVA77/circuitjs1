#!/bin/bash

# Run `sh update_languages.sh` in the current directory

export TMP_DIR=templates
export LOCALES_DIR=src/main/java/com/lushprojects/circuitjs1/public

cd ..

# Create templates files
cat $LOCALES_DIR/locale_csx.txt $TMP_DIR/add_to_locale_csx.txt > $TMP_DIR/locale_csx.template
cat $LOCALES_DIR/locale_da.txt $TMP_DIR/add_to_locale_da.txt > $TMP_DIR/locale_da.template
cat $LOCALES_DIR/locale_de.txt $TMP_DIR/add_to_locale_de.txt > $TMP_DIR/locale_de.template
cat $LOCALES_DIR/locale_es.txt $TMP_DIR/add_to_locale_es.txt > $TMP_DIR/locale_es.template
cat $LOCALES_DIR/locale_fr.txt $TMP_DIR/add_to_locale_fr.txt > $TMP_DIR/locale_fr.template
cat $LOCALES_DIR/locale_it.txt $TMP_DIR/add_to_locale_it.txt > $TMP_DIR/locale_it.template
cat $LOCALES_DIR/locale_nb.txt $TMP_DIR/add_to_locale_nb.txt > $TMP_DIR/locale_nb.template
cat $LOCALES_DIR/locale_pl.txt $TMP_DIR/add_to_locale_pl.txt > $TMP_DIR/locale_pl.template
cat $LOCALES_DIR/locale_ru.txt $TMP_DIR/add_to_locale_ru.txt > $TMP_DIR/locale_ru.template

# Updates locale files
cat $TMP_DIR/locale_csx.template > $LOCALES_DIR/locale_csx.txt
cat $TMP_DIR/locale_da.template > $LOCALES_DIR/locale_da.txt
cat $TMP_DIR/locale_de.template > $LOCALES_DIR/locale_de.txt
cat $TMP_DIR/locale_es.template > $LOCALES_DIR/locale_es.txt
cat $TMP_DIR/locale_fr.template > $LOCALES_DIR/locale_fr.txt
cat $TMP_DIR/locale_it.template > $LOCALES_DIR/locale_it.txt
cat $TMP_DIR/locale_nb.template > $LOCALES_DIR/locale_nb.txt
cat $TMP_DIR/locale_pl.template > $LOCALES_DIR/locale_pl.txt
cat $TMP_DIR/locale_ru.template > $LOCALES_DIR/locale_ru.txt

rm $TMP_DIR/locale_csx.template
rm $TMP_DIR/locale_da.template
rm $TMP_DIR/locale_de.template
rm $TMP_DIR/locale_es.template
rm $TMP_DIR/locale_fr.template
rm $TMP_DIR/locale_it.template
rm $TMP_DIR/locale_nb.template
rm $TMP_DIR/locale_pl.template
rm $TMP_DIR/locale_ru.template
