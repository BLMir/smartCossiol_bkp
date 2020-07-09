from Clock import Clock
import gpIO

def init():

    psoil,ptemp=gpIO.pin_config()
    #gpIO.callback_soil(psoil)

    clock_measure = Clock(20,True,psoil,ptemp)
    clock_measure.handler = clock_measure._measure_values
    clock_measure._set_alarm()
