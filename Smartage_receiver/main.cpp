/*
 * Copyright (c) 2018 HELIOS Software GmbH
 * 30826 Garbsen (Hannover) Germany
 * Licensed under the Apache License, Version 2.0);
 */
 #include "main.h"


DigitalOut myled(LED);

int main() {
#ifdef HELTEC_STM32L4
    DigitalOut vext(POWER_VEXT);
    vext = POWER_VEXT_ON;
#endif    
    /*
     * inits the Serial or USBSerial when available (230400 baud).
     * If the serial uart is not is not connected it swiches to USB Serial
     * blinking LED means USBSerial detected, waiting for a connect.
     * It waits up to 30 seconds for a USB terminal connections 
     */
    InitSerial(30*1000, &myled); 
    print_stuff();
    while (1){
        ReceiveAndSend();
    }
}