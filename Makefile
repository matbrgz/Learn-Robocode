# Makefile for Learn-Robocode Project

# --- Configuration ---
# Robocode Version
ROBOCODE_VERSION = 1.9.3.6
ROBOCODE_BINARY_ZIP = robocode-$(ROBOCODE_VERSION)-binary.zip
ROBOCODE_URL = https://downloads.sourceforge.net/project/robocode/robocode-stable/$(ROBOCODE_VERSION)/$(ROBOCODE_BINARY_ZIP)

# Local directory for Robocode installation if not provided by user
ROBOCODE_LOCAL_INSTALL_DIR = robocode_local

# User-provided ROBOCODE_HOME (can be overridden by environment variable)
# If not set by user, it defaults to the local downloaded Robocode directory.
ROBOCODE_HOME ?= $(CURDIR)/$(ROBOCODE_LOCAL_INSTALL_DIR)

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

.PHONY: all build install battle clean help download_robocode copy_robots

all: build

# Builds the Robocode robot(s) by compiling Java source files.
build:
	@echo "--- Building Robocode robots ---"
	@mkdir -p $(BIN_DIR)
	@if [ ! -f "$(ROBOCODE_HOME)/libs/robocode.jar" ]; then \
		echo "Error: $(ROBOCODE_HOME)/libs/robocode.jar not found."; \
		echo "Please run 'make install' first or ensure ROBOCODE_HOME is set correctly."; \
		exit 1; \
	fi
	javac -cp "$(ROBOCODE_HOME)/libs/robocode.jar" -d $(BIN_DIR) $(SRC_DIR)/*.java
	@echo "Build complete. Classes are in $(BIN_DIR)"

# Installs the compiled robot classes and ensures Robocode is present.
install: download_robocode copy_robots

# Downloads and extracts Robocode setup if not already present at $(ROBOCODE_HOME)
download_robocode:
	@if [ ! -d "$(ROBOCODE_HOME)/libs" ]; then \
		echo "--- Robocode not found at $(ROBOCODE_HOME), attempting to download and extract ---"; \
		mkdir -p "$(ROBOCODE_LOCAL_INSTALL_DIR)"; \
		if [ ! -f "$(ROBOCODE_LOCAL_INSTALL_DIR)/$(ROBOCODE_BINARY_ZIP)" ]; then \
			echo "Downloading Robocode $(ROBOCODE_VERSION) from SourceForge... This may take a moment."; \
			curl -L -o "$(ROBOCODE_LOCAL_INSTALL_DIR)/$(ROBOCODE_BINARY_ZIP)" "$(ROBOCODE_URL)"; \
			if [ $$? -ne 0 ]; then \
				echo "Error: Download failed for Robocode from $(ROBOCODE_URL)."; \
				exit 1; \
			fi; \
		else \
			echo "Robocode binary zip already present: $(ROBOCODE_LOCAL_INSTALL_DIR)/$(ROBOCODE_BINARY_ZIP)"; \
		fi; \
		echo "Extracting Robocode to $(ROBOCODE_LOCAL_INSTALL_DIR)..."; \
		unzip -o "$(ROBOCODE_LOCAL_INSTALL_DIR)/$(ROBOCODE_BINARY_ZIP)" -d "$(ROBOCODE_LOCAL_INSTALL_DIR)"; \
		if [ $$? -ne 0 ]; then \
			echo "Error: Failed to extract Robocode binary zip. It might be corrupted or not a valid zip file."; \
			exit 1; \
		fi; \
		if [ ! -f "$(ROBOCODE_HOME)/libs/robocode.jar" ]; then \
			echo "Error: robocode.jar not found in extracted directory. Robocode installation might be incomplete."; \
			exit 1; \
		fi; \
		echo "Robocode $(ROBOCODE_VERSION) successfully installed locally to $(ROBOCODE_LOCAL_INSTALL_DIR)" \
	else \
		echo "--- Robocode already found at $(ROBOCODE_HOME) ---"; \
	fi

# Copies the compiled robots into the Robocode installation's robots directory.
copy_robots: build
	@echo "--- Copying $(MAIN_ROBOT) to $(ROBOCODE_HOME)/robots/mega/ ---"
	@mkdir -p "$(ROBOCODE_HOME)/robots/mega/"
	@cp -r $(BIN_DIR)/* "$(ROBOCODE_HOME)/robots/mega/"
	@echo "Robot $(MAIN_ROBOT) installed to $(ROBOCODE_HOME)/robots/mega/"
	@echo "Installation complete. If Robocode GUI is open, you may need to refresh it (Battle -> New Battle)."

# Creates a .battle file for benchmarking and runs it.
# The battle will consist of NUM_BENCHMARK_ROBOTS instances of our MAIN_ROBOT.
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
	@echo "  make install       - Ensures Robocode is available (downloads if necessary)"
	@echo "                       and copies compiled robots to the Robocode installation."
	@echo "  make battle        - Builds and installs the robots, then runs a benchmark battle"
	@echo "                       with $(NUM_BENCHMARK_ROBOTS) instances of $(MAIN_ROBOT)."
	@echo "                       Results are saved to $(RESULTS_XML)."
	@echo "  make clean         - Removes compiled classes, generated battle files/logs,"
	@echo "                       and the locally downloaded Robocode installation (if any)."
	@echo "  make help          - Displays this help message."
	@echo ""
	@echo "Configuration:"
	@echo "  ROBOCODE_HOME: $(ROBOCODE_HOME) (Can be overridden by environment variable)"
	@echo "                 If not set, Robocode will be downloaded to $(ROBOCODE_LOCAL_INSTALL_DIR)."
	@echo "  ROBOCODE_VERSION: $(ROBOCODE_VERSION)"
	@echo "  NUM_ROUNDS: $(NUM_ROUNDS)"
	@echo "  NUM_BENCHMARK_ROBOTS: $(NUM_BENCHMARK_ROBOTS)"
	@echo ""
	@echo "Note: 'curl -L', 'unzip', and 'seq' commands are required for automatic Robocode download and battle generation."