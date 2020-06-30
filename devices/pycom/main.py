import pycom
import machine
import sys
sys.path.append('/lib')
sys.path.append('/resources')
import config_mode
import normal_mode

pycom.heartbeat(False)

#pycom.rgbled(0x007f00) # green

def switch_mode(arg):
    switcher ={
    0:"config_mode",
    1:"normal_mode",
    }
    mode=(switcher.get(arg,"invalid"))
    return mode

mode = pycom.nvs_get('mode')

dev_mode=switch_mode(mode)
eval(dev_mode).init()

machine.idle()
