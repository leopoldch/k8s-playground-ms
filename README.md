# Microservices Architecture Project

This project is a multi-service application designed to demonstrate the implementation, deployment, and operation of a microservices architecture, primarily focusing on container orchestration with Kubernetes.

The application's core functionality is a simple **Text Polarity Analysis** tool that allows users to submit a sentence and receive a prediction of its sentiment (e.g., positive, negative, or neutral). It also includes a **Feedback** mechanism for users to submit data for potential model improvement.

As this project was completed for an academic assignment, some setup elements—specifically those related to prerequisites like certificates and necessary permissions—have been configured for maximum portability and ease of execution across diverse machines and environments.

## Architecture

The system is composed of four main microservices, interconnected via an API Gateway:

| Service | Technology | Role |
| :--- | :--- | :--- |
| **Frontend** | React, JavaScript | User interface for submitting text for analysis and viewing results. |
| **API Gateway** | Spring Boot, Java | Central entry point, routes external requests to the correct backend services (Polarity and Feedback). |
| **Logic API** | Python, Flask | Implements the core business logic: performs the sentiment/polarity analysis on text input. |
| **Feedback API** | .NET Core, C# | Manages user feedback, storing data in an SQLite database. |

A diagram illustrating the feature flow and service interactions can be found in the `doc/images/feature_diagram.png` file.

## Technology Stack

The project utilizes a diverse set of modern technologies:

* **Frontend:** React, Node.js
* **Backend Services:** Spring Boot (Java), Python/Flask, .NET Core (C#)
* **Containerization:** Docker
* **Orchestration:** Kubernetes
* **Database:** SQLite (for Feedback API)
* **Monitoring & Logging:** Prometheus (Metrics), Grafana (Visualization), Loki/Logging Stack (Centralized Logs)

## Deployment

This repository includes a comprehensive set of Kubernetes manifest files to deploy the entire application and its infrastructure components.

### Prerequisites

* A running Kubernetes cluster (e.g., Minikube, K3s, kind).
* `kubectl` configured to communicate with the cluster.
* The necessary images for each microservice must be built and accessible to the cluster (e.g., pushed to a registry or built locally if using a single-node cluster like Minikube).

### Deployment Steps

The Kubernetes resource files are organized under the `submission/` directory and should be applied in a logical order to ensure dependencies are met.

To execute all in once: 
    ```bash
    kubectl apply -R -f submission/
    ```

1.  **Infrastructure Setup (Monitoring, Logging, CRDs):**
    ```bash
    kubectl apply -f submission/01-monitoring/
    kubectl apply -f submission/00-crd/
    ```

2.  **Secret and Volume Setup:**
    ```bash
    kubectl apply -f submission/07-encryption/00-tls-secret.yaml
    kubectl apply -f submission/03-feedback-api/feedback-db-pv.yaml
    kubectl apply -f submission/03-feedback-api/feedback-db-pvc.yaml
    ```

3.  **Service Deployments (Feedback, Logic, API Gateway, Frontend):**
    ```bash
    kubectl apply -f submission/03-feedback-api/
    kubectl apply -f submission/05-logic-api/
    kubectl apply -f submission/02-api-gateway/
    kubectl apply -f submission/04-frontend/
    ```

4.  **Ingress Configuration:**
    Ensure an Ingress Controller (like NGINX) is running in your cluster.
    ```bash
    kubectl apply -f submission/06-ingress/
    ```

### Accessing the Application

Once deployed, you can access the various services using the configured Ingress hosts (check your Ingress controller's IP address):

* **Frontend Application:** `https://<your-frontend-host>`
* **API Gateway:** `https://<your-api-host>/api/...`
* **Grafana Dashboard:** `https://<your-api-host>/admin/grafana` (for metrics and logs visualization)
