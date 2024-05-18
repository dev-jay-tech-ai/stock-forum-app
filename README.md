
Developing a bulletin board service with stock discussion rooms for each financial sector, operating on a social media platform.    
The project involves transitioning from a monolithic architecture to a microservices approach.

<br>

## Introduce.

The stock forum app serves as a platform where users can share stock information, exchange feedback, and browse current stock situations. Through real-time updates and reviews presented in a social media format, users can make well-informed purchasing decisions.

<br>

## Docker-Compose usage.
```
 docker-compose -f docker-compose.yml up    
```
<br>

## Process.

https://trello.com/invite/b/yhpSXVVD/ATTI8c6ff83e1049d11d22e29c533c4a413aF23844DF/⚓️-project

<br>

## ERD.

<br>

## API document.

https://documenter.getpostman.com/view/12708271/2sA3JRYywd

<br>

## Main features.

- User Management: Users can enhance their communication network through profile management and follow functions.
- Share Stock Information and Reviews: Users can post stock information, write comments, and engage with other users' content through likes and shares.
- Real-Time Stock System: The app provides a secure and user-friendly interface for accessing real-time stock lists and charts.

<br>

## Performance Optimisation Updates.

- Implementation of MSA (MicroService Architecture): We've adopted a MicroService Architecture to enhance scalability and maintainability across our services.
- Integration of API Gateway: An API Gateway has been incorporated to bolster system stability and streamline service management.
- Introduction of Real-Time Stock Service: To efficiently manage high volumes of traffic, we've introduced a real-time inventory management service.
<br>

## Trouble shooting.

- Issue: Every time an API request is made, it requires reading user data from the database, further contributing to server load.

- Solution: To resolve this problem, Redis was implemented to cache frequently accessed member information. This reduces the load on the database and speeds up the retrieval process.
By caching user information with Redis, the number of direct database requests is minimized, leading to improved performance and efficiency, especially during API requests.
<br>

## Techology.

>JDK 17    
Spring Boot 2.6.7   
Spring Data JPA   
Gradle   
Lombok   
Github   
Docker   
Redis   
Kafka   
MariaDB    
MongoDB    
RabbitMQ   
Testcontainers   
Microservice Architecture   
Json Web Token   
HTTP Request / Response   
Spring Cloud Netflix Eureka   
Spring Cloud Gateway   
