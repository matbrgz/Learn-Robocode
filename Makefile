# Makefile for Learn-Robocode Project

# --- Configuration ---
# ROBOCODE_HOME should point to your Robocode installation directory.
# The install.sh script will create a 'robocode_local' directory by default.
ROBOCODE_HOME ?= $(CURDIR)/robocode_local

# Output directory for compiled classes
BIN_DIR = bin

# Source directory for Java files
SRC_DIR = src/mega

# Name of the main robot class (fully qualified)
MAIN_ROBOT = mega.Boilerplate

# Number of rounds for the benchmark battle
NUM_ROUNDS = 100

# Number of robots in the benchmark battle (including our main robot)
NUM_BENCHMARK_ROBOTS = 10

# Name of the battle file to generate
BATTLE_FILE = benchmark.battle

# Name of the results file
RESULTS_FILE = benchmark_results.txt

# --- Targets ---

.PHONY: all build install battle clean help

all: build

# Builds the Robocode robot(s) by compiling Java source files.
build:
	@echo "--- Building Robocode robots ---"
	@if [ ! -f "$(ROBOCODE_HOME)/libs/robocode.jar" ]; then \
		echo "Error: Robocode is not installed. Please run 'make install' first."; \
		exit 1; \
	fi
	@mkdir -p $(BIN_DIR)
	javac -cp "$(ROBOCODE_HOME)/libs/robocode.jar" -d $(BIN_DIR) $(SRC_DIR)/*.java
	@echo "Build complete. Classes are in $(BIN_DIR)"

# Installs Robocode (if needed) and the compiled robot.
install: build
	@echo "--- Running installation script (if necessary) ---"
	@./install.sh
	@echo "--- Cleaning and copying robot to $(ROBOCODE_HOME)/robots/mega/ ---"
	@rm -rf "$(ROBOCODE_HOME)/robots/mega"
	@mkdir -p "$(ROBOCODE_HOME)/robots/mega"
	@(cd $(BIN_DIR) && cp -r mega/* "$(ROBOCODE_HOME)/robots/mega/")
	@echo "Robot $(MAIN_ROBOT) installed to $(ROBOCODE_HOME)/robots/mega/"

# Creates a .battle file and runs it with the Robocode GUI.
battle: install
	@echo "--- Generating benchmark battle file: $(BATTLE_FILE) ---"
	@echo "#Robocode Battle file" > $(BATTLE_FILE)
	@echo "numRounds=$(NUM_ROUNDS)" >> $(BATTLE_FILE)
	@echo "battlefield.width=800" >> $(BATTLE_FILE)
	@echo "battlefield.height=600" >> $(BATTLE_FILE)
	@echo "gunCoolingRate=0.1" >> $(BATTLE_FILE)
	@echo "inactivityTime=450" >> $(BATTLE_FILE)
	@echo "hideEnemyNames=false" >> $(BATTLE_FILE)
	@echo "sentryRobot=null" >> $(BATTLE_FILE)
	@for i in $(seq 1 $(NUM_BENCHMARK_ROBOTS)); do \
		echo "robot.$${i}=$(MAIN_ROBOT) $(MAIN_ROBOT)$${i}" >> $(BATTLE_FILE); \
	done
	@echo "Generated $(BATTLE_FILE) with $(NUM_BENCHMARK_ROBOTS) instances of $(MAIN_ROBOT)."

	@echo "--- Starting Robocode battle with GUI ---"
	@java -Xmx512M \
		-Dsun.java2d.noddraw=true \
		--add-opens java.base/sun.net.www.protocol.jar=ALL-UNNAMED \
		-cp "$(ROBOCODE_HOME)/libs/robocode.jar:$(CURDIR)/bin" \
		robocode.Robocode \
		-battle "$(CURDIR)/$(BATTLE_FILE)" \
		-results "$(CURDIR)/$(RESULTS_FILE)" \
		-tps 30 \
		-nosound

# Cleans up compiled files and generated battle files/logs.
clean:
	@echo "--- Cleaning up project ---"
	@rm -rf $(BIN_DIR) $(BATTLE_FILE) $(RESULTS_FILE) robocode-debug.log battle.log
	@rm -rf robocode_local # Remove locally downloaded Robocode
	@echo "Clean up complete."

help:
	@echo "Makefile for Learn-Robocode Project"
	@echo ""
	@echo "Usage:"
	@echo "  make build         - Compiles the Java source files."
	@echo "  make install       - Installs Robocode and the robot using install.sh."
	@echo "  make battle        - Installs and builds the robot, then runs a benchmark battle with the GUI."
	@echo "  make clean         - Removes compiled classes and generated battle files/logs."
	@echo ""
	@echo "Setup:"
	@echo "  The 'make install' target will automatically download and install Robocode if not found."
	@echo ""
	@echo "Configuration:"
	@echo "  ROBOCODE_HOME: $(ROBOCODE_HOME) (Can be overridden by environment variable)"
