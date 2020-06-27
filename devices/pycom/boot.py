import json
import http
from Connect import Connect
from Auth import Auth

with open('resources/config.json') as configFile:
    config = json.load(configFile)

Connect(config['SSID'], config['PASS']).wifi()
Auth().authenticate()
print(Auth().getToken())