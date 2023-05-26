from time import *
import RPi.GPIO as GPIO
from time import *
import time,threading

def initKnopSensor():
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(6, GPIO.IN) # pull-ups are too weak, they introduce noise
    GPIO.add_event_detect(6, GPIO.RISING, callback=knopPressedDetected, bouncetime=200) # bouncetime in mSec
    return

def knopPressedDetected(param):
    print("button")


initKnopSensor()
print("start")
sleep(20)
print("end")
