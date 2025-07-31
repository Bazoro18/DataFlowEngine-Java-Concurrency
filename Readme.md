# DataFlowEngine – Java Concurrency Exploration Project

**DataFlowEngine** is a hands-on, modular Java project built to explore concurrency, synchronization, and JVM internals through a realistic data pipeline. It simulates the ingestion, transformation, and aggregation of log events, allowing engineers to observe and experiment with various threading models, race conditions, memory visibility, deadlock prevention, and more.

The project is divided into components that isolate and demonstrate specific concurrency concepts, while remaining interconnected within a working pipeline.

This is intended as a learning-focused system for practicing Java concurrency and data flow design patterns—not a production-ready system.

## Project Structure

The project is organized as follows:

DataFlowEngine/
├── Main.java
├── ingestion/
│   └── CSVReader.java
├── pipeline/
│   ├── Stage.java
│   ├── TransformMap.java
│   ├── Aggregator.java
│   └── EventLoopEngine.java
├── concurrency/
│   ├── SemaphoreBuffer.java
│   ├── DeadlockSimulator.java
│   └── RaceConditionTest.java
├── virtualthreads/
│   └── VirtualThreadRunner.java
├── metrics/
│   └── MetricsPrinter.java
├── model/
│   └── LogEvent.java
└── resources/
    └── logs.csv

Each module serves a specific educational purpose, tied together by a central data flow through generated or ingested logs.

## Module Overview

Each folder in this project isolates a specific aspect of Java concurrency or pipeline behavior. Together, they simulate a complete data flow engine processing structured log events.

### ingestion/

Contains input components for feeding the pipeline. In the current setup, `CSVReader.java` reads logs from a CSV file and converts them into structured `LogEvent` objects. This simulates a basic ingestion system and helps explore single-threaded producers, blocking queues, and controlled input flow.

### pipeline/

Implements the core transformation and aggregation logic. This includes multiple composable stages such as mapping, filtering, and aggregation of log metrics. It is built to run in both single-threaded and multithreaded modes. It demonstrates how data flows through concurrent stages and how race conditions emerge during shared-state aggregation.

### concurrency/

Contains isolated modules that demonstrate specific concurrency problems and solutions. `RaceConditionTest.java` shows unsafe read-modify-write access and its remedies. `SemaphoreBuffer.java` implements flow control using semaphores. `DeadlockSimulator.java` intentionally causes and then helps avoid deadlocks. This folder is dedicated to hands-on learning of threading fundamentals and problem resolution.

### virtualthreads/

Demonstrates lightweight concurrency using Java virtual threads (introduced in Java 21). `VirtualThreadRunner.java` launches thousands of concurrent tasks using virtual threads to simulate high-concurrency workloads. This module explores modern concurrency strategies beyond thread pools.

### metrics/

Responsible for periodic reporting of computed metrics. `MetricsPrinter.java` runs as a background process that reads from the shared aggregator state and prints summaries of log traffic, latency, or counts. It showcases safe read access from concurrently updated structures.

### model/

Defines core domain objects used across the pipeline. `LogEvent.java` is the central data structure representing a single log entry. This shared model is passed between producer, transformer, and aggregator components, making it a contract across all modules.

### resources/

Contains static input data such as `logs.csv`. This folder is useful for reproducible experiments with known data, and can be replaced or extended with generated data for stream-like behavior.

### Main.java

Acts as the program's entry point. It wires together producers, queues, pipelines, thread pools, and metrics reporters. It can be modified to switch between different demo modes (race condition tests, deadlock simulation, etc.), making it the launcher for all experiments.
