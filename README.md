# Parallel Image Filtering Experiment

This repository contains programs for applying mean and median filters to RGB color images using both sequential and parallel algorithms implemented in Java. The aim of the experiment is to analyze the speedup and effectiveness of parallelization compared to sequential processing.

## Introduction

This report presents an experiment comparing the performance of sequential and parallel algorithms for image filtering. Four programs were developed: `MeanFilterSerial`, `MedianFilterSerial`, `MeanFilterParallel`, and `MedianFilterParallel`. The experiment investigates the speedup achieved by parallel algorithms across different image sizes and filter sizes.

## Method Description

- **MeanFilterSerial.java**: Implements a serial mean filter by calculating the average of surrounding pixels within a window.
- **MedianFilterSerial.java**: Implements a serial median filter by calculating the median value of surrounding pixels within a window.
- **MeanFilterParallel.java & MedianFilterParallel.java**: Implements parallel mean and median filters using the Java Fork/Join framework. These classes divide the image into smaller tasks and process them in parallel.

## Repository Structure

- `MeanFilterSerial.java`: Serial implementation of mean filter.
- `MedianFilterSerial.java`: Serial implementation of median filter.
- `MeanFilterParallel.java`: Parallel implementation of mean filter using Fork/Join framework.
- `MedianFilterParallel.java`: Parallel implementation of median filter using Fork/Join framework.
- `ExperimentReport.pdf`: Detailed report of the experiment including methods, results, and conclusions.
