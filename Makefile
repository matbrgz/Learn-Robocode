# Makefile for Learn-Robocode Project

# --- Configuration ---
# Robocode Version
ROBOCODE_VERSION = 1.10.0
ROBOCODE_SETUP_JAR = robocode-$(ROBOCODE_VERSION)-setup.jar
ROBOCODE_URL = https://sourceforge.net/projects/robocode/files/robocode/$(ROBOCODE_VERSION)/$(ROBOCODE_SETUP_JAR)/download

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

# Downloads and installs Robocode using the setup JAR in headless mode.
download_robocode:
	@if [ ! -d "$(ROBOCODE_HOME)/libs" ]; then \
		echo "--- Robocode not found at $(ROBOCODE_HOME), attempting to download and install ---"; \
		mkdir -p "$(ROBOCODE_LOCAL_INSTALL_DIR)"; \
		if [ ! -f "$(ROBOCODE_LOCAL_INSTALL_DIR)/$(ROBOCODE_SETUP_JAR)" ]; then \
			echo "Downloading Robocode $(ROBOCODE_VERSION) setup from SourceForge... This may take a moment."; \
			curl -L -o "$(ROBOCODE_LOCAL_INSTALL_DIR)/$(ROBOCODE_SETUP_JAR)" "$(ROBOCODE_URL)"; \
			if [ $$? -ne 0 ] || [ ! -s "$(ROBOCODE_LOCAL_INSTALL_DIR)/$(ROBOCODE_SETUP_JAR)" ]; then \
				echo "Error: Download failed for Robocode from $(ROBOCODE_URL). File is missing or empty."; \
				exit 1; \
			fi; \
		else \
			echo "Robocode setup JAR already present: $(ROBOCODE_LOCAL_INSTALL_DIR)/$(ROBOCODE_SETUP_JAR)"; \
		fi; \
		echo "Generating auto-install.xml for headless installation..."; \
		echo '<izpack:installation version="5.0">' > auto-install.xml; \
		echo '    <installpath>$(CURDIR)/$(ROBOCODE_LOCAL_INSTALL_DIR)</installpath>' >> auto-install.xml; \
		echo '    <pack name="Robocode" index="0" selected="true"/>' >> auto-install.xml; \
		echo '</izpack:installation>' >> auto-install.xml; \
		echo "Running Robocode installer headlessly from within its directory..."; \
		(cd $(ROBOCODE_LOCAL_INSTALL_DIR) && java -jar $(ROBOCODE_SETUP_JAR) ../auto-install.xml); \
		rm auto-install.xml; \
		if [ ! -f "$(ROBOCODE_HOME)/libs/robocode.jar" ]; then \
			echo "Error: robocode.jar not found after installation. The headless installation might have failed."; \
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
	@rm -rf $(BIN_DIR) $(BATTLE_FILE) $(RESULTS_XML) robocode-debug.log auto-install.xml
	@rm -rf $(ROBOCODE_LOCAL_INSTALL_DIR) # Remove locally downloaded Robocode
	@echo "Clean up complete."

help:
	@echo "Makefile for Learn-Robocode Project"
	@echo ""
	@echo "Usage:"
	@echo "  make build         - Compiles the Java source files."
	@echo "  make install       - Ensures Robocode is available (downloads if necessary)"
	@echo "                       and copies compiled robots to the Robocode installation."
	@echo "  make battle        - Builds and installs the robot, then runs a benchmark battle."
	@echo "  make clean         - Removes compiled classes and generated battle files/logs."
	@echo ""
	@echo "Setup:"
	@echo "  The 'make install' target will automatically download and install Robocode if not found."
	@echo ""
	@echo "Configuration:"
	@echo "  ROBOCODE_HOME: $(ROBOCODE_HOME) (Can be overridden by environment variable)"
