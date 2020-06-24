import socket
import json

def postRequest(host,path,port, body):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host,8080))

    payloadSchema = ("POST /{path} HTTP/1.1\r\n" # send headers
        "HOST:  {host}\r\n"
        "Content-Length: {content_length}\r\n"
        "Content-Type: application/json\r\n"
        "\r\n" # blank line seperating headers from body 
        "{body_json}")

    payload = payloadSchema.format(
        path = path,
        content_length=len(str(body).encode('ascii')),
        host=str(host) + ":" + str(port),
        body_json=body
    )
    
    print(payload)
    s.send(payload)

    response= b''
    while True:
        chunk = s.recv(1024)
        if not chunk:
            break
        response += chunk

    result = response.split(b'd5\r\n', 1)[1].split(b'\r\n0\r\n\r\n',1)[0]

    return (str(result, 'utf8'))