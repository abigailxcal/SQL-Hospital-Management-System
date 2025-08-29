# Hospital Management System
## Overview
This project implements a relational database system to manage hospital operations for a growing retirement community. The system supports patient admissions, room assignments, staff management, treatments, and diagnoses. A simple Java interface executes SQL queries and displays results.

## Features
- Database Design: ER diagram translated into normalized relational tables.
- Data Population: Tables created and populated using SQL (CREATE TABLE, INSERT).
- SQL Queries:
    - Room utilization (occupied/unoccupied tracking)
    - Patient admissions, discharges, and history
    - Diagnosis frequency and treatment records
    - Employee listings and doctor assignment analysis
    - Interface: Java menu to select and execute SQL queries.

## Requirements
- Java (JDK 8+)
- MySQL Workbench
- MySQL Server
- MySQL Connector/J (JDBC Driver)

## Setup Instructions


1. **Run the Program**:
    - Compile the Java files:
        ```bash
        javac -cp .:/path/to/mysql-connector-java.jar HospitalManagement/*.java
        ```
    - Run the main program:
        ```bash
        java -cp .:/path/to/mysql-connector-java.jar HospitalManagement.Driver
        ```
    - Replace `/path/to/mysql-connector-java.jar` with the actual path to your MySQL Connector/J `.jar` file.

