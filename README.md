Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we asume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and README.md with API documentation and curl commands to get data for voting and vote.

### curl samples (application deployed in application context `graduation`).
> For windows use `Git Bash`

 #### get All Users
 `curl -s http://localhost:8080/graduation/rest/admin/users --user admin@gmail.com:admin`
 
 #### get User by id
 `curl -s http://localhost:8080/graduation/rest/admin/users/100001 --user admin@gmail.com:admin`
 
 #### get User by email
  `curl -s http://localhost:8080/graduation/rest/admin/users/by?email=seconduser@mail.ru --user admin@gmail.com:admin`
 
 #### create User
 `curl -s -X POST -d '{"name":"New","email":"new@gmail.com","password":"newPass","roles":["ROLE_USER","ROLE_ADMIN"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/rest/admin/users --user admin@gmail.com:admin`
 
 #### delete User
 `curl -s -X DELETE http://localhost:8080/graduation/rest/admin/users/100000 --user admin@gmail.com:admin`
 
 #### update User
 `curl -s -X PUT -d '{"name":"Update", "email":"user@mail.ru", "password":"newPass","roles":["ROLE_ADMIN"]}' -H 'Content-Type: application/json' http://localhost:8080/graduation/rest/admin/users/100000 --user admin@gmail.com:admin`

 #### register User
 `curl -s -X POST -d '{"name":"New","email":"new@gmail.com","password":"newPass","roles":["ROLE_USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/rest/profile/register`
 
 #### get Restaurant by id
 `curl -s http://localhost:8080/graduation/rest/profile/restaurants/100003  --user user@mail.ru:password`
 
 #### get Restaurant not find
  `curl -s http://localhost:8080/graduation/rest/profile/restaurants/100012  --user user@mail.ru:password`
 
 #### get All Restaurants
  `curl -s http://localhost:8080/graduation/rest/profile/restaurants --user admin@gmail.com:admin`
  
 #### get Restaurant by name
  `curl -s http://localhost:8080/graduation/rest/profile/restaurants/by?name=Remy+Kitchen+Bakery --user user@mail.ru:password`
  
 #### delete Restaurant
  `curl -s -X DELETE http://localhost:8080/graduation/rest/admin/restaurants/100004 --user admin@gmail.com:admin`
  
 #### update Restaurant
  `curl -s -X PUT -d '{"name":"Update"}' -H 'Content-Type: application/json' http://localhost:8080/graduation/rest/admin/restaurants/100003 --user admin@gmail.com:admin`

 #### create Restaurant
 `curl -s -X POST -d '{"name":"New"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/rest/admin/restaurants --user admin@gmail.com:admin`
 
 #### get last Menu
  `curl -s http://localhost:8080/graduation/rest/profile/menu/last?restaurantId=100003  --user user@mail.ru:password`
  
 #### get Menu by id and restaurant id
  `curl -s http://localhost:8080/graduation/rest/admin/menu/100011?restaurantId=100003  --user admin@gmail.com:admin`
  
 #### get all Menu
  `curl -s http://localhost:8080/graduation/rest/admin/menu?restaurantId=100003 --user admin@gmail.com:admin`
  
 #### get Menu by date
  `curl -s "http://localhost:8080/graduation/rest/admin/menu/by?restaurantId=100003&date=2015-05-30" --user admin@gmail.com:admin`
  
 #### delete Menu
 `curl -s -X DELETE http://localhost:8080/graduation/rest/admin/menu/100011?restaurantId=100003 --user admin@gmail.com:admin`
 
 #### update Menu
 `curl -s -X PUT -d '{"date":"2015-05-29"}' -H 'Content-Type: application/json' http://localhost:8080/graduation/rest/admin/menu/100007?restaurantId=100003 --user admin@gmail.com:admin`
 
 #### create Menu
  `curl -s -X POST -d '{"date":"2019-09-09"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/rest/admin/menu?restaurantId=100004 --user admin@gmail.com:admin`
  
 #### get Dish by id and menu id
  `curl -s http://localhost:8080/graduation/rest/profile/dish/100012?menuId=100007  --user user@mail.ru:password`
  
 #### get all Dish
  `curl -s http://localhost:8080/graduation/rest/profile/dish?menuId=100008 --user user@mail.ru:password`
  
 #### delete Dish
  `curl -s -X DELETE http://localhost:8080/graduation/rest/admin/dish/100021?menuId=100011 --user admin@gmail.com:admin`
  
 #### update Dish
  `curl -s -X PUT -d '{"name":"Update", "price":"100500"}' -H 'Content-Type: application/json' http://localhost:8080/graduation/rest/admin/dish/100012?menuId=100007 --user admin@gmail.com:admin`
  
 #### create Dish
  `curl -s -X POST -d '{"name":"Create", "price":"100500"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/rest/admin/dish?menuId=100007 --user admin@gmail.com:admin`
  
 #### get today rating by restaurant
  `curl -s http://localhost:8080/graduation/rest/profile/vote/todayRating?restaurantName=White+Rabbit --user seconduser@mail.ru:password`
  `curl -s http://localhost:8080/graduation/rest/profile/vote/todayRating?restaurantName=Remy+Kitchen+Bakery --user user@mail.ru:password`
  
 #### get history vote by user
  `curl -s http://localhost:8080/graduation/rest/profile/vote  --user seconduser@mail.ru:password`
  
 #### to vote
  `curl -s -X POST -d '{"date":"2019-09-09", "userId":"100001", "restaurantName":"White Rabbit"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/rest/profile/vote --user seconduser@mail.ru:password`
  `curl -s -X POST -d '{"date":"2019-09-09", "userId":"100001", "restaurantName":"Remy Kitchen Bakery"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/rest/profile/vote --user seconduser@mail.ru:password`
   
 #### get Restaurant rating by date
  `curl -s "http://localhost:8080/graduation/rest/admin/vote/ratingByDate?restaurantName=White+Rabbit&date=2019-07-21" --user admin@gmail.com:admin`
  
 #### get all Restaurant rating group by date
  `curl -s http://localhost:8080/graduation/rest/admin/vote/ratingGroupByDate?restaurantName=White+Rabbit --user admin@gmail.com:admin`