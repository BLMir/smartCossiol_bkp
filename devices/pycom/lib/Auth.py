import http
import json
import machine
import binascii

class Auth:
    PATH = "api/authenticate"
    def __init__(self):
        with open('resources/config.json') as configFile:
            config = json.load(configFile)
        self.host = config['HOST']
        self.port = config['PORT']

    def authenticate(self):
        body = self.getBodyCredentials()
        response = http.postRequestWithResponse(self.host, self.PATH, self.port, body)

        idToken = self.getTokenFromResponse(response)
        self.setToken(idToken)

    def getBodyCredentials(self):
        return "{\"username\":\""+ self.getMachineUniqueId() +"\" , \"password\":\"" + self.getMachineUniqueId() +"\"} "

    def getMachineUniqueId(self):
        return str(binascii.hexlify(machine.unique_id(), '_').decode())

    def setToken(self, idToken):
        try:
            with open('/flash/token.txt', 'a') as f:
                f.write(idToken)
        except OSError as e:
            print('Error {} sotring the token to file'.format(e))

    def getToken(self):
        try:
            with open('/flash/token.txt') as f:
                result = f.read()
                f.close()
                return result
        except OSError as e:
            print('Error {} getting the token from file'.format(e))

    def getTokenFromResponse(self, response):
        return (json.loads(response)["id_token"])
