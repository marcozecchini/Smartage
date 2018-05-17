/*
 * Copyright (c) 2018 Helmut Tschemernjak
 * 30826 Garbsen (Hannover) Germany
 */
 #include "main.h"
 
time_t cvt_date(char const *date, char const *time);

BufferedSerial *ser;
#ifdef FEATURE_USBSERIAL
USBSerialBuffered *usb;
#endif
bool _useDprintf;

void InitSerial(int timeout, DigitalOut *led)
{
    _useDprintf = true;
    bool uartActive;
    {
        {
            // need to turn rx low to avoid floating signal
            DigitalOut rx(USBRX);
            rx = 0;
        }
        DigitalIn uartRX(USBRX);
        uartActive = uartRX.read();
    }
#ifdef FEATURE_USBSERIAL
    if (!uartActive) {
        usb = new USBSerialBuffered();
        Timer t;
        t.start();
        while(!usb->connected()) {
            if (led)
                *led = !*led;
            wait_ms(100);
            if (timeout) {
                if (t.read_ms() >= timeout)
                    return;
            }
        }
        return;
    } else {
#else
    {
#endif
        ser = new BufferedSerial(USBTX, USBRX);
        ser->baud(230400);
        ser->format(8);
    }
    time_t t = cvt_date(__DATE__, __TIME__);
    if (t > time(NULL)) {
        set_time(t);
    }

}

void printTimeStamp()
{
    static LowPowerTimer *timer;
    if (!timer) {
        timer = new LowPowerTimer();
        timer->start();
    }
    time_t seconds = time(NULL);
    struct tm *tm = localtime(&seconds);
    int usecs = timer->read_us();
    if (usecs < 0) {
        usecs = 0;
        timer->stop();
        timer->reset();
        timer->start();
    }
    int msecs = usecs % 1000000;
    
    rprintf("%02d:%02d:%02d.%06d ", tm->tm_hour, tm->tm_min, tm->tm_sec, msecs);
}

void dprintf(const char *format, ...)
{
    std::va_list arg;

    va_start(arg, format);
    VAprintf(true, true, _useDprintf, format, arg);
    va_end(arg);
}

void rprintf(const char *format, ...)
{
    std::va_list arg;

    va_start(arg, format);
    VAprintf(false, false, _useDprintf, format, arg);
    va_end(arg);   
}

void VAprintf(bool timstamp, bool newline, bool printEnabled, const char *format, va_list arg)
{
     if (!printEnabled)
        return;

    if (timstamp)
        printTimeStamp();
#ifdef FEATURE_USBSERIAL
    if (usb) {
        usb->vprintf_irqsafe(format, arg);
        if (newline)
            usb->printf_irqsafe("\r\n");
#else
    if (0) {
#endif
    } else if (ser) {
        // serial jas 
        int r = 0;
        r = vsnprintf(NULL, 0, format, arg);
        if (r < 82) {
            char buffer[82+1];

            vsnprintf(buffer, sizeof(buffer), format, arg);
            r = ser->write(buffer, r);
        } else {
            char *buffer = new char[r+1];
            if (buffer) {
                vsnprintf(buffer, r+1, format, arg);
                r = ser->write(buffer, r);
                delete[] buffer;
            } else {
                error("%s %d cannot alloc memory (%d bytes)!\r\n", __FILE__, __LINE__, r+1);
                r = 0;
            }
        }
        if (newline)
            ser->write("\r\n", 2);
    }
}


void dump(const char *title, const void *data, int len, bool dwords)
{
    dprintf("dump(\"%s\", 0x%x, %d bytes)", title, data, len);

    int i, j, cnt;
    unsigned char *u;
    const int width = 16;
    const int seppos = 7;

    cnt = 0;
    u = (unsigned char *)data;
    while (len > 0) {
        rprintf("%08x: ", (unsigned int)data + cnt);
        if (dwords) {
            unsigned int *ip = ( unsigned int *)u;
            rprintf(" 0x%08x\r\n", *ip);
            u+= 4;
            len -= 4;
            cnt += 4;
            continue;
        }
        cnt += width;
        j = len < width ? len : width;
        for (i = 0; i < j; i++) {
            rprintf("%2.2x ", *(u + i));
            if (i == seppos)
                rprintf(" ");
        }
        rprintf(" ");
        if (j < width) {
            i = width - j;
            if (i > seppos + 1)
                rprintf(" ");
            while (i--) {
                rprintf("%s", "   ");
            }
        }
        for (i = 0; i < j; i++) {
            int c = *(u + i);
            if (c >= ' ' && c <= '~')
                rprintf("%c", c);
            else
                rprintf(".");
            if (i == seppos)
                rprintf(" ");
        }
        len -= width;
        u += width;
        rprintf("\r\n");
    }
    rprintf("--\r\n");
}

/*
 * Convert compile time to system time
 */
time_t
cvt_date(char const *date, char const *time)
{
    char s_month[5];
    int year;
    struct tm t;
    static const char month_names[] = "JanFebMarAprMayJunJulAugSepOctNovDec";
    sscanf(date, "%s %d %d", s_month, &t.tm_mday, &year);
    sscanf(time, "%2d %*c %2d %*c %2d", &t.tm_hour, &t.tm_min, &t.tm_sec);
    // Find where is s_month in month_names. Deduce month value.
    t.tm_mon = (strstr(month_names, s_month) - month_names) / 3;
    t.tm_year = year - 1900;
    return (int)mktime(&t);
}
