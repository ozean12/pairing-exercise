Pair Programming Exercise for Billie
=============
### The Requirements

The way a business like Billie works is this:

```
A business buyer goes to a merchant's ecommerce platform catering for B2B and buys goods. 
At the checkout, the buyer chooses Billie as a payment method, and checks out via our 
widget on the merchants site.  Upon shipment, the merchant tells us this, and is paid
immediately. Billie also issues an invoice to the buyer.  The buyer is then invoiced 
from Billie and they pay the invoice
```

At this point, we have built an API to map simple organisations, but not much else.  
There are a lot of features still to build!!!

### The Exercise

The task is to implement one of the most important business requirements:

> The ability for the merchant to notify Billie of shipment of an order, so they can get paid.

In order to implement this, please make a fork of this repo and when done with the implementation, send us a link to
your repo.

Strong hint: we are fans of TDD, DDD and clean code.

### The Tech Stuff
#### Prerequisites
We assume that you have docker, docker compose, and Java 15 installed, and can run gradle

Running the tests:
```shell
cd <project_root>
docker compose up database -d
gradle flywayMigrate
gradle clean build
docs at -> http://localhost:8080/swagger-ui/index.html
```
Work has been started but not done yet to containerise the kotlin service.

The service runs in the host right now.  Feel free to fix that if it makes your life easier
