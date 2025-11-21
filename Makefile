# Makefile for Learn-Robocode Project

# --- Configuration ---
# ROBOCODE_HOME should point to your Robocode installation directory.
# This directory should contain `libs/robocode.jar` and a `robots` directory.
#
# If you place the robocode.jar in `robocode_local/libs/` as per the README,
# this default setting will work.
ROBOCODE_HOME ?= $(CURDIR)/robocode_local

# Output directory for compiled classes
BIN_DIR = bin/mega

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

# Name of the results XML file
RESULTS_XML = benchmark_results.xml

# --- Targets ---

.PHONY: all build install battle clean help

all: build

# Builds the Robocode robot(s) by compiling Java source files.
build:
	@echo "--- Building Robocode robots ---"
	@if [ ! -f "$(ROBOCODE_HOME)/libs/robocode.jar" ]; then \
		echo "Error: $(ROBOCODE_HOME)/libs/robocode.jar not found."; \
		echo "Please manually download robocode.jar and place it in '$(ROBOCODE_HOME)/libs/' as per README.md instructions."; \
		exit 1; \
	fi
	@mkdir -p $(BIN_DIR)
	javac -cp "$(ROBOCODE_HOME)/libs/robocode.jar" -d $(BIN_DIR) $(SRC_DIR)/*.java
	@echo "Build complete. Classes are in $(BIN_DIR)"

# Installs the compiled robot classes into the Robocode robots directory.
install: build
	@echo "--- Copying robot to $(ROBOCODE_HOME)/robots/mega/ ---"
	@if [ ! -d "$(ROBOCODE_HOME)/robots" ]; then \
		mkdir -p "$(ROBOCODE_HOME)/robots"; \
	fi
	@mkdir -p "$(ROBOCODE_HOME)/robots/mega/"
	@cp -r $(BIN_DIR)/* "$(ROBOCODE_HOME)/robots/mega/"
	@echo "Robot $(MAIN_ROBOT) installed to $(ROBOCODE_HOME)/robots/mega/"

# Creates a .battle file for benchmarking and runs it.
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

	@echo "--- Running benchmark battle (this may take a while) ---"
	@java -Xmx512M -Dsun.java2d.noddraw=true -cp "$(ROBOCODE_HOME)/libs/robocode.jar" robocode.Robocode \
		-battle $(BATTLE_FILE) \
		-results $(RESULTS_XML) \
		-nodisplay \
		-hidden \
		-nosound
	@echo "Benchmark battle finished. Results saved to $(RESULTS_XML)."
	@echo "You can view the raw XML results in $(RESULTS_XML)."
	@echo "To extract summary, you might need a script or manual inspection."

# Cleans up compiled files and generated battle files/logs.
clean:
	@echo "--- Cleaning up project ---"
	@rm -rf $(BIN_DIR) $(BATTLE_FILE) $(RESULTS_XML) robocode-debug.log
	@rm -rf $(ROBOCODE_LOCAL_INSTALL_DIR) # Remove locally downloaded Robocode
	@echo "Clean up complete."

help:
	@echo "Makefile for Learn-Robocode Project"
	@echo ""
	@echo "Usage:"
	@echo "  make build         - Compiles the Java source files."
	@echo "  make install       - Builds and installs the robot to the local Robocode directory."
	@echo "  make battle        - Builds and installs the robot, then runs a benchmark battle."
	@echo "  make clean         - Removes compiled classes and generated battle files/logs."
	@echo ""
	@echo "Setup:"
	@echo "  You must manually download 'robocode.jar' and place it in 'robocode_local/libs/'."
	@echo "  See README.md for detailed setup instructions."
	@echo ""
	@echo "Configuration:"
	@echo "  ROBOCODE_HOME: $(ROBOCODE_HOME) (Can be overridden by environment variable)"
