#!/bin/bash

# --- Configuration ---
ROBOCODE_VERSION="1.10.0"
ROBOCODE_SETUP_JAR="robocode-${ROBOCODE_VERSION}-setup.jar"
ROBOCODE_URL="https://sourceforge.net/projects/robocode/files/robocode/${ROBOCODE_VERSION}/${ROBOCODE_SETUP_JAR}/download"
INSTALL_DIR="robocode_local"

# --- Installation Logic ---

set -e # Exit immediately if a command exits with a non-zero status.

# Check if Robocode is already installed
if [ -f "${INSTALL_DIR}/libs/robocode.jar" ]; then
    echo "--- Robocode already found at ${INSTALL_DIR} ---"
    exit 0
fi

echo "--- Robocode not found at ${INSTALL_DIR}, attempting to download and extract ---"

# Create installation directory
mkdir -p "${INSTALL_DIR}"

# Download the setup JAR if it doesn't exist
if [ ! -f "${INSTALL_DIR}/${ROBOCODE_SETUP_JAR}" ]; then
    echo "Downloading Robocode ${ROBOCODE_VERSION} setup from SourceForge... This may take a moment."
    curl -L -o "${INSTALL_DIR}/${ROBOCODE_SETUP_JAR}" "${ROBOCODE_URL}"
    if [ ! -s "${INSTALL_DIR}/${ROBOCODE_SETUP_JAR}" ]; then
        echo "Error: Download failed for Robocode from ${ROBOCODE_URL}. File is missing or empty."
        exit 1
    fi
else
    echo "Robocode setup JAR already present: ${INSTALL_DIR}/${ROBOCODE_SETUP_JAR}"
fi

# Extract the setup JAR using unzip
echo "Extracting Robocode from setup JAR to ${INSTALL_DIR}..."
unzip -o "${INSTALL_DIR}/${ROBOCODE_SETUP_JAR}" -d "${INSTALL_DIR}"

# Check for successful installation
if [ ! -f "${INSTALL_DIR}/libs/robocode.jar" ]; then
    echo "Error: robocode.jar not found after extraction. The installation might have failed."
    exit 1
fi

echo "Robocode ${ROBOCODE_VERSION} successfully installed locally to ${INSTALL_DIR}"
exit 0
