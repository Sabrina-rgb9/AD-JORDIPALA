#!/bin/bash

cd "$(dirname "$0")"

export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED \
                   --add-opens java.base/java.nio=ALL-UNNAMED \
                   --add-opens java.base/java.util=ALL-UNNAMED"

# Main class (default if not provided)
mainClass="${1:-cat.iesesteveterradas.PR210Honor}"
shift

# Prepare program args
javaArgsStr=$(printf "%q " "$@")

echo "MAIN CLASS: $mainClass"
echo "ARGS: $javaArgsStr"
echo "MAVEN_OPTS: $MAVEN_OPTS"

mvn clean compile exec:java \
    -Dexec.mainClass="$mainClass" \
    -Dexec.args="$javaArgsStr"
