/*
 * Copyright (c) 2018 Helmut Tschemernjak
 * 30826 Garbsen (Hannover) Germany
  * Licensed under the Apache License, Version 2.0);
*/

#include "mbed.h"
#include "PinMap.h"
#include "BufferedSerial.h"
#ifdef FEATURE_USBSERIAL
#include "USBSerialBuffered.h"
#endif
#include "smartage.h"

extern BufferedSerial *ser;
#ifdef FEATURE_USBSERIAL
extern USBSerialBuffered *usb;
#endif
extern bool _useDprintf;
extern void InitSerial(int timeout, DigitalOut *led);  
extern void dump(const char *title, const void *data, int len, bool dwords = false);

extern void dprintf(const char *format, ...) __attribute__((format(printf,1,2)));
extern void rprintf(const char *format, ...) __attribute__((format(printf,1,2)));
extern void VAprintf(bool timstamp, bool newline, bool printEnabled, const char *format, va_list arg);