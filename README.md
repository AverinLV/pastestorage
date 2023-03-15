![](https://github.com/AverinLV/pastestorage/actions/workflows/github-ci.yml/badge.svg)
[![LinkedIn][linkedin-shield]][linkedin-url]
# Pastestorage API
This project is a sample REST API backend for a pastebin-like service. It was developed only for self-educational reasons. With this service, you can upload chunks of text and get a short link to share with others.

When uploading, you can set a time limit for when the paste will be available from the link. You can also choose the access limitation as public or unlisted.

The short link you receive for the paste download will be similar to this: http://service.com/{random-hash}, e.g. http://service.com/ab12cd34.

You can share the link with anyone you like, and they can access the paste as long as it's available and not unlisted. Users can also view public pastes that have been uploaded.

# Project stack

This service developed using following stack:

* Java 11

* Spring: Boot, MVC (REST), Data, Security, AOP

* PostgreSQL

* JUnit5, Mockito 

* Lombok, Mapstruct, Swagger UI

* Docker

* Google Cloud Platform

# Build and deploy pipeline

Project repository contains GitHub Actions workflow which does following:

* Build .jar
* Build Docker image and push it to Docker Hub
* Deploy on Google Cloud Platform

# Swagger UI

You can access Swagger docs [here](https://averinlv.github.io/pastestorage/)

# Authentication 

Service uses JWT (JSON Web Token) for authentication and authorization. When a user logs in, the server generates a JWT token and sends it to the client. The client then includes this token in the Authorization header of every subsequent request to the server.

The server verifies the token's signature to ensure its authenticity and extracts the user's claims from the token. These claims are used to identify the user and provide authorization. For example, the claims may include the user's roles, permissions, and other attributes that are used to make authorization decisions.

With JWT authentication and authorization, service provides a secure and scalable way to authenticate and authorize users. It reduces server-side storage requirements, improves performance, and simplifies user management. Additionally, the use of JWT allows for easy integration with third-party services and mobile applications.

# Google Cloud Platform

Application and its database are both deployed on GCP. 

Service itself deployed on Cloud Run which is fully managed serverless platform that allows users to run stateless containers in a highly scalable and efficient manner. Cloud Run can handle the scaling, load balancing, and server management automatically

And database is hosted on Compute Engine service which is infrastructure-as-a-service (IaaS) offering that provides users with virtual machines (VMs) that can be customized and managed directly

# Getting started

Service running on cloud, but if you want to deploy it locally on your PC you can simply do it by performing following steps:

## Set up Postgres Docker
Build docker image from Dockerfile
```
docker build -t postgres_docker local_db/.
```
Run it
```
docker run --name postgres_db -dp 5432:5432 postgres_docker
```
## Run service Docker
Pull image from Docker Hub
```
docker pull averinlv/pastestorage:main
```
Run it. You can specify any parameters you'd like 
```
docker run --name pastestorage_docker -p 8080:8080 -d -e DB_API=localhost -e JWT_SECRET=secret -e DB_USER=myuser -e DB_PASSWORD=postgres averinlv/pastestorage:main
```
<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/lev-averin/
