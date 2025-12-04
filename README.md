# RapidExpress - Logistics and Delivery System

A comprehensive Java-based logistics and delivery management system built with Maven and MySQL.

## Features

- **Vehicle Management**: Complete CRUD operations for fleet management
- **Driver Management**: Driver registration and status tracking
- **Package Management**: Package tracking with unique codes
- **Route Planning**: Route creation and tracking system
- **Maintenance Tracking**: Vehicle maintenance records
- **Reports**: Delivery reports and vehicle history
- **Audit System**: Complete operation logging
- **User Authentication**: Role-based access control (Admin, Operator, Driver, Auditor)

## Technologies

- Java 17
- Maven 3.9.9
- MySQL 8
- JDBC

## Setup

1. Install MySQL and create the database:
   ```sql
   source database/1_schema_ddl.sql
   source database/2_data_dml.sql
   ```

2. Update database credentials in `src/main/java/com/rapidexpress/utils/ConexionBD.java`

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="com.rapidexpress.Main"
   ```

## Default Credentials

- Username: `admin`
- Password: `password123`

## Project Structure

```
src/main/java/com/rapidexpress/
├── dao/          # Data Access Objects
├── model/        # Entity models
├── service/      # Business logic
└── utils/        # Utilities
```

## Authors

- Team Member 1
- Team Member 2
- Team Member 3

