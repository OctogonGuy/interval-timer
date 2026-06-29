# Interval Timer

Create repeating timers consisting of one or several intervals.

## Overview

This application allows the user to start timers of one or more intervals. What this means is that the first interval will elapse, an alarm will be played, and then the second interval will start, and so forth. Common uses include the Pomodoro Technique for time management, box breathing for breath management, and interval training for workouts.

Built with [JavaFX](https://openjfx.io/).

## Setup & Usage

### Prerequisites

You must have the following installed on your computer to run the application:

- Java JDK (version 25+)

### Installation

Perform the following steps to be able to run the application:

1. Clone the repository.
2. Navigate to the repository.
3. Add execute permissions to `mvnw`.
   ```
   chmod +x mvnw
   ```

### Running

In the directory of the repository, run the following command:

```
./mvnw javafx:run
```

## Features

- **Custom intervals**: Create any number of intervals, each of whatever length you like, and each with an alert sound of your choice. Additionaly, specify whether the timer should repeat after all intervals are over and whether all intervals should share the same alert.
- **Presets**: Quickly create a common type of interval timer with predefined parameters, such as Pomodoro, box breathing, and interval training.
- **Save data**: Reuse your current intervals next time you start up the application.
- **Customize UI**: Paint the UI a custom color.