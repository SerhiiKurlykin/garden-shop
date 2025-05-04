Project :
1) Jira ()
2) Git
3) TG (pin jira, pin git)
4) Team lead

Sprint 1, 2, 3, 4, 5
Sprint 1( 5 task -> jira 5 taks -> 4 person)

Sprint 1 :
Template - Spring boot project (WEB, JPA, Junit, lombok, liquibase, PostgreSQL)
(login , password, DBname - одинаковые у всех участников проекта)

Entities :
//Поля взять из таблицы
1) User (Repository, Service + Impl) - getAll,create,getById,delete
2) Category (Repository, Service + Impl) -getAll,create,getById,delete
3) Product (Repository, Service + Impl) -getAll,create,getById,delete
4) Favorite (Repository, Service + Impl) -getAll,create,getById,delete

5) На каждый метод сервиса - написать тесты - Junit
6) Exception, handler


Part 2 :
1) Controllers (use REST API document)
2) Order , Cart, CartItem, OrderItems
5) DTO
3) Filter products
4) Relationship
6) MapStruct(lombok, mapstruct, lombok mapstruct binding, plugin - mapstruct)

User -> fav1 (id, user_id, product_id)
-> fav2 (id, user_id, product_id)
-> fav3 (id, user_id, product_id)




//Order
CREATED, Payment controller -> PAYED -> (5 min) -> COMPLETED
CREATED -> (10 min) -> CANCELLED


