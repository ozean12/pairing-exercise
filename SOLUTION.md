Solution for Billie Pair Programming Exercise 
=============
### Requirements:

* Issuing an invoice for the buyer once the merchant notifies us the goods have been shipped

### Steps to test
1- open the swagger ui:
http://localhost:8080/swagger-ui/index.html

2- find the list of available merchants or add a new one:

```
[
  {
    "id": "59267c88-a561-4e07-ab5d-75b64bed6a7c",
    "name": "amazon.com"
  },
  {
    "id": "a177028a-fcde-4b56-95aa-08c0fa9fca2e",
    "name": "ebay.com"
  }
]
```

3- find the list of available customers or create a new one:
```
[
  {
    "id": "25fca542-cb2f-4c5d-81e1-cbe2f4a6fd83",
    "name": "John Smith",
    "address_details": "5703 Louetta Rd, Texas"
  },
  {
    "id": "2867689a-f3b1-45ed-a1ad-7adf3b40a0ab",
    "name": "Josh Long",
    "address_details": "9069 Holman Rd NW, Seattle"
  },
  {
    "id": "1325673e-ea2e-4b0b-822c-051204ecc7e8",
    "name": "Esfandiyar Talebi",
    "address_details": "Yavux su, burhabine, Istanbul"
  }
]
```

4- find the list of available Products or add new ones:
```
[
  {
    "id": "126ff1c1-1e28-4355-a5d2-1aab3f3c48b8",
    "name": "iPhone 15 SE",
    "price": 999
  },
  {
    "id": "93e77e2e-30ea-40d2-b505-b46c00f15680",
    "name": "iPhone 15 Max",
    "price": 1299
  }
]
```
5- Create an Order for one of the Customers with available product uid:
```
{
  "amount": 100.00,
  "customerId": "25fca542-cb2f-4c5d-81e1-cbe2f4a6fd83"
}
```
and note down the order uid:
```
{
  "id": "94449231-63de-441a-bb9b-d70e802ab957"
}
```
6- Notify the shipment of the order by using merchant resource

```
{
 "shipmentUId": "94449231-63de-441a-bb9b-d70e802ab955",
  "orderUId": "94449231-63de-441a-bb9b-d70e802ab957",
  "customerUId": "25fca542-cb2f-4c5d-81e1-cbe2f4a6fd83"
}
```
as a result, invoice will be generated for the customer.

7- find the newly created Invoice within the list of invoices

### work in progress
1- Handling the deduplication of shipment event
2- Adding more integration tests for DAO classes


#### Prerequisites

Running the tests:
```shell
cd <project_root>
docker compose up database -d
gradle flywayMigrate
gradle clean build
docs at -> http://localhost:8080/swagger-ui/index.html
```
