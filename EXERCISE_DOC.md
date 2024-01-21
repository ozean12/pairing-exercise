Thought Process on Programming Exercise
=============
### Assumptions

I understood how Billie deals with e-commerce business and high level requirements from [here](README.md). However, I made few assumptions 
while implementing the exercise instead of waiting for answers raised on weekend.
1. Requirement talks about _Merchant_ but the given code has _Organisation_ which somewhat similar but not completely same. **I assumed _Organisation_ as _Merchant_ domain for this exercise.**
2. _Order_ doesn't exist without _Organisation_ and _Shipment_ doesn't exist without _Order_.
3. The API requests are already validated from security point of view and authentication is implemented.
4. Designed data model is very basic and kept limited to the demo. No complex modeling, validation and business cases such as currency exchange etc. considered.

### What is done
1. With availability of very basic domain information, I created two domains _Order_ and _Shipment_ including respective DB tables.
2. Domains perform basic validations such as shipment notification validates existence of order and total amount within range of order amount.
3. Added integration tests which tests possible use-cases including request validations.
4. Separate domain and view models(Dto).

### Exercise vs Production scope
From resilience, scalability and performance point of view, I think the design has below basic amendments meet production level.
1. Multiple domains are deployed in single service, dividing them in separate microservices would be useful for scalability purpose.
2. Domains would have their own database which helps maintainability and limiting the scope to domain requirements.
4. When services communicate via REST end-points, it is possible to use contract testing to ensure neither server or client breaks the contact before moving to production.
5. Post end-points to be Idempotent to avoid multiple try from client doesn't result in new resource creation.
6. Optimize API timeouts, required retry strategy and circuit breaker to make services resilient.
