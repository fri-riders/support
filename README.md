[![Build Status](https://travis-ci.org/fri-riders/support.svg?branch=master)](https://travis-ci.org/fri-riders/support)
# Support Service

## Run with Docker Compose
1. Build app: `mvn clean package`
1. Run: `docker-compose up --build`

App is accessible on port `8088`

# Registered endpoints
## Support
* `GET: /v1/support` Returns list of support tickets
* `GET: /v1/support/{ticketId}` Returns info about support ticket
* `GET: /v1/support/{userId}/user` Returns list of support tickets for user \***
* `POST: /v1/support/` Create support ticket (params: `userId:string`, `subject:string`, `message:string`) \***
## Config
* `GET: /v1/config` Returns list of config values
* `GET: /v1/config/info` Returns info about project
## Health
* `GET: /health` Returns health status
* `GET: /v1/health-test/instance` Returns info about instance
* `POST: /v1/health-test/update` Update property `healthy` (params: `healthy:boolean`)
* `GET: /v1/health-test/dos/{n}` Execute calculation of n-th Fibonacci number
## Metrics
* `GET: /metrics` Returns metrics

#### Notes
\*** must be authenticated with `authToken` header present and supplied with valid JWT from login