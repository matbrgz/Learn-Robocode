# Learn-Robocode

This repository aims to store studies about the Robocode game, which involves developing a robot in Java for battles against other robots.

The intention is to maintain this repository as a study and reference site for anyone who wants to learn about the game, from basic to advanced levels.

Any contribution is welcome!

## Overview

This project serves as a comprehensive boilerplate and educational resource for students and enthusiasts looking to dive into Robocode. It provides a structured approach to learning various aspects of robot programming, from fundamental movement and targeting techniques to advanced AI implementations.

## Getting Started

### Prerequisites

*   **Java Development Kit (JDK):** Ensure you have a JDK installed (Java 8 or higher is typically required).
*   **Robocode JAR:** You must manually download the Robocode JAR file.

### Manual Robocode Setup

Automated downloads of Robocode have proven unreliable. Please follow these manual setup steps:

1.  **Download Robocode:** Download the Robocode setup JAR (e.g., `robocode-1.10.0-setup.jar`) from the official GitHub releases page:
    *   **[Robocode Releases](https://github.com/robo-code/robocode/releases)**

2.  **Extract `robocode.jar`:** The downloaded file is an installer. You need to extract the core `robocode.jar` from it.
    *   **Option A (Run Installer):** Run the installer (`java -jar robocode-1.10.0-setup.jar`) and install Robocode to a temporary directory. Then, find the `libs/robocode.jar` file inside that installation.
    *   **Option B (Extract as ZIP):** A `.jar` file can often be treated like a `.zip` file. You can try to extract the contents of `robocode-1.10.0-setup.jar` using an archive manager and look for the `libs/robocode.jar` file inside.

3.  **Create Local Directory Structure:** In the root of this project, create the following directory structure:
    ```
    robocode_local/
    └── libs/
    ```

4.  **Place `robocode.jar`:** Move the `robocode.jar` you extracted in step 2 into the `robocode_local/libs/` directory.

The final project structure should look like this before you run `make`:
```
<project_root>/
├── robocode_local/
│   └── libs/
│       └── robocode.jar
├── src/
└── Makefile
```
Once this setup is complete, you can proceed with using the `Makefile`.

### Building and Running with `Makefile`

With `robocode.jar` in place, you can now use the `Makefile` to build and run the robot.

1.  **Build the Robot:** Compile the Java source files.
    ```bash
    make build
    ```

2.  **Install the Robot:** This copies the compiled classes into the local Robocode installation's `robots` directory.
    ```bash
    make install
    ```

3.  **Create a Benchmark Battle File:** This command will generate a battle file named `benchmark.battle`.
    ```bash
    make battle
    ```

4.  **Run the Benchmark Manually:**
    *   Open your Robocode application.
    *   Go to `Battle -> Open`.
    *   Select the `benchmark.battle` file from this project's root directory.
    *   Run the battle.

5.  **Clean Up:** Remove compiled classes and generated battle files.
    ```bash
    make clean
    ```

## Preliminary Benchmark Results

*(This section should be updated by running the benchmark battle manually and recording the results from the Robocode GUI.)*

To get benchmark results, run the battle as described above and fill in the results here.

*   **Total Score:** [e.g., 5000 points]
*   **Survival Rate:** [e.g., 60% (Robot survived 60 out of 100 rounds)]
*   **Win Rate:** [e.g., 10% (Robot was the last one standing in 10 rounds)]
*   **Damage Dealt:** [e.g., 1500 points]
*   **Damage Taken:** [e.g., 1000 points]
*   **Avg. Enemy Score:** [e.g., 450 points]

These results provide a baseline for evaluating further improvements to the robot's AI components.
