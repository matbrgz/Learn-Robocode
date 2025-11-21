# Makefile for Learn-Robocode Project

# --- Configuration ---
# ROBOCODE_HOME should point to your Robocode installation directory.
# The install.sh script will create a 'robocode_local' directory by default.
ROBOCODE_HOME ?= $(CURDIR)/robocode_local

# Output directory for compiled classes
BIN_DIR = bin

# Source directory for Java files
SRC_DIR = src/mega

# Robot Info
ROBOT_CLASS = mega.Boilerplate
ROBOT_VERSION = 1.0
ROBOT_JAR = $(ROBOT_CLASS)_$(ROBOT_VERSION).jar

# --- Targets ---

.PHONY: all build package install clean help

all: build

# Builds the Robocode robot(s) by compiling Java source files and copying resources.
build:
	@echo "--- Building Robocode robots ---"
	@if [ ! -f "$(ROBOCODE_HOME)/libs/robocode.jar" ]; then \
		echo "Error: Robocode is not installed. Please run 'make install' first."; \
		exit 1; \
	fi
	@rm -rf $(BIN_DIR)
	@mkdir -p $(BIN_DIR)/mega
	javac -cp "$(ROBOCODE_HOME)/libs/robocode.jar" -d $(BIN_DIR) $(SRC_DIR)/*.java
	@cp $(SRC_DIR)/*.properties $(BIN_DIR)/mega/
	@echo "Build complete."

# Packages the compiled classes into a JAR file.
package: build
	@echo "--- Packaging robot into JAR file: $(ROBOT_JAR) ---"
	@jar -cvf $(ROBOT_JAR) -C $(BIN_DIR) .

# Installs Robocode (if needed) and the robot JAR.
install: package
	@echo "--- Running installation script (if necessary) ---"
	@./install.sh
	@echo "--- Installing robot JAR to $(ROBOCODE_HOME)/robots/ ---"
	@cp $(ROBOT_JAR) "$(ROBOCODE_HOME)/robots/"
	@echo "Robot $(ROBOT_JAR) installed."
	@echo "To use the robot, open Robocode, go to Options -> Preferences -> Development Options,"
	@echo "and add the full path to this project's directory to the development paths."
	@echo "Then, create a new battle and find 'mega.Boilerplate' in the list of robots."


# Cleans up compiled files and generated battle files/logs.
clean:
	@echo "--- Cleaning up project ---"
	@rm -rf $(BIN_DIR) $(ROBOT_JAR)
	@rm -rf robocode_local # Remove locally downloaded Robocode
	@echo "Clean up complete."

help:
	@echo "Makefile for Learn-Robocode Project"
	@echo ""
	@echo "Usage:"
	@echo "  make build         - Compiles the Java source files."
	@echo "  make package       - Packages the compiled classes into a JAR file."
	@echo "  make install       - Installs Robocode and the robot JAR."
	@echo "  make clean         - Removes compiled classes and generated files."
	@echo ""
	@echo "After running 'make install', you must run battles manually from the Robocode GUI."
	@echo ""
	@echo "Configuration:"
	@echo "  ROBOCODE_HOME: $(ROBOCODE_HOME) (Can be overridden by environment variable)"
