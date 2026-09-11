#!/bin/bash
set -e

INPUT_FILE="$1"
if [ -z "$INPUT_FILE" ]; then
    echo "Usage: ./process_logo.sh <path_to_uploaded_image>"
    exit 1
fi

if [ ! -f "$INPUT_FILE" ]; then
    echo "Error: File $INPUT_FILE not found!"
    exit 1
fi

echo "Processing original user image: $INPUT_FILE"

# 1. Precise 1:1 center crop to square to ensure exact aspect ratio of the emblem
# Get dimensions
DIM=$(identify -format "%w %h" "$INPUT_FILE")
WIDTH=$(echo $DIM | cut -d' ' -f1)
HEIGHT=$(echo $DIM | cut -d' ' -f2)

echo "Input dimensions: ${WIDTH}x${HEIGHT}"

# Crop to center square based on the smaller dimension
MIN_DIM=$(( WIDTH < HEIGHT ? WIDTH : HEIGHT ))
convert "$INPUT_FILE" -gravity center -crop "${MIN_DIM}x${MIN_DIM}+0+0" +repage -resize "512x512" "app/src/main/res/drawable/drg_app_icon.jpg"

echo "Generated 512x512 cropped original logo at app/src/main/res/drawable/drg_app_icon.jpg"

# 2. Generate all multi-density launcher mipmap icons directly from original file
for density in "mdpi:48" "hdpi:72" "xhdpi:96" "xxhdpi:144" "xxxhdpi:192"; do
  folder="app/src/main/res/mipmap-${density%%:*}"
  size="${density##*:}"
  convert "app/src/main/res/drawable/drg_app_icon.jpg" -resize "${size}x${size}" "$folder/ic_launcher.webp"
  convert "app/src/main/res/drawable/drg_app_icon.jpg" -resize "${size}x${size}" "$folder/ic_launcher_round.webp"
  echo "Updated $folder at ${size}x${size}"
done

echo "All icons successfully created using exact original file with zero AI modification!"
