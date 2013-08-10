1) When talking about radius (get cats by area) we're actually using a rectangular shape
2) The code could be reduced significantly by using frameworks like Spring MVC and Apache CXF, but we were asked not to use them in this project
3) The DAOs are not overgeneric - we could do that, but preferred that every DAO would validate its specific model
4) To log events we use System.out.println(). Due to lack of time we decided not to spend time on configuring a proper logger such as LOG4J
5) Redis is currently used to index cats by ID.

Testing
Cats REST:
/catTest.html