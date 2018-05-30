import serial
from serial import SerialException

import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

def create_db_connection():
    # Fetch the service account key JSON file contents
    cred = credentials.Certificate('smartage-ps2018-firebase.json')
    # Initialize the app with a service account, granting admin privileges
    firebase_admin.initialize_app(cred, {
        'databaseURL': 'https://smartage-ps2018.firebaseio.com/'
    })

def check_if_present(gc_id):
    if (db.reference('GCs/'+gc_id).get() == None):
        return False
    else:
        return True

def update_value(gc_id, value, empty, temperature, gyro):
    ref = db.reference('GCs')
    gc_ref = ref.child(gc_id)
    gc_ref.update({
        'distance' : value,
        'empty' : empty,
        'temperature' : temperature,
        'gyro' : gyro
    })

def create_value(gc_id, value, empty, temperature, gyro):
    ref = db.reference('GCs')
    ref.set({
        gc_id : {
            'distance' : value,
            'empty' : empty,
            'temperature' : temperature,
            'gyro' : gyro
        }
    })

#variable to check whether the next serial messages are to catch or not
next_message = 0
#set the serial communication
ser = serial.Serial()
ser.port = 'COM3'
ser.baudrate = 230400
ser.open()
print("WAITING FOR MESSAGES")

distance = 0
identifier= ""
empty = 0
temperature = 0
gyro = ""
create_db_connection()

while (True):
    try:
        line = ser.readline()
        print(line)
        if (b'DISTANCE' in line): #20 cm il prototipo da vuoto
            next_message = 5
        elif(next_message == 5):
            distance=int(line.strip().split(b" ")[1].decode("utf-8")[0:3])
            #print(distance)
            next_message = 4
        elif(next_message == 4):
            #print(line.strip().split(b" ")[1].decode("utf-8")[0:4])
            identifier=line.strip().split(b" ")[1][0:4].decode("utf-8")
            next_message = 3
        elif(next_message == 3):
            #print(line.strip().split(b" ")[1].decode("utf-8")[0:3])
            empty=int(line.strip().split(b" ")[1][0:3].decode("utf-8"))
            #print(empty)
            next_message = 2
        elif(next_message == 2):
            next_message = 1
            #print(line.strip().split(b" ")[1][0:4].decode("utf-8"))
            temperature = float(line.strip().split(b" ")[1][0:4].decode("utf-8"))
        elif(next_message == 1):
            #print(line)
            if (b"FI" in line):
                gyro = "FINE";
            else:
                gyro = "TILT";
            #print(gyro)
            if (check_if_present(identifier)):
                update_value(identifier, distance, empty, temperature, gyro)
            else:
                create_value(identifier, distance, empty, temperature, gyro)
            print("----------------------- > {0} {1} {2} {3} {4}".format(distance, identifier, empty, temperature, gyro))
            next_message=0

    except UnicodeDecodeError:
           #print("Errore nello unicode")
            continue
    except ValueError:
           #print("Ricevuto valore non valido")
            continue
    except KeyboardInterrupt:
           print("Fine esecuzione")
           exit()
    except IndexError:
            print("Errore nell' indice");
    except SerialException:
        print("Sembra essersi disconnesso il device")
        exit()
