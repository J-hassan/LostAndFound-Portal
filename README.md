# 🔍 Lost & Found Management System (Cloud Edition)

A professional JavaFX desktop application now upgraded with **Cloud Sync**. This system allows users to report and match lost/found items across a centralized network using a remote MySQL database.

---

## 🆕 What's New in v1.1.0 (Cloud Migration)
*   **Centralized Database:** Migrated from local `.dat` files to a hosted **MySQL (Clever Cloud)** database.
*   **Real-time Global Sync:** Items reported by one user are instantly visible to all users worldwide.
*   **Persistent User Auth:** Secure cloud-based login and registration system.
*   **Cloud Notifications:** Automated matching engine now saves potential matches directly to the cloud.

---

## ✨ Key Features
*   **Smart Matching Engine:** Scores items based on name, category, and location to find the best matches.
*   **Real-time UI:** Built with **JavaFX** and **CSS** featuring a modern Glassmorphism design.
*   **Async Loading:** Includes background tasks and loading indicators for a smooth cloud experience.
*   **One-Click Installer:** Self-contained `.exe` that includes the Java Runtime (JRE).

---

## 🛠️ Tech Stack
*   **Language:** Java 17+
*   **UI Framework:** JavaFX 13
*   **Database:** MySQL (Cloud Hosted)
*   **Build Tool:** Maven
*   **Distribution:** jpackage (Native Windows Installer)

---

## 🚀 Installation
1.  Go to the [Releases](https://github.com/J-hassan/LostAndFound-Portal/releases/tag/v1.1.0) section.
2.  Download the latest **`LostAndFound_v1.1.0.exe`**.
3.  Install and run. (Note: Internet connection is required for cloud features).
4.  *Security Tip:* If Windows SmartScreen appears, click **"More Info"** -> **"Run Anyway"**.

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
git clone https://github.com/J-hassan/LostAndFound-Portal.git
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
*   Role: Lead Java Developer

---

## 📜 License
Distributed under the MIT License. See `LICENSE` for more information.

This project is licensed under the **MIT License**.
