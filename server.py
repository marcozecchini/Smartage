import serial

next_message = False;
ser = serial.Serial()
ser.port = 'COM3'
ser.baudrate = 230400
ser.open()
print("WAITING FOR MESSAGES")

while (True):
    
    line = ser.readline()
    if (b'DISTANCE' in line):
        next_message = True
    elif(next_message == True):
        try:
            distance=int(line.strip().split(b" ")[1].decode("utf-8"))
            print(distance)
            next_message=False
        except UnicodeDecodeError:
            print("Errore nello unicode")
        except ValueError:
            print("Ricevuto valore non valido")
        
        #TODO estrae la distanza e la invia su internet
