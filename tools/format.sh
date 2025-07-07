#!/bin/bash

FORMATTER_JAR=~/Documents/Java/SamFish/tools/google-java-format-1.27.0-all-deps.jar

if [[ ! -f "$FORMATTER_JAR" ]]; then
  echo "❌ Formatter JAR not found: $FORMATTER_JAR"
  exit 1
fi

find src -name "*.java" | while read -r file; do
    echo "📄 Checking $file"

    temp_file=$(mktemp)

    # Run formatter and check exit code
    java -jar "$FORMATTER_JAR" "$file" > "$temp_file"
    if [[ $? -ne 0 ]]; then
        echo "❌ Failed to format $file"
        rm "$temp_file"
        continue
    fi

    if ! diff -u "$file" "$temp_file" > /dev/null; then
        echo "🔍 Changes:"
        diff -u "$file" "$temp_file"

        echo -n "💬 Apply changes to $file? [y/N]: "
        read -r answer < /dev/tty

        if [[ "$answer" == "y" || "$answer" == "Y" ]]; then
            mv "$temp_file" "$file"
            echo "✅ Changes applied to $file"
        else
            echo "❌ Skipped $file"
            rm "$temp_file"
        fi
    else
        echo "✅ No changes needed"
        rm "$temp_file"
    fi

    echo
done
