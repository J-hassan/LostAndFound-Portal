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

1.  **Download**: Navigate to the [Releases](https://github.com/J-hassan/LostAndFound-Portal/releases/tag/v1.0.0) section and download the latest `LostAndFound.exe` installer.
2.  **Install**: Run the installer and follow the on-screen instructions.
3.  **Security Note**: Since this is an unsigned developer build, Windows SmartScreen may show a warning. Click **"More Info"** and then **"Run Anyway"** to proceed.

---

## 📸 Preview


# Login Interface 
<img width="1738" height="901" alt="image" src="https://github.com/user-attachments/assets/2f965692-60de-4a87-9456-78ceb703477f" />


# User Dashboard 
<img width="1730" height="918" alt="image" src="https://github.com/user-attachments/assets/de88810b-2fda-4099-a824-e93a14333671" />



---

## 🏗️ Development Setup

If you want to contribute or build from source:

### Clone the repository:
```bash
git clone [https://github.com/J-hassan/LostAndFound-Portal.git]
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
