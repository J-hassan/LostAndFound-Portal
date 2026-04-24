# 🔍 Lost & Found Management System

A sleek, modern desktop application designed to bridge the gap between people who have lost items and those who have found them. Featuring a **Smart Matching Engine**, this app automatically connects users based on item descriptions, categories, and locations. 

---

## ✨ Key Features

*   **Smart Matching Engine**: Uses a weighted scoring system to match items based on Name, Category, Location, and Date.
*   **Persistent Notifications**: Real-time notification badges alert users when a potential match is found.
*   **Glassmorphism UI**: A modern, semi-transparent user interface styled with JavaFX and CSS.
*   **Custom Window Controls**: A custom-built title bar for a seamless, professional look.
*   **Data Persistence**: Securely stores user data, items, and notifications using Java Serialization in the user's home directory.
*   **One-Click Installer**: Fully packaged `.exe` for Windows—no need to install Java or JavaFX separately.

---

## 🛠️ Built With

*   **Java 17+** - Core application logic.
*   **JavaFX 13** - Rich desktop UI components.
*   **Maven** - Dependency and build management.
*   **Scene Builder** - FXML layout design.

---

## 🚀 Installation & Setup

1.  **Download**: Navigate to the [Releases](https://github.com) section and download the latest `LostAndFound.exe` installer.
2.  **Install**: Run the installer and follow the on-screen instructions.
3.  **Security Note**: Since this is an unsigned developer build, Windows SmartScreen may show a warning. Click **"More Info"** and then **"Run Anyway"** to proceed.

---

## 📸 Preview


| Login Interface | User Dashboard |
| :--- | :--- |
| ![Login Screenshot](path/to/login-image.png) | ![Dashboard Screenshot](path/to/dashboard-image.png) |

---

## 🏗️ Development Setup

If you want to contribute or build from source:

### Clone the repository:
```bash
git clone https://github.com
```

### Build the project:
```bash
mvn clean package
```

### Run the application:
```bash
java -jar target/demo-1.0-SNAPSHOT.jar
```

---

## 👤 Author

**Muhammad Hassan**  
*   GitHub: [@J-hassan](https://github.com)  
*   Role: Lead Developer

---

## 📄 License

This project is licensed under the **MIT License**.
