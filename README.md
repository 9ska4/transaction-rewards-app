# transaction-rewards-app

## Based on requirements: 
1. Rewards program to its customers, awarding points based on each recorded purchase.
2. A customer receives 1 points for every dollar spent over $50 in each createTransaction and also plus 1 additional point for every dollar spent over $100 in each createTransaction (e.g. a $105 purchase = 1x$55 + 1x$5 = 60 points).
3. Given a record of every transaction during a three-month period, calculate the reward points earned for each customer per month and total.

___

## Solution:
1. Springboot app to store transactions in h2 in-memory db and


### Required:
- Java17+
- Maven3

### Run by:
`mvn spring-boot:run`

### Documentation:
- Swagger documentation: http://localhost:8080/swagger-ui/index.html
- Open api v3: http://localhost:8080/v3/api-docs
