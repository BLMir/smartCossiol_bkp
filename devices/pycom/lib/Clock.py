import pycom
import socket
import machine
from machine import Timer
import http
import json

class Clock:
    PATH = "api/stats"
    def __init__(self,time,period,psoil,ptemp):
        self.handler = ""
        #self.value = 0
        self.time = time
        self.periodic=period
        #self.apin = machine.ADC().channel(pin='P16')
        #self.soilPin = machine.Pin('P22' , mode = machine.Pin.IN)
        self.apin = ptemp
        self.soilPin = psoil

    def _set_alarm(self):
        self.__alarm = Timer.Alarm(self.handler, self.time, periodic=self.periodic)

    def _measure_soil(self, alarm):
        print("interrupt soil")
        #if self.value==0 and self.soilPin.value()==1 or self.value==1 and self.soilPin.value()==0:
            #url = 'https://192.168.1.131/'
            #http.http_post(url,self.soilPin.value())
            #print("send post")
        print(self.soilPin.value())
        self.value = self.soilPin.value()
        return 0

    def _measure_temp(self, alarm):
        print("interrupt_temppp")
        millivolts = self.apin.voltage()
        degC = ((millivolts - 500.0) / 10.0)
        degF = ((degC * 9.0) / 5.0) + 32.0

        print(degC)
        print(degF)

    def _measure_values (self,alarm):
        print("*******************************")
        print("SOIL VALUE*********************")
        print(self.soilPin.value())

        print("TEMP VALUE*********************")
        millivolts = self.apin.voltage()
        degC = ((millivolts - 500.0) / 10.0)+10
        degF = ((degC * 9.0) / 5.0) + 32.0

        print(degC)
        with open('resources/config.json') as configFile:
            config = json.load(configFile)

        http.sendMeasure(config["HOST"],self.PATH, config["PORT"], self.getBodyStats())
        print("******************************")

    def getBodyStats(self):
        return "{\"devices\":{ \"id\" : 1 } , \"insertAt\":\"2020-06-27T11:29:40.705Z\", \"light\" : 0, \"soil\":2 , \"temp\": 2} "