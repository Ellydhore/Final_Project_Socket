# Final_Project_Socket
Added Server and ClientHandler
UI Reworked
Chat System Implemented
Load last messages

[JDK 21]
This project focuses on implementing a sign-up, sign-in, and chat system.

*-_-_-_-_-_ RUN XAMPP and Server class before running this program! -_-_-_-_-_*

-------------------------------------------------------------------------------------
!! LIST OF Files/Directories/Packages THAT YOU ONLY NEED TO KNOW !!

final_project_socket/
>src/
>main/
>java/
>com.example.final_project_socket/    <--(Everything in this package)
>database/
>fxml_controller/
>handler/
>socket/
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

[2] The SceneHandler class manages the switching of scenes between the Sign-Up, Sign-In,
and Chat-Box FXML files.

-------------------------------------------------------------------------------------
!! DATABASE !!

[1] Add the JDBC (Java Database Connectivity) MySQL connector dependency to Maven by including
the following dependency in your pom.xml file.
(See link: https://mvnrepository.com/artifact/com.mysql/mysql-connector-java/8.0.0).

Make sure to require and import sql class properly

[2] To configure MySQL entries, including the database root and password,
refer to the MySQLConnect class.

[3] Database Schema (Update this info for every system or database!)
Create a new database named 'dbyapper' or configure it in the MySQLConnector class.
If you do not have a copy of the database, uncomment the code in the Main class that
creates the necessary tables.


-------------------------------------------------------------------------------------
!! Socket !!

[1] The Server and ClientHandler classes may be run on separate machine.
Just make sure to add the proper IP address.

[2] It is important that the server receives a username as the first message.
Check the ClientHandler class for details.

-------------------------------------------------------------------------------------
!! References !!

    (Database)
    - https://www.youtube.com/watch?v=AHFBPxWebFQ
    - https://www.youtube.com/watch?v=ltX5AtW9v30

    (Socket)
    - https://www.youtube.com/watch?v=gLfuZrrfKes&t=1274s
    - https://www.youtube.com/watch?v=_1nqY-DKP9A&t=139s