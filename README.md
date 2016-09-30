# dataset-manager
##Start development:
1. git clone https://github.com/andrewhrytsiv/dataset-manager.git
2. From Eclipce: Open Import -> Existing Maven Projects 
3. In PostgreSQL create database with settings from \src\main\resources\address.properties file
4. Init database with db/install.sql
5. Run Bootstrap.java

##Build:
1. From Eclipce run Maven build
2. Place server folder from dataset-manager/target where you want
3. Run from cmd "java -jar app-server.jar" to start application

Heroku link https://dataset-manager.herokuapp.com
