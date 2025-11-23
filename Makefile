# Makefile for Learn-Robocode Project

# --- Configuration ---
# ROBOCODE_HOME should point to your Robocode installation directory.
# The install.sh script will create a 'robocode_local' directory by default.
ROBOCODE_HOME ?= $(CURDIR)/robocode_local

# Output directory for compiled classes
BIN_DIR = bin

# Source directory for Java files
SRC_DIR = src

# Robot Info
ROBOT_CLASS = mega.Boilerplate
ROBOT_VERSION = 1.0
ROBOT_JAR = $(ROBOT_CLASS)_$(ROBOT_VERSION).jar

# --- Targets ---

.PHONY: all build package install setup run battle clean help

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
	javac -cp "$(ROBOCODE_HOME)/libs/robocode.jar" -d $(BIN_DIR) $(SRC_DIR)/mega/*.java
	@cp $(SRC_DIR)/mega/*.properties $(BIN_DIR)/mega/
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

# Ensures the robot is installed and ready for battle.
setup: install
	@echo "Robot is installed. Battle file 'battles/strategy_showcase.battle' is ready."

# Runs the benchmark battle using the pre-defined showcase battle file.
run: setup
	@echo "--- Clearing Robocode robot cache ---"
	@rm -rf "$(ROBOCODE_HOME)/robots/.data"
	@echo "--- Starting Robocode battle with GUI ---"
	@java -Xmx512M \
		-Dsun.java2d.noddraw=true \
		--add-opens java.base/sun.net.www.protocol.jar=ALL-UNNAMED \
		-cp "$(ROBOCODE_HOME)/libs/robocode.jar" \
		robocode.Robocode \
		-battle "$(CURDIR)/battles/strategy_showcase.battle" \
		-tps 30

# Alias for 'run'
battle: run

# Cleans up compiled files and generated battle files/logs.
clean:
	@echo "--- Cleaning up project ---"
	@rm -rf $(BIN_DIR) $(BATTLE_FILE) $(RESULTS_FILE) robocode-debug.log battle.log $(ROBOT_JAR) jar-root
	@rm -rf robocode_local # Remove locally downloaded Robocode
	@echo "Clean up complete."

help:
	@echo "Makefile for Learn-Robocode Project"
	@echo ""
	@echo "Usage:"
	@echo "  make build         - Compiles the Java source files."
	@echo "  make package       - Packages the compiled classes into a JAR file."
	@echo "  make install       - Installs Robocode and the robot JAR."
	@echo "  make setup         - Verifies installation."
	@echo "  make run           - Starts the showcase battle with the GUI."
	@echo "  make battle        - Alias for 'make run'."
	@echo "  make clean         - Removes compiled classes and generated files."
	@echo ""
	@echo "Setup:"
	@echo "  The 'make install' target will automatically download and install Robocode if not found."
	@echo ""
	@echo "Configuration:"
	@echo "  ROBOCODE_HOME: $(ROBOCODE_HOME) (Can be overridden by environment variable)"