# Smartage
Smartage is a solution for the management of the garbage aiming to simplify the monitoring and the collection in a city. It measures the fill-level of a garbage collector, while monitoring the temperature and the orientation and provides an Android application that allows to have a view of the garbage collectors in an area, and provides the possibility to develop the shortest path among them.

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

The following screenshots show how to app works, the facility that it offers and how it's very easy to use!

![alt-text](https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/app.png) 
![alt-text](https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/app2.png)

## Prototype
These are images of the prototype realized by our team

<a href="url"><img src="https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/1.jpg" height="307.12" width="401.13" ></a>
<a href="url"><img src="https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/2.jpg"  height="308.87" width="431.12" ></a>
<a href="url"><img src="https://github.com/marcozecchini/Smartage/blob/master/ReadMe%20Images/IMG_9162.jpg"  height="658" width="836" ></a>

## ARM MBED OS Code

### Sender code
The code within the board cyclically does the following:

* It collects the data from the sensors (distance, temperature, orientation) as shown in the following snippet of code within the main function

```c++
int main() {
  [...]
    hum_temp->enable();
    
    /* Enable LSM6DSL accelerometer */
    acc_gyro->enable_x();
    /* Enable 6D Orientation. */
    acc_gyro->enable_6d_orientation();
    
    print_stuff();
    bool empty = true;
    //WAIT
    wait(7.5f);
    while(1){
        
        char distance[4], empty_distance[4], temperature[4];//Message to be sent
        get_distance(distance);
        get_temperature(temperature);
        notification = send_orientation(); 
        
        if(empty) { //the very first time it also sends the distance of the empty garbage collector
            memcpy(empty_distance, distance, 4);
            SendAndBack((uint8_t*)distance, (uint8_t*)empty_distance, (uint8_t*)temperature, notification); 
            empty = false;
            }
        else{
            SendAndBack((uint8_t*)distance, (uint8_t*)empty_distance, (uint8_t*)temperature, notification);
        }
        
    }
}
```

* It encrypts the data and the send this with LoRa to the receiver until either it receives an ack or for five times.

```c++
void SendAndBack(uint8_t* str, uint8_t* empty_distance, uint8_t* temperature, bool tilt_status)
{
  [...]

    int trasmission_routine = 0;
    AES myAES(AES_128, myKEY, myIV);
    while (trasmission_routine<=WHILE_QUANTITY){
        switch( State )
        {
        case RX:
            //It means that I've received the ack
            *led3 = 0;
        
            if( BufferSize > 0 )
            {
                //setting trasmissione_routine in order to end the while 
                trasmission_routine = WHILE_QUANTITY+1;    
            }
            State = LOWPOWER;
            break;
        case DO_TX:
            *led3 = 1;
            
            // We fill the buffer with numbers for the payload 
            Buffer[4]='G';
            Buffer[5]='C';
            Buffer[6]='-';
            Buffer[7]='1';
            i += 4;
            // Then it follows the distance
            memcpy(Buffer, str, sizeof(str));
            i += 4;
            // Then it follows the empty distance
            memcpy(Buffer+8, empty_distance, sizeof(empty_distance));
            i+= 4;
            // Then temperature ...
            memcpy(Buffer+12, temperature, sizeof(temperature));
            i +=4;
            
            //Finally, tilt status
            if (tilt_status){
                Buffer[16] = 'T';
                Buffer[17] = 'I';
                Buffer[18] = 'L';
                Buffer[19] = 'T';
                
            }
            else{
                Buffer[16] = 'F';
                Buffer[17] = 'I';
                Buffer[18] = 'N';
                Buffer[19] = 'E';
    
            }
            i += 4; 
             
            for( i; i < BufferSize; i++ )
            {
                Buffer[i] = i - sizeof(str)+4;
            }
            dump("Check: ", Buffer, BufferSize);
            myAES.encrypt(Buffer, Buffer, BufferSize);
            dump("Crypto: ", Buffer, BufferSize);
            wait_ms( 10 ); 
            Radio->Send( Buffer, BufferSize );
            trasmission_routine += 1;
            State = LOWPOWER;
            break;
        case TX:
            Radio->Rx( RX_TIMEOUT_VALUE );
            State = LOWPOWER;
            break;
        case TX_TIMEOUT:
            Radio->Rx( RX_TIMEOUT_VALUE );
            State = LOWPOWER;
            break;
        case LOWPOWER:
            sleep();
            break;
        default:
            State = LOWPOWER;
            break;
        }    
    }
    dprintf("> Finished!");
    //wait for a bit - in seconds.
    wait(10.0f);
    //destroy led led3 e Buffer e radio
    delete(led);
    delete(led3);
    delete(Buffer);
    delete(Radio);
}
```
* It sleeps for a bit

### Receiver code
* It is always listening for new LoRa messages.
* When one arrives, the board decrypts it.
* Knowing how the message is organized, the board writes some messages on the serial port containing the values of the sensor that it has just received.

To see the code within the boards visit [this page](https://os.mbed.com/users/marcozecchini/code/Smartage/)

## Python script code
Smartage needs a Python script to read data passed by the receiver device on the serial port and to upload this data on Firebase. The file implementing this facility read correctly the formatted data on the serial port and using some functions verify if it's necessary to upload some data in the realtime database for an already stored garbage collector or to register for the first time the garbage collector and its data.
To see the script visit [this page](https://github.com/marcozecchini/Smartage/blob/master/server.py)


## Useful links
* [Initial Concept & User Evaluation presentation](https://www.slideshare.net/AlessandroTrapasso/smartage)
* [MVP presentation](https://www.slideshare.net/AlessandroTrapasso/smartage-student-group-projects-mvp)
* [Final presentation](https://www.slideshare.net/AlessandroTrapasso/smartage-finalpresentation)

## Contributors 
* [Marco Zecchini](https://www.linkedin.com/in/marco-zecchini/)
* [Flavio Massimo Falesiedi](https://www.linkedin.com/in/flavio-massimo-falesiedi-37b61b163/)
* [Alessandro Trapasso](https://www.linkedin.com/in/alessandro-trapasso/)
* [Federico Mascoma](https://www.linkedin.com/in/federico-mascoma/)
