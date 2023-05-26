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

open(self, FULL_TIME)
time.sleep(1)
close(self, FULL_TIME)



def open(self, time):
    self.direction(self.DIRECTION_OPEN)
    self.motorOn()
    threading.Timer(time, self.motorOff).start()

def close(self, time):
    self.direction(self.DIRECTION_CLOSE)
    self.motorOn()
    threading.Timer(time, self.motorOff).start()

def motorOn(self):
    GPIO.output(12, GPIO.HIGH)

def motorOff(self):
    GPIO.output(12, GPIO.LOW)
    self.busy = False

def direction(self, direction):
    GPIO.output(17, direction)
