import pycom
import machine
import sys 
sys.path.append('/lib')
from Clock import Clock

pycom.heartbeat(False)

clock_temp = Clock(10,True)
clock_temp.handler = clock_temp._measure_temp
clock_temp._set_alarm()

clock_soil = Clock(50,True)
clock_soil.handler = clock_soil._measure_soil
clock_soil._set_alarm()

machine.idle()