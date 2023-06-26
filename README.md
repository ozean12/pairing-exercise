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
* Adding an address to an organisation
* Issuing an invoice for the buyer once the merchant notifies us the goods have been shipped
* Once the buyer has paid the invoice, there needs to be the ability to mark the invoice as paid 
* The ability for the merchant to notify us of shipment of an order, so they can get paid

We will tackle _some_ of these cases as part of the pairing exercise


The Exercise
====
```
In order for you to prepare for the pair programming we need you to be familiar 
with the code we've already got in this repository.
Please make a fork of this repo and add *one* feature from the list above and 
send us a link to your repo.  This tells us you've worked in the code and are 
familiar with it. We are excited to see your solution that introduces you 
as a smart and skilled engineer.  We like high success rates for our pairing 
sessions and history tells us that is much more likely if a candidate has 
familiarity with the code already and tried to solve a problem which might be
met in FinTech.

Strong hint: we are fans of TDD, pairing and continuous deployment.
```


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


### From candidate:

Implemented features:
1. Add an address to an organisation

Additional improvements:
1. Integration tests now run on random ports
2. Flyway migration runs on application start
3. Now, it is possible to run service inside docker on local
