# Smartage
Smartage is a solution for the management of the garbage aiming to simplify the monitoring and the collection in a city. It measures the fill-level of a garbage collector, while monitoring the temperature and the orientation and provides an Android application that allows to have a view of the garbage collectors in an area, and provides the possibility to develop the shortest path among them.
This project was developped for the "Pervasive System" course edition of 2018 at "La Sapienza" university.

## Technologies involved in the project
### Hardware
* Within the garbage collector we have inserted the **Discovery Kit STM32L072Z** with an **ultrasonic sensor HC-SR04** and an Expansion Board **X-Nucleo IKS01A2** that includes the gyroscope sensor and the temperature sensor
* The data generated by the sensors are sent to another Discovery Kit STM32L072Z that exchange messages through a *LoRa* protocol connected to a PC
### Software
* **ARM MBED OS** used in the Nucleo boards
* **FireBase** as server backend
* **Google Cloud Developer** to interact with Google API (**Google Maps SDK**, **Direction API**)
* **Python Script** for serial communication with a Discovery Kit Board
* Java and Xml for the **Android Application** through Android Studio

## Architecture
![alt-text](https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/Architecture.png)
![alt-text](https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/Garbage.png)


## Android App
The Android Application analyze the changes of information inside the FireBase Database and then report on the Google map inside the App the status of the garbage collectors, providing also the possibility of compute the shortest path among them.

From this DataBase the Application also manage the User Credentials for the login. 

![alt-text](https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/app.png) 
![alt-text](https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/app2.png)

## ARM MBED OS Code
To see the code within the boards visit [this page](https://os.mbed.com/users/marcozecchini/code/Smartage/)

## Useful links
* [Initial Concept & User Evaluation presentation](https://www.slideshare.net/AlessandroTrapasso/smartage)
* [MVP presentation](https://www.slideshare.net/AlessandroTrapasso/smartage-student-group-projects-mvp)
* Final presentation

## Contributors 
* [Marco Zecchini](https://www.linkedin.com/in/marco-zecchini/)
* [Flavio Massimo Falesiedi](https://www.linkedin.com/in/flavio-massimo-falesiedi-37b61b163/)
* [Alessandro Trapasso](https://www.linkedin.com/in/alessandro-trapasso/)
* [Federico Mascoma](https://www.linkedin.com/in/federico-mascoma/)
