﻿# HotelRatingMicroservceApp


# 🏨 Hotel Rating Microservice System

This project is a **Spring Boot-based Microservice Application** that manages hotels, users, and ratings. It demonstrates a clean microservices architecture with secure communication, centralized configuration, and service discovery.

---

## 📌 Features

- ✅ **Microservices Architecture** using Spring Boot
- 🔐 **API Security** via **Okta OAuth 2.0**
- 🧭 **Service Discovery** with **Eureka Server**
- 🛡️ **API Gateway** for routing and centralized access
- ⚙️ **Centralized Configuration** using Spring Cloud Config Server
- 🧪 JUnit-based testing
- 📡 RESTful APIs for communication
- 📁 Organized folder structure with clear domain separation

---

## 🏗️ Project Structure

```bash
HotelRatingMicroservceApp/
│
├── ApiGateway/                  # API Gateway using Spring Cloud Gateway
├── ServiceRegistry/            # Eureka Server for service discovery
├── MicroserviceConfigServer/   # Spring Cloud Config Server
├── HotelService/               # Hotel Microservice
├── RatingService/              # Rating Microservice
├── UserService/                # User Microservice
└── README.md


🔐 Authentication and Security
Okta OAuth 2.0 is used for securing all APIs.

Each service is authenticated before processing any request.

Access tokens must be passed with each request to protected endpoints.




🔄 Service Discovery
The ServiceRegistry module implements a Eureka Server.

All services register themselves and use logical names for inter-service communication.

🌐 API Gateway
Routes and filters requests to respective microservices.

Acts as a single entry point for the entire system.

⚙️ Configuration Server
The MicroserviceConfigServer provides centralized configurations.

All application configurations are stored in a shared Git repository or in-memory for local development.

🧪 Testing
Unit and integration tests implemented using JUnit and Mockito.

Test coverage includes service logic, controller layers, and integration points.

🧰 Tech Stack
Tool/Framework	Description
Spring Boot	Core framework for building services
Spring Cloud	For API Gateway, Config Server, Eureka
Okta	OAuth 2.0 based Authentication
Eureka Server	Service Registry & Discovery
Spring Gateway	API Gateway
JUnit/Mockito	Testing Frameworks
Maven	Dependency Management
GitHub	Version Control

🚀 Getting Started
1. Clone the repo
bash
Copy
Edit
git clone https://github.com/Achamanp/HotelRatingMicroservceApp.git
cd HotelRatingMicroservceApp
2. Configure Okta
Create a developer account on https://developer.okta.com

Create an application and configure redirect URIs.

Add client ID and issuer in your application.yml:

yaml
Copy
Edit
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://{yourOktaDomain}/oauth2/default
3. Start Services (order matters)
bash
Copy
Edit
1. Start ServiceRegistry
2. Start MicroserviceConfigServer
3. Start HotelService, UserService, RatingService
4. Start ApiGateway
📬 API Endpoints (Examples)
Service	Endpoint	Method	Description
HotelService	/hotels	GET	Get all hotels
UserService	/users	POST	Create a new user
RatingService	/ratings/user/{userId}	GET	Get ratings by a user
API Gateway	/hotel-service/hotels	GET	Routed through API Gateway

📷 Screenshots
Coming soon...

📚 Future Enhancements
✅ Add database integration (MySQL/PostgreSQL)

✅ Implement service resilience with Circuit Breaker (Resilience4j)

⏳ Add Swagger/OpenAPI documentation

⏳ Integrate distributed tracing (Zipkin/Sleuth)

🤝 Contribution
Contributions are welcome! Feel free to fork the repository and raise a pull request.

📄 License
This project is open-source and free to use under the MIT License.

🙋‍♂️ Author
Achaman Pathak
https://github.com/Achamanp/ | https://www.linkedin.com/in/achaman-pathak-912561251/

