import pycom
import socket
import machine
from machine import Timer
import http

class Clock:
    def __init__(self,time,period):
        self.handler = ""
        self.value = 0
        self.time = time
        self.periodic=period
        self.apin = machine.ADC().channel(pin='P16')
        self.soilPin = machine.Pin('P22' , mode = machine.Pin.IN)

    def _set_alarm(self):
        self.__alarm = Timer.Alarm(self.handler, self.time, periodic=self.periodic)
    
    def _measure_soil(self, alarm):
        print("interrupt soil")
        if self.value==0 and self.soilPin.value()==1 or self.value==1 and self.soilPin.value()==0:
            url = 'https://192.168.1.131/'
            http.http_post(url,self.soilPin.value())
            print("send post")
            
        self.value = self.soilPin.value()
        return 0

    def _measure_temp(self, alarm):
        print("interrupt_temppp")
        millivolts = self.apin.voltage()
        degC = (millivolts - 500.0) / 10.0
        degF = ((degC * 9.0) / 5.0) + 32.0
  
        print(degC)
        print(degF)