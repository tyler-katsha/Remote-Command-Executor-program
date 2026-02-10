# Remote-Command-Executor-program
RemoteCommandExecutor is a multithreaded Java TCP/IP server built with Maven that lets clients send commands and receive real-time responses. Current features include uptime, OS info, date/time, and client tracking, while geolocation support using Google Maps API is planned for later versions..

#Overview 

RemoteCommandExecutor is a multithreaded Java TCP/IP server application that allows multiple clients to connect and execute predefined commands in real time. The project is built using Maven and demonstrates practical networking concepts such as sockets, concurrency, command parsing, and API integration.

This project focuses on clean architecture, scalability, and real-world backend development practices suitable for learning Java networking and server-side programming.

#Current Features

- Multithreaded TCP server using Java sockets

- Multiple client support with connection tracking

- Command-based server interaction

- Real-time responses from the server

#Built-in commands:

- time – Displays current server time

- date – Displays current server date

- uptime – Shows how long the server has been running

- os – Displays operating system information

- clients – Shows number of connected users

- ping – Returns a pong response

- whoami – Displays client IP address

- help – Lists all commands

- exit / leave – Disconnect from server

#Upcoming Features (Planned Versions)

- Geolocation support using Google Maps API

- JSON-based structured responses

- Improved command handling system

- Enhanced error handling and logging

- GUI-based client or admin dashboard

- UDP-based experimental networking modules

- Security improvements and environment-based configuration

- Real-time Console Logging – Allows commands to output messages similar to console.log("Hello World") for debugging and monitoring.

#Technologies Used

- Java (Sockets, Threads, Networking APIs)

- Maven (Dependency Management & Build System)

- Gson (JSON Parsing)

- Java Dotenv (Environment Variables)

#How It Works

1. The server listens for incoming TCP connections.

2. Each client connection is handled by a dedicated thread.

3. Commands sent by clients are processed by a central dispatcher.

4. The server sends formatted responses back to clients.

- This design demonstrates concurrency, socket programming, and scalable backend architecture.

#Educational Purpose

This project was designed to explore:

- TCP/IP networking fundamentals

- Multithreading in Java

- Maven dependency management

- API integration and JSON parsing

- Secure configuration using environment variables

#Future Vision

RemoteCommandExecutor aims to evolve into a modular networking toolkit showcasing advanced backend concepts such as remote administration, monitoring tools, and extensible command systems while maintaining clean, readable Java code.

#License

This project is for educational and demonstration purposes.
