import machine
from machine import Pin


def callback_soil(p):
    p.callback(Pin.IRQ_FALLING | Pin.IRQ_RISING, pin_callback)

def pin_config():
    psoil = Pin('P22',mode = Pin.IN,pull=Pin.PULL_UP)
    ptemp = machine.ADC().channel(pin='P16')
    return psoil,ptemp

def pin_callback(p):
    print("SOIL CHANGE")
    print("INTERRUPT")
