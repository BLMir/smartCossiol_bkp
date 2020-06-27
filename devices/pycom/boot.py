import network
import machine
import json
import ubinascii
import http

with open('resources/config.json') as configFile:
    config = json.load(configFile)

#wlan = network.WLAN()

#wlan = network.WLAN(mode=network.WLAN.STA)
#nets = wlan.scan()

#for net in nets:
#    print(net)
#    if net.ssid == config['SSID']:
#        print('Network found!OO')
#        wlan.connect(net.ssid, auth=(net.sec, config['PASS']), timeout=5000)
#        while not wlan.isconnected():
#            machine.idle() # save power while waiting
#        print('WLAN connection succeeded!')
#        break

#get token
#http.http_get("http://192.168.1.133/")

#http.http_post("http://192.168.1.133/api/authenticate", "hola")
