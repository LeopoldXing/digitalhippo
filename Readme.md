# DigitalHippo



The frontend of this project was made based on the “Josh tried coding” tutorial, with a self-designed, self-coded backend application.



Welcome to the DigitalHippo project! It aims to provide a modern online marketplace with extensive functionalities, including sign-in/sign-up, searching, puchasing and product management.



This is the repository for DigitalHippo backend application, the frontend repo is available [here](https://github.com/LeopoldXing/digitalhippo). This README will guide you through the key features, technologies used. Check [here](https://blogs.leopoldhsing.com/digitalhippo/) for more information.



## Key Features

- 🛠️ Complete marketplace built with Next.js 14 / SpringBoot / SpringCloud
- 💾 Microservice architecture for the backend
- 🚢 Kubernetes or Docker compose deployment options available
- 📊 System Monitoring and Observation using Kibana and Grafana
- 🔍 Fast searching experience powred by Elasticsearch
- 📤 Cached by Redis, customer have fast access to all product pages
- 📪 Decoupled by AWS SNS/SQS
- 💻 Beautiful landing page & product pages included
- 🎨 Custom artwork included
- 💳 Full admin dashboard
- 🛍️ Users can purchase and sell their own products
- 🛒 Locally and Server-side persisted shopping cart
- 🔑 Session based token for authentication
- 🖥️ Deep integration with AWS Cloud
- ✉️ Beautiful emails for signing up and after purchase
- ✅ Admins can verify products to ensure high quality
- 🌟 Clean, modern UI using shadcn-ui
- ⌨️ Written in Java and Kotlin
- 🎁 ...much more



## Technologies Used

- **[SpringBoot](https://spring.io/projects/spring-boot)**: Framework used within the single microservice.
- **[SpringCloud (Netflix & AWS)](https://spring.io/projects/spring-cloud)**: Framework used for communication and management of all microservices.
- **[Maven](https://maven.apache.org/)**: Package management tool.
- **[PostgreSQL](https://www.postgresql.org/)**: High-performance relational database.
- **[Redis](https://redis.io/)**: High-performance No-SQL database for session storage and caching.
- **[Redisson](https://redisson.org/)**: Redis-based Java client; this project uses its distributed lock feature.
- **[Bitmap]()**: An overall representation of each product’s existence, this can prevent database from compromising by intercepting malicious requests.
- **[AWS SNS/SQS](https://aws.amazon.com/sns/)**: Topic pub/sub and messaging between microservices, decoupling the system.
- **[AWS S3](https://aws.amazon.com/s3/)**: Used for product file and image storage.
- **[Terraform](https://www.terraform.io/)**: Definition of Cloud resources, Infrastructure as code.
- **[Kubernetes / Docker](https://kubernetes.io/)**: Deployment, container ochestraction.
- **[ELK (Elasticsearch, Logstash, Kibana)](https://www.elastic.co/elastic-stack)**: Powering the product search feature and central logging management.
- **[Prometheus](https://prometheus.io/)**: Collecting system metrics.
- **[Grafana](https://grafana.com/)**: Displaying system status in various dashboards based on the data of Prometheus.
- **[Springdoc OpenAPI](https://springdoc.org/)**: Restful API Documentation.
- **[Stripe](https://stripe.com/en-ca)**: Handles the payment.