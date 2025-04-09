# 📢 Event-Driven S3 Notifier 🚀

Welcome to the Event-Driven S3 Notifier project! This is a serverless application built with AWS Serverless Application Model (SAM) and Java 17, designed to send email notifications whenever a file is uploaded to an Amazon S3 bucket. Leveraging an event-driven architecture (EDA), this project showcases the power of AWS services like S3, Lambda, and SNS, orchestrated through a robust CI/CD pipeline with GitHub Actions. 🌟

## 🎯 Project Overview

This application demonstrates a simple yet powerful event-driven workflow:

1. 📦 An object is uploaded to an S3 bucket.
2. ⚡ The upload triggers a Lambda function.
3. 📧 The Lambda function sends an email notification via SNS to subscribed users.

## ✨ Features

- **Serverless Architecture**: Built with AWS SAM for easy infrastructure-as-code (IaC) management.
- **Event-Driven**: Reacts to S3 ObjectCreated events in real-time.
- **CI/CD Pipeline**: Automated deployment with GitHub Actions for dev and prod environments.
- **Java 17**: Modern Java runtime for the Lambda function.
- **Scalable**: Handles multiple subscribers via SNS topics.

## 🏗️ Architecture

Here's how it all fits together:

```
[S3 Bucket] 🖼️ --> (ObjectCreated Event) ⚡ --> [Lambda Function] 🛠️ --> [SNS Topic] 📬 --> [Email Subscribers] ✉️
```

- **S3 Bucket**: Stores uploaded files and triggers events.
- **Lambda Function**: Processes the S3 event and publishes a message to SNS.
- **SNS Topic**: Manages email subscriptions and sends notifications.

For a visual representation, check out the (to be added separately).

## 🛠️ Prerequisites

To get started, ensure you have the following:

- ☕ **Java 17**: Installed for local development (e.g., Temurin JDK).
- 🛠️ **Maven**: For building the Java project.
- ☁️ **AWS CLI**: Configured with credentials (`aws configure`).
- 🚀 **AWS SAM CLI**: For building and deploying the SAM application.
- 🐙 **GitHub Account**: For CI/CD integration.
- 📧 **Email Address**: For SNS subscription (must be confirmed).

## 📂 Project Structure

Here's what you'll find in the `eda-lambda/` directory:

```
eda-lambda/
├── .github/              # GitHub Actions workflows
│   ├── workflows/
│   │   ├── deploy-dev.yml   # CI/CD for development
│   │   └── deploy-prod.yml  # CI/CD for production
├── src/                  # Java source code
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── S3EventHandler.java  # Lambda handler
├── target/               # Maven build output (e.g., JAR file)
├── pom.xml               # Maven configuration
├── template.yaml         # AWS SAM template
└── README.md             # You're reading it! 🎉
```

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/eda-lambda.git
cd eda-lambda
```

### 2. Configure AWS Credentials

Set up your AWS CLI with an IAM user that has permissions for S3, Lambda, SNS, IAM, and CloudFormation.

```bash
aws configure
```

Store credentials in GitHub Secrets for CI/CD:
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

### 3. Set Email Subscriber

Update `template.yaml` with your email:

```yaml
Parameters:
  EmailSubscribers:
    Type: String
    Default: "your-email@example.com"
```

Alternatively, set `DEV_EMAIL_SUBSCRIBERS` and `PROD_EMAIL_SUBSCRIBERS` in GitHub Secrets.

### 4. Build the Project Locally

```bash
mvn clean package
```

This generates the JAR in `target/eda-lambda-1.0-SNAPSHOT.jar`.

### 5. Deploy Locally (Optional)

```bash
sam build --use-container
sam deploy --stack-name s3-notifier-dev --parameter-overrides EnvironmentName=dev EmailSubscribers=your-email@example.com --no-fail-on-empty-changeset --capabilities CAPABILITY_IAM --resolve-s3
```

## 🌐 CI/CD Pipeline

The project uses GitHub Actions for automated deployment:

- **Dev Environment**: Triggered on push to `main` branch.
- **Prod Environment**: Triggered on push to `production` branch.

### Workflow Steps
1. Checkout Code 📥
2. Setup Java 17 ☕
3. Configure AWS Credentials 🔑
4. Install SAM CLI 🛠️
5. Build (`mvn clean package` + `sam build`) ⚙️
6. Deploy (`sam deploy`) 🚀

### Secrets Needed
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `DEV_EMAIL_SUBSCRIBERS`
- `PROD_EMAIL_SUBSCRIBERS`

## 📧 How It Works

1. **Upload a File**:
    - Use the AWS S3 Console to upload a file to `s3-upload-notifier-dev-<account-id>`.

2. **Event Trigger**:
    - S3 detects the `s3:ObjectCreated:*` event and invokes the Lambda function.

3. **Notification**:
    - The Lambda function (`S3EventHandler`) publishes a message to the SNS topic.
    - SNS sends an email to all confirmed subscribers.

4. **Check Your Inbox**:
    - Look for an email with a subject like "New File Uploaded to s3-upload-notifier-dev-<account-id>".

## 🧪 Testing

### Local Testing

Build and simulate an S3 event:

```bash
sam build --use-container
sam local invoke NotificationLambda -e event.json
```

Sample `event.json`:

```json
{
  "Records": [
    {
      "s3": {
        "bucket": { "name": "s3-upload-notifier-dev-<account-id>" },
        "object": { "key": "test.txt" }
      }
    }
  ]
}
```

Check CloudWatch logs locally or in AWS.

### AWS Console Testing

1. Upload a file to the S3 bucket.
2. Check CloudWatch Logs (`/aws/lambda/s3-upload-notifier-lambda-dev`) for execution details.
3. Verify email receipt.

## ⚠️ Troubleshooting

### No Email Received? 📭
- Confirm SNS subscription in AWS Console (SNS > Topics > Subscriptions).
- Check CloudWatch logs for Lambda errors.
- Ensure email isn't in spam/junk.

### Lambda Not Triggered? 🚫
- Verify S3 event configuration in the bucket properties.
- Check `LambdaInvokePermission` in `template.yaml`.

### Deployment Fails? 🛑
- Review GitHub Actions logs or run `sam deploy --debug` locally.

## 🌟 Extra Features

- **Architecture Diagram**: Coming soon! Add a link to a visual diagram for bonus points. 🎨
- **Environment Separation**: Dev and prod stacks are isolated with `EnvironmentName` parameter.
- **Scalability**: Add more email subscribers by updating `EmailSubscribers` and redeploying.

## 📜 License

This project is licensed under the MIT License. Feel free to use, modify, and share! 🌍

## 🤝 Contributing

Contributions are welcome! Fork the repo, make your changes, and submit a pull request. Let's make this project even better together! 💪

## 📬 Contact

Have questions? Reach out via GitHub Issues or star the repo to show your support! ⭐

Happy coding, and enjoy your event-driven notifications! 🎉