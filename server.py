import serial

next_message = 0;
ser = serial.Serial()
ser.port = 'COM3'
ser.baudrate = 230400
ser.open()
print("WAITING FOR MESSAGES")
distance = 0
identifier= ""

while (True):
    try:
        line = ser.readline()
        if (b'DISTANCE' in line):
            next_message = 2
        elif(next_message == 2):
                distance=int(line.strip().split(b" ")[1].decode("utf-8"))
                next_message=1
        elif(next_message == 1):
                identifier=line.strip().split(b" ")[1].decode("utf-8")[:-1]
                print("{0} {1}".format(distance, identifier))
                next_message=0
        
    except UnicodeDecodeError:
           print("Errore nello unicode")
    except ValueError:
           print("Ricevuto valore non valido")
    except KeyboardInterrupt:
           print("Fine esecuzione")
           exit()
        #TODO estrae la distanza e la invia su internet
