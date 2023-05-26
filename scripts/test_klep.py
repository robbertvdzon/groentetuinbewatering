from time import *
import RPi.GPIO as GPIO
from time import *
import time,threading

DIRECTION_OPEN = GPIO.HIGH
DIRECTION_CLOSE = GPIO.LOW
FULL_TIME = 3
CLOSE_TO_HALF_TIME = 1
OPEN_TO_HALF_TIME = 0.6
STATE_CLOSED = 0
STATE_HALF = 1
STATE_OPEN = 2



GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(12, GPIO.OUT) # aan / uit
GPIO.setup(17, GPIO.OUT) # richting
GPIO.output(12, GPIO.HIGH)




def open(time):
    direction(DIRECTION_OPEN)
    motorOn()
    threading.Timer(time, motorOff).start()

def close(time):
    direction(DIRECTION_CLOSE)
    motorOn()
    threading.Timer(time, motorOff).start()

def motorOn():
    GPIO.output(12, GPIO.HIGH)

def motorOff():
    GPIO.output(12, GPIO.LOW)
    busy = False

def direction( direction):
    GPIO.output(17, direction)

close(FULL_TIME)
time.sleep(5)
open(FULL_TIME)
