ToDo App: A simple desktop To-Do List application built with Java, MongoDB, and Docker.
Generates a GUI where users can Add, Remove, or Mark Completed any task they want to track. Data for tasks is stored using MongoDB in a Docker Container.

Due to using Docker, the program sometimes takes a few seconds to actually start running.


Requirements: 

Java 17+
Gradle 8+
Docker

Once you have cloned the Repo, navigate to the project folder in command line or terminal and run the following commands:

./gradlew clean
./gradlew build
./gradlew run

or if you are using Windows:

gradlew.bat clean
gradlew.bat build
gradlew.bat run


The program should start and stop the Docker Container automatically, but if it doesnt:

Start the Container Manually before running the program by running this command

docker-compose up -d

Stop the container manually once you are finished with this command 

docker-compose down
