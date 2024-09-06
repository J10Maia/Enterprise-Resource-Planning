# 📊 Enterprise Resource Planning (ERP) System

![ERP Overview](https://media.giphy.com/media/fAnzw6YK33jMwzp5wp/giphy.gif)

## Overview

The Enterprise Resource Planning (ERP) System is a comprehensive solution designed to streamline the management and execution of client orders, production schedules, and resource allocation in a flexible production line environment. This ERP module is a crucial component in a larger industrial automation project, providing essential functions like client order management, production scheduling, and cost calculation to ensure efficient and profitable operations.

## 🎯 Key Features

- **Client Order Management:** Accepts and processes client orders encoded in XML, with automatic scheduling and monitoring.
- **Production Scheduling:** Generates a Master Production Schedule (MPS) to optimize the production line's output and resource utilization.
- **Cost Calculation:** Calculates unit costs for each produced item, considering raw material costs, production costs, and depreciation.
- **Integration with MES:** Seamlessly interacts with the Manufacturing Execution System (MES) to manage production orders and monitor execution.

## 🛠️ Technologies Used

- **Backend:** Java
- **Frontend:** JavaScript
- **Version Control:** Github

## 📋 Functional Overview

### 1. Client Orders
The ERP system accepts client orders via XML files sent over UDP/IP to port 24680. Each order includes details like the client name, order number, work-piece type, quantity, due date, and penalties for early or late delivery.

### 2. Production Planning
The ERP generates a Master Production Schedule (MPS) that forecasts production needs, schedules production orders, and plans raw material acquisitions from suppliers. The goal is to meet client deadlines while minimizing costs.

### 3. Cost Calculation
The system calculates the total cost (Tc) for each work-piece using the formula:

\[ Tc = Rc + Pc + Dc \]

Where:
- **Rc** is the raw material cost.
- **Pc** is the production cost (based on production time).
- **Dc** is the depreciation cost (based on the time the material stays in the production line).


## 📫 Contact

If you have any questions, suggestions, or feedback, feel free to reach out to me:

- **LinkedIn:** [João Maia](https://www.linkedin.com/in/your-linkedin-profile/)
- **Email:** [your.email@domain.com](mailto:your.email@domain.com)

---
