# Learn-Robocode

This repository aims to store studies about the Robocode game, which involves developing a robot in Java for battles against other robots.

The intention is to maintain this repository as a study and reference site for anyone who wants to learn about the game, from basic to advanced levels.

Any contribution is welcome!

## Overview

This project serves as a comprehensive boilerplate and educational resource for students and enthusiasts looking to dive into Robocode. It provides a structured approach to learning various aspects of robot programming, from fundamental movement and targeting techniques to advanced AI implementations.

## Getting Started

### Prerequisites

*   **Java Development Kit (JDK):** Ensure you have a JDK installed (Java 8 or higher is typically required).

### Setup and Installation

This project uses a `Makefile` to simplify the build process. The installation of Robocode and running battles is a manual process.

**Step 1: Install Robocode Manually**
*   Run the `install.sh` script to download and extract Robocode into a `robocode_local` directory.
    ```bash
    bash install.sh
    ```

**Step 2: Build and Package Your Robot**
*   Use the `make` command to compile your robot and package it into a JAR file.
    ```bash
    make package
    ```

**Step 3: Install Your Robot**
*   This command copies your robot's JAR file into the Robocode `robots` directory.
    ```bash
    make install
    ```

### Running a Battle

After installing your robot, you must start and run battles from the Robocode GUI.

1.  **Start Robocode:** Run the `robocode.sh` (on Linux/macOS) or `robocode.bat` (on Windows) script from inside the `robocode_local` directory.
2.  **Create a New Battle:** In the Robocode application, go to `Battle -> New`.
3.  **Add Your Robot:** Find `mega.Boilerplate 1.0` in the list of robots and add it to the battle. You can add multiple instances or other sample robots to fight against.
4.  **Start Battle:** Click the "Start Battle" button.

## Preliminary Benchmark Results

To get benchmark results, create a battle with 10 instances of `mega.Boilerplate 1.0` for 100 rounds, run it, and manually record the final scores here.

