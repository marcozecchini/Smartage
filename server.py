import serial
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

    # As an admin, the app has access to read and write all data, regradless of Security Rules
    ref = db.reference('GCì')
    print(ref.get())

def check_if_present(gc_id):
    if (db.reference('GCs/'+gc_id).get() == None):
        return False
    else:
        return True

def update_value(gc_id, value):
    ref = db.reference('GCs')
    gc_ref = ref.child(gc_id)
    gc_ref.update({'distance' : value})

def create_value(gc_id, value):
    ref = db.reference('GCs')
    ref.set({
        gc_id : {
            'distance' : value
        }
    })


next_message = 0
ser = serial.Serial()
ser.port = 'COM3'
ser.baudrate = 230400
ser.open()
print("WAITING FOR MESSAGES")
distance = 0
identifier= ""

create_db_connection()

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

                if (check_if_present(identifier)):
                    update_value(identifier, distance)
                else:
                    create_value(identifier, distance)

                print("{0} {1}".format(distance, identifier))
                next_message=0

    except UnicodeDecodeError:
           print("Errore nello unicode")
    except ValueError:
           print("Ricevuto valore non valido")
    except KeyboardInterrupt:
           print("Fine esecuzione")
           exit()
