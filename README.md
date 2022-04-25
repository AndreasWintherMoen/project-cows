# Project COWS

**C**asual
**O**nline
**W**ar
**S**imulator

This is a project for **TDT4240 - Software Architecture** at NTNU.

## How to run

0. The project is developed with Android Studio Bumblebee using JDK 17 and Android SDK version 32. It may run with other programs and versions, but try using the same as us if you have any issues :)
1. Configure environment variables in the Client. Create an environment file named _env_ in the _assets_ folder. A template file is provided, named _env.example_, so just copy this file and rename the copy _env_. By default, it connects to a local server, but you can replace _127.0.0.1:PORT_ with our public server at _api.winthermoen.no_ (i.e. _HTTP_API_BASE=https://api.winthermoen.no/cows_ and _WS_API_BASE=wss://api.winthermoen.no/ws-cows_). 
2. Run the client (twice, as it's a 2-player online game). While you can build a JAR file, we recommend running it directly from Android Studio in either a desktop or Android configuration. Note, we have had some issues running it on Windows. If you encounter issues, try upgrading buildToolsVersion to "30.0.3", located in _build.gradle_ in the _android_ folder. 
3. Configure environment variables on the Game State Server (located in the folder named _server_). Similar to the client, a sample file named _env.example_ is provided, located in _src/main/resources_. If you want to use our public API, use the endpoint _https://api.winthermoen.no/ss-cows_.
4. Run the Game State Server. This one can either be run in Android studio with a default Kotlin configuration and Application.Kt as the main class, or by building the project using ```./gradlew shadowJar``` (Mac & Linux) or ```gradlew.bat shadowJar``` (Windows), and then running the compiled .jar file located in build/libs with ```java -jar server-0.0.1-all.jar```.
5. Run the Simulation Server. This one doesn't need environment variables, and can also be run either as a default Kotlin configuration in the IDE with Application.Kt as the main class, or as a built version using the same commands as the Game State Server. The built jar file is named _simulationserver-0.0.1-all.jar_.
