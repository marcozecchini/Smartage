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

## Useful links
* [Initial Concept & User Evaluation presentation](https://www.slideshare.net/AlessandroTrapasso/smartage)
* [MVP presentation](https://www.slideshare.net/AlessandroTrapasso/smartage-student-group-projects-mvp)
* Final presentation
* [Discovery Kit STM32L072Z code](https://os.mbed.com/users/marcozecchini/code/Smartage/)
