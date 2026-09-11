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

DIM=$(identify -format "%w %h" "$INPUT_FILE")
WIDTH=$(echo $DIM | cut -d' ' -f1)
HEIGHT=$(echo $DIM | cut -d' ' -f2)
MIN_DIM=$(( WIDTH < HEIGHT ? WIDTH : HEIGHT ))

# 1. Generate 512x512 lossless PNG for in-app usage
convert "$INPUT_FILE" -gravity center -crop "${MIN_DIM}x${MIN_DIM}+0+0" +repage -resize "512x512" "app/src/main/res/drawable/drg_app_icon.png"
rm -f "app/src/main/res/drawable/drg_app_icon.jpg"

echo "Generated 512x512 cropped original logo at app/src/main/res/drawable/drg_app_icon.png"

# 2. Generate multi-density mipmap icons and adaptive foregrounds
for item in "mdpi:48:108" "hdpi:72:162" "xhdpi:96:216" "xxhdpi:144:324" "xxxhdpi:192:432"; do
  folder="app/src/main/res/mipmap-$(echo $item | cut -d: -f1)"
  size=$(echo $item | cut -d: -f2)
  canvas=$(echo $item | cut -d: -f3)
  fg_size=$(( canvas * 72 / 100 ))

  # Legacy icons
  convert "app/src/main/res/drawable/drg_app_icon.png" -resize "${size}x${size}" "$folder/ic_launcher.webp"
  convert "app/src/main/res/drawable/drg_app_icon.png" -resize "${size}x${size}" "$folder/ic_launcher_round.webp"

  echo "Updated $folder: icon=${size}x${size}"
done

# 3. Maintain standard ic_launcher_foreground.xml layer-list
cat << 'EOF' > app/src/main/res/drawable/ic_launcher_foreground.xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:width="66dp"
        android:height="66dp"
        android:drawable="@drawable/drg_app_icon"
        android:gravity="center" />
</layer-list>
EOF

echo "All icons successfully created using exact original file with zero AI modification!"

