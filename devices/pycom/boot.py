from network import WLAN
import machine
import json

with open('resources/config.json') as configFile:
    config = json.load(configFile)

wlan = WLAN()

wlan = WLAN(mode=WLAN.STA)
nets = wlan.scan()

for net in nets:
    print(net)
    if net.ssid == config['SSID']:
        print('Network found!OO')
        wlan.connect(net.ssid, auth=(net.sec, config['PASS']), timeout=5000)
        while not wlan.isconnected():
            machine.idle() # save power while waiting
        print('WLAN connection succeeded!')
        break