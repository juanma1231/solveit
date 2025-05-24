# SolveIt

![Build Status](https://github.com/username/solveit/actions/workflows/ci.yml/badge.svg)

## Overview
SolveIt is a platform that allows users to publish and find services, chat with other users, and manage publications.

## Features
- User authentication and management
- Publication creation and management
- Real-time chat between users
- Report system for inappropriate content

## Development
This project uses:
- Java 21
- Spring Boot 3.4.5
- Gradle for building
- MySQL for database

### Building the Project
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

## Continuous Integration
This project uses GitHub Actions for continuous integration. The CI pipeline runs on every push to the master branch and pull requests targeting the master branch. It:
- Builds the project
- Runs tests
- Performs code quality checks
- Publishes test reports
- Uploads build artifacts

## Releases
Releases are created automatically when a tag starting with 'v' is pushed to the repository. The release workflow:
- Builds the project
- Creates a GitHub release with the built artifacts

## Security
This project uses OWASP Dependency Check to scan for vulnerabilities in dependencies. The scan runs:
- Weekly on Sunday at midnight
- Manually when triggered

The results are uploaded to GitHub's security dashboard and as artifacts in the workflow run.
