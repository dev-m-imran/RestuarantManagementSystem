# Restaurant Management System

A desktop-based Restaurant Management System built using **Java**, **JavaFX**, and **SQLite**.  
This application helps manage restaurant operations including inventory management, menu handling, order processing, and billing through a user-friendly graphical interface.



## Features

-  Modern GUI built with JavaFX
-  Dynamic menu management
-  Order placement and tracking
-  Automatic bill calculation
-  Receipt generation
-  SQLite database integration
-  Data persistence and retrieval



## Technologies Used

- **Java**
- **JavaFX**
- **SQLite**
- JDBC (Database Connectivity)

## Screenshots
Login Page:
<img width="1492" height="1050" alt="image" src="https://github.com/user-attachments/assets/76cdc57f-d5ee-47be-aefa-95df89f28a29" />
Registration Page
<img width="1487" height="1055" alt="image" src="https://github.com/user-attachments/assets/f89aed8c-e571-4bb2-a4b6-fe033fc61e5a" />
Inventory Page
<img width="2747" height="1560" alt="image" src="https://github.com/user-attachments/assets/36f8ac43-db7d-44bf-b238-300b48a8a0d2" />
Menu Page
<img width="2727" height="1557" alt="image" src="https://github.com/user-attachments/assets/4bcaad9c-836b-44c3-885e-91a961b18b48" />
Customers Page
<img width="2750" height="1572" alt="image" src="https://github.com/user-attachments/assets/c366ca75-5cc2-47f2-92d8-659d9042b9ac" />

## Setup and run locally
Follow the following steps to run the project locally

### 1. Prerequisites
Make sure you have the following installed before cloning:
- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [JavaFX SDK](https://openjfx.io/)
- [Git](https://git-scm.com/)
- An IDE such as [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/)

### Clone the Repository
```bash
git clone https://github.com/dev-m-imran/restaurant-management-system.git
cd restaurant-management-system
```

### Setup Instructions

1. Open the project in your IDE
2. Add the JavaFX SDK to your project libraries:
   - In IntelliJ: `File → Project Structure → Libraries → Add JavaFX SDK`
3. Configure VM options for JavaFX:
   --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
4. The SQLite database will be created automatically on first run — no setup needed
5. Run `Launcher.java` to launch the application

### Default Login Credentials
- Username: imran
- Password: 123456789
> You can register a new account from the Registration page
