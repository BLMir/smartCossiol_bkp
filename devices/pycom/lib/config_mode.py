import pycom
import time
import machine

def set_mode(n_mode):
    pycom.nvs_set('mode', n_mode)

def set_value_temp_alarm(vatemp):
    pycom.nvs_set('vatemp', vatemp)

def init():
    print("config_mode")
    print("*****************************")
    print("***  set temp alarm value ***")
    print("***  ************************")

    mode=input()
    #set_value_temp_alarm(n_vat)

    set_mode(1)
    time.sleep(5)
    machine.reset()
