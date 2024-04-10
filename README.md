# Final_Project_Socket

Sign Up and Log In are implemented. Chat system however is yet to be implemented.

[JDK 21]
This project is all about the implementation of Sign-In, Log-In and, Chat system.

-------------------------------------------------------------------------------------
!! LIST OF Files/Directories/Packages THAT YOU ONLY NEED TO KNOW OR COPY !!

final_project_socket/
  >src/
    >main/
      >java/
        >com.example.final_project_socket/    <--(Everything in this package)
          >fxml_controller/
          >utility/
          >Main
          >readme.txt
        >module-info.java   <--(info)
      >resources/
        >com.example.final_project_socket/    <--(Everything in this package)
          >fxml/
          >images/
          >css/
    >pom.xml    <--(Make sure the sql connector dependency is added)

-------------------------------------------------------------------------------------
!! FXML AND CONTROLLERS !!

[1] Make sure to properly export the fxml_controller package inside
 module-info found in src/main/java/module-info.java

[2] The DBUtil class manages the switching of scenes between the Sign-Up, Sign-In,
 and Chat-Box FXML files, as well as the logic for database queries, and other
 functionalities.

-------------------------------------------------------------------------------------
!! DATABASE !!

[1] Add the JDBC (Java Database Connectivity) MySQL connector dependency to Maven by including
 the following dependency in your pom.xml file.
 (See link: https://mvnrepository.com/artifact/com.mysql/mysql-connector-java/8.0.0).

 Make sure to require and import sql class properly

[2] Database Schema (Update this info for every system or database!)
 Create a new database and name it 'dbGame'.
 Create a table called 'tblAccount' with 3 attributes accountID (INT)(PK), username (VARCHAR)
 and, password (VARCHAR).

[3] SQL entries in case you want to configure DB root and password
 signUpUser()   found in src/main/java/com/example/final_project_socket/utility/DBUtil.java
 logInUser()    found in src/main/java/com/example/final_project_socket/utility/DBUtil.java

-------------------------------------------------------------------------------------
!! References !!

    (Database)
    - https://www.youtube.com/watch?v=AHFBPxWebFQ
    - https://www.youtube.com/watch?v=ltX5AtW9v30
