import socket
from Auth import Auth

def postRequestWithResponse(host,path,port, body):
    s = getSocket(host, port)

    payloadSchema = ("POST /{path} HTTP/1.1\r\n" # send headers
        "HOST:  {host}\r\n"
        "Content-Length: {contentLength}\r\n"
        "Content-Type: application/json\r\n"
        "\r\n" # blank line seperating headers from body 
        "{bodyJson}")

    payload = payloadSchema.format(
        path = path,
        contentLength=len(str(body).encode('ascii')),
        host=str(host) + ":" + str(port),
        bodyJson=body
    )
    s.send(payload)

    response= b''
    while True:
        chunk = s.recv(1024)
        if not chunk:
            break
        response += chunk

    return (cleanResponse(response))

def sendMeasure(host,path,port, body):
    s = getSocket(host, port)

    payloadSchema = ("POST /{path} HTTP/1.1\r\n" # send headers
        "HOST:  {host}\r\n"
        "Content-Length: {content_length}\r\n"
        "Authorization: Bearer {idToken}\r\n"
        "Content-Type: application/json\r\n"
        "\r\n" # blank line seperating headers from body 
        "{bodyJson}")

    payload = payloadSchema.format(
        path = path,
        contentLength=len(str(body).encode('ascii')),
        host=str(host) + ":" + str(port),
        bodyJson=body,
        idToken = Auth().getToken()
    )

    s.send(payload)
    
def getSocket(host, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host,port))

    return s

def cleanResponse(response):
    result = response.split(b'd5\r\n', 1)[1].split(b'\r\n0\r\n\r\n',1)[0]

    return (str(result, 'utf8'))