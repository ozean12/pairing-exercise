Managers' Case Study / Exercise for Billie
=============
### Case Study / Exercise
We have a strongly held belief in Billie that any engineering management should remain current, and have their coding and design 
skills at a sufficient level to be able to run a high performing team.  
The way our compentencies are defined for management allows for managers to remain more technical or less technical based on their personal strengths they 
bring to the role.  
However, there is a baseline of understanding of coding, design and architecture we expect from any management position.


Details of the Exercise
====
``` 
There are 2 main steps on how we assess technical competencies for managers. Firstly, 
we will ask for the candidate to complete one of the very simple outstanding tasks
below†, at home.  Time guidance is please don't spend more than an hour on it. 

The second part is an interview with some folks in Billie's remote (or in the office if possible)

This part we will discuss how you would take the code and architecture further from
the example, into production, and what type architecture of principles you would apply
to the platform.

† Strong hint: we are fans of TDD, pairing and continuous deployment.
```




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
There are a lot of things still to build
* Adding an address to an organisation
* Issuing an invoice for the buyer once the merchant notifies us the goods have been shipped
* Once the buyer has paid the invoice, there needs to be the ability to mark the invoice as paid 
* The ability for the merchant to notify us of shipment of an order, so they can get paid

We will tackle _some_ of these cases as part of the pairing exercise



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
