[![Swagger Validator](https://img.shields.io/badge/swagger-integrated-brightgreen.svg)](https://swagger.io/)

# OnlineShopping
A REST HTTP service for an e-commerce website. 

#### Create Database(MySQL) Schema
* ```cd src/main/resources```
* Modify ```mysql_creds.cnf``` with mysql username and password
* ```sh create_schema.sh```


## Run from IntelliJ
* Program Arguments: ```server src/main/resources/config.yaml```

#### To server static content -
    
1) Open project module settings
2) `Dependencies` tab
3) Select '+' -> JARs or Directories..
4) Select the directory containing the 'assets' folder which has the static files.
5) Select `Classes` when prompted to choose the category.

Now static files under the `assets` folder will be served in the path `/assets/image.jpg`

## Run from Command Line
* ```mvn clean install```
* ```java -jar path/to/jar.jar server path/to/config.yaml```

To server static content, have a directory named `static` in the same directory as the jar and put all the static contents under the `assets` folder inside the `static` directory.

__The directory should be named 'static' and should be in the same path as the jar file because the maven shade plugin is configured to add this static folder to the class path__