# DentiSync Application

## Introduction

DentiSync is a advanced dental clinic management application built with a Vue.js frontend and a Spring Boot backend. This guide will walk you through the steps to set up and run the application locally.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- [Node.js](https://nodejs.org/) (v14 or later)
- [npm](https://www.npmjs.com/) (v6 or later)
- [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (v11 or later)
- [Maven](https://maven.apache.org/install.html) (v3.6.3 or later)
- [MySQL](https://www.mysql.com/downloads/) (for database setup)

## Cloning the Repository

# Clone the repository to your local machine using the following command:

```sh
git clone https://github.com/wolflorena/Cabi2023.git
cd Cabi2023
```

## Backend Setup

```sh
cd server
```

# Build and run the backend:

```sh
mvn clean install
mvn spring-boot:run
```

# The backend server should now be running on http://localhost:9090.

## Frontend Setup

# Navigate to the frontend directory:

```sh
cd ../client
```

# Install dependencies:

```sh
npm install
```

# Run the frontend development server:

```sh
npm run serve
```

# The frontend application should now be running on http://localhost:8080.
