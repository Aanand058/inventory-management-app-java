# Inventory Management System - JavaFX Project


## Overview

This JavaFX application serves as a GUI-based **Inventory Management System** tailored for a small manufacturing organization transitioning from a spreadsheet-based workflow to a more advanced system. It allows users to add, modify, delete, and search for both parts and products, with the capability to save and load data through files or a database.

---

## 🛠 Tools & Technologies Used

- **Java** – Primary programming language  
- **JavaFX** – GUI framework  
- **Scene Builder** – For designing FXML layouts  
- **Eclipse IDE** – Development environment  
- **MySQL** – Database for persistent storage  
- **JDBC** – Java Database Connectivity to communicate with MySQL

---

## Features

### ✅ Main Screen
- Search bar and list views for **Parts** and **Products**
- Buttons: `Add`, `Modify`, `Delete`, `Search`, `Exit`
- Additional buttons (Workshop Upgrade):
  - `Save to File`
  - `Load from File`
  - `Save to Database`
  - `Load from Database`

### ✅ Parts Module
- **Add Part** Screen
  - Support for `In-House` and `Outsourced` part types
  - Input fields for: ID, Name, Inventory, Price, Min, Max, Company/Machine ID
  - Save or Cancel actions
- **Modify Part** Screen
  - Editable pre-populated fields
  - Option to switch between part types

### ✅ Products Module
- **Add Product** Screen
  - Fields: ID, Name, Inventory, Price, Min, Max
  - Associate multiple parts
  - Remove associated parts
  - Save or Cancel actions
- **Modify Product** Screen
  - Pre-filled data with full editability
  - Part association management

### ✅ Backend Features
- Fully object-oriented structure with:
  - Inheritance
  - Encapsulation
  - Abstraction
- Custom Exceptions & Validations:
  - Inventory constraints (min ≤ inventory ≤ max)
  - Product must have ≥ 1 part
  - Prevent deletion of products with parts
  - Price cannot be less than the sum of associated parts
- Save/Load support via:
  - Java Object Serialization or XML
  - Database operations (JDBC or ORM)

---

## UML-Based Class Design

The application follows a five-class structure as defined in the provided UML diagram:

- `Part` (abstract)
- `InHouse` (extends Part)
- `Outsourced` (extends Part)
- `Product`
- `Inventory` (data manager)

Each class uses getters/setters for encapsulated fields.

---

## Additional Workshop Upgrade (Persistence)

### File I/O:
- **Save to File:** Serialize or export inventory as XML
- **Load from File:** Deserialize or import inventory from XML

### Database I/O:
- **Save to DB:** Store parts/products to relational database
- **Load from DB:** Load all existing parts/products into the application

---


