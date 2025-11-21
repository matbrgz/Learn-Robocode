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

# Number of rounds for the benchmark battle
NUM_ROUNDS = 100

# Number of robots in the benchmark battle (including our main robot)
NUM_BENCHMARK_ROBOTS = 10

# Name of the battle file to generate
BATTLE_FILE = benchmark.battle

# --- Targets ---

.PHONY: all build package install battle clean help

all: build

# Builds the Robocode robot(s) by compiling Java source files.
build:
	@echo "--- Building Robocode robots ---"
	@if [ ! -f "$(ROBOCODE_HOME)/libs/robocode.jar" ]; then \
		echo "Error: Robocode is not installed. Please run 'make install' first."; \
		exit 1; \
	fi
	@rm -rf $(BIN_DIR)
	@mkdir -p $(BIN_DIR)
	javac -cp "$(ROBOCODE_HOME)/libs/robocode.jar" -d $(BIN_DIR) $(SRC_DIR)/*.java
	@echo "Build complete. Classes are in $(BIN_DIR)"

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

# Creates a .battle file for the user to run manually.
battle: install
	@echo "--- Generating benchmark battle file: $(BATTLE_FILE) ---"
	@echo "#Robocode Battle file" > $(BATTLE_FILE)
	@echo "robocode.battle.numRounds=$(NUM_ROUNDS)" >> $(BATTLE_FILE)
	@echo "robocode.battleField.width=800" >> $(BATTLE_FILE)
	@echo "robocode.battleField.height=600" >> $(BATTLE_FILE)
	@echo "robocode.battle.gunCoolingRate=0.1" >> $(BATTLE_FILE)
	@echo "robocode.battle.rules.inactivityTime=450" >> $(BATTLE_FILE)
	@ROBOT_LIST=""; \
	for i in $$(seq 1 $(NUM_BENCHMARK_ROBOTS)); do \
		ROBOT_LIST="$${ROBOT_LIST}$(ROBOT_CLASS) $(ROBOT_VERSION),"; \
	done; \
	echo "robocode.battle.selectedRobots=$${ROBOT_LIST}" >> $(BATTLE_FILE)
	@echo "Generated $(BATTLE_FILE) with $(NUM_BENCHMARK_ROBOTS) instances of $(ROBOT_CLASS)."
	@echo ""
	@echo "--- To run the benchmark, open Robocode, go to Battle -> Open, and select $(BATTLE_FILE) ---"

# Cleans up compiled files and generated battle files/logs.
clean:
	@echo "--- Cleaning up project ---"
	@rm -rf $(BIN_DIR) $(BATTLE_FILE) $(ROBOT_JAR)
	@rm -rf robocode_local # Remove locally downloaded Robocode
	@echo "Clean up complete."

help:
	@echo "Makefile for Learn-Robocode Project"
	@echo ""
	@echo "Usage:"
	@echo "  make build         - Compiles the Java source files."
	@echo "  make package       - Packages the compiled classes into a JAR file."
	@echo "  make install       - Installs Robocode and the robot JAR."
	@echo "  make battle        - Creates a benchmark battle file to be run from the GUI."
	@echo "  make clean         - Removes compiled classes and generated files."
	@echo ""
	@echo "Setup:"
	@echo "  The 'make install' target will automatically download and install Robocode if not found."
	@echo ""
	@echo "Configuration:"
	@echo "  ROBOCODE_HOME: $(ROBOCODE_HOME) (Can be overridden by environment variable)"
