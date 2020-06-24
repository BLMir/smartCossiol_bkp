import http
import json
import machine
import binascii
import pycom
import struct

class Auth:
    PATH = "api/authenticate"
    def __init__(self):
        with open('resources/config.json') as configFile:
            config = json.load(configFile)
        self.host = config['HOST']
        self.port = config['PORT']

    def authenticate(self):
        body = self.getBodyCredentials()
        response = http.postRequest(self.host, self.PATH, self.port, body)
        idToken = json.loads(response)["id_token"]
        print("asdf")
        try:
            with open('/flash/log.txt', 'a') as f:
                f.write(idToken)
                print("write")
            with open('/flash/log.txt') as f:
                value = f.read()
        
            print(value)

        except OSError as e:
            print('Error {} writing to file'.format(e))
        # pycom.nvs_set('id_token', struct.unpack('>i', idToken.decode('hex')))        


    def getBodyCredentials(self):
        return "{\"username\":\""+ self.getMachineUniqueId() +"\" , \"password\":\"" + self.getMachineUniqueId() +"\"} "

    def getMachineUniqueId(self):
        return str(binascii.hexlify(machine.unique_id(), '_').decode())