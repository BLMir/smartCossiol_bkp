from network import WLAN
import machine

class Connect:
    def __init__(self, ssid, credentials):
        self.ssid = ssid
        self.credentials = credentials

    def wifi(self):
        wlan = WLAN()
        wlan = WLAN(mode=WLAN.STA)
        networks = wlan.scan()
        for net in networks:
            if net.ssid == self.ssid:
                print("Network found")
                wlan.connect(net.ssid, auth=(net.sec, self.credentials), timeout=500)
                while not wlan.isconnected():
                    machine.idle() # save power while waiting
                print('WLAN connection succeeded!')
                break