# 📊 Enterprise Resource Planning (ERP) System

![ERP GIF](https://media.giphy.com/media/26BRuo6sLetdllPAQ/giphy.gif)

## Overview

The Enterprise Resource Planning (ERP) System is a comprehensive solution designed to streamline the management and execution of client orders, production schedules, and resource allocation in a flexible production line environment. This ERP module is a crucial component in a larger industrial automation project, providing essential functions like client order management, production scheduling, and cost calculation to ensure efficient and profitable operations.

![ERP System Overview](https://media.giphy.com/media/3oEjHZh9W8vML2lA2I/giphy.gif)

## 🎯 Key Features

- **Client Order Management:** Accepts and processes client orders encoded in XML, with automatic scheduling and monitoring.
- **Production Scheduling:** Generates a Master Production Schedule (MPS) to optimize the production line's output and resource utilization.
- **Cost Calculation:** Calculates unit costs for each produced item, considering raw material costs, production costs, and depreciation.
- **Integration with MES:** Seamlessly interacts with the Manufacturing Execution System (MES) to manage production orders and monitor execution.

![Scheduling](https://media.giphy.com/media/l0HlNQ03J5JxX6lva/giphy.gif)

## 🛠️ Technologies Used

- **Backend:** Python (Django)
- **Database:** PostgreSQL
- **APIs:** RESTful APIs for communication with MES and other components
- **Deployment:** Docker for containerized deployment
- **Version Control:** Git

## 📋 Functional Overview

### 1. Client Orders
The ERP system accepts client orders via XML files sent over UDP/IP to port 24680. Each order includes details like the client name, order number, work-piece type, quantity, due date, and penalties for early or late delivery.

![Order Management](https://media.giphy.com/media/xT9IgzoKnwFNmISR8I/giphy.gif)

### 2. Production Planning
The ERP generates a Master Production Schedule (MPS) that forecasts production needs, schedules production orders, and plans raw material acquisitions from suppliers. The goal is to meet client deadlines while minimizing costs.

### 3. Cost Calculation
The system calculates the total cost (Tc) for each work-piece using the formula:

\[ Tc = Rc + Pc + Dc \]

Where:
- **Rc** is the raw material cost.
- **Pc** is the production cost (based on production time).
- **Dc** is the depreciation cost (based on the time the material stays in the production line).

![Cost Calculation](https://media.giphy.com/media/3oKIPvvjzj6a84EjXi/giphy.gif)

### 4. Integration with MES
The ERP interacts with the Manufacturing Execution System (MES) to issue production orders, monitor their progress, and receive updates. This ensures that the production process aligns with the schedule and client requirements.

![MES Integration](https://media.giphy.com/media/l0HlVz5Z1lRk5QNjO/giphy.gif)

## 🌟 Future Enhancements

- **Multi-language Support:** Add support for multiple languages to cater to a global audience.
- **Advanced Reporting:** Implement advanced analytics and custom report generation.
- **Mobile App Integration:** Develop a mobile app version of the ERP system.
- **Cloud Deployment:** Enhance cloud deployment options for scalability and performance.

## 🧪 Testing

To run the unit tests and ensure everything is working correctly:

```bash
cd src/backend
python manage.py test
