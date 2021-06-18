# AWS-ImageRecognitionAsAService

## Project 1 - IaaS - Amazon Web Services
The aim of this project was to develop a cloud app which will provide Image Recognition as a Service to users by using the AWS cloud resources to perform deep learning on images provided by the users.
The deep learning model was provided in an AWS image (ami-0ee8cf7b8a34448a6 and use the default location as us-east-1). This application invokes this model to perform image recognition on the received images.
The application will handle multiple requests concurrently. It will automatically scale out when the request demand increases, and automatically scale in when the demand drops.

AWS services used in the project are:  
Elastic Compute Cloud (EC2)   
Simple Queue Service (SQS)  
Simple Storage Service (S3)  

Further details are provided in the report.

## WebTier
This is a RESTful Web Service which accepts requests from the user (Images), stores them into a **S3 input bucket “cc21cprk-input-bucket”** and puts the image name into an **Input Queue “cc21cprk-input-queue”**.
After which, it starts listening to the **Output Queue “cc21cprk-output-queue”** for the response.
This application also has a load balancer service which creates app instances when the request demand increases (Scale out). Autoscaling is the ability of any distributed application to scale-out and scale-in based on the amount of load. In this architecture, the number of worker AppTier EC2 instances are spawned and terminated based on the number of image requests.

## AppTier
This application runs inside the app instances and listens for messages (requests) in the Input Queue “cc21cprk-input-queue”.
When the message arrives, it takes the message, stores a local copy for the image and runs the deep learning model for classification, puts the classification result into a **S3 output bucket “cc21cprk-output-bucket”**. The classification result is also inserted into the Output Queue “cc21cprk-output-queue”.
When there is no message in the Input queue, the application shuts down the instance in which its running, facilitating scale in.

## Instructions:-
Webtier is running in AWS. HTTP Requests should be sent to this link:  
http://ec2-35-170-71-37.compute-1.amazonaws.com:8080/upload 
