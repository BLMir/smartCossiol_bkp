import socket
import json
import machine
import binascii

def http_get(url):
    _, _, host, path = url.split('/', 3)
    print(host)
    print(path)
    addr = socket.getaddrinfo(host, 8088)[0][-1]
    print(addr)
    s = socket.socket()
    s.connect(addr)
    print(bytes('GET /%s HTTP/1.0\r\nHost: %s\r\n\r\n' % (path, host), 'utf8'))
    s.send('POST /soil HTTP/1.0\r\nHost: 192.168.1.131\r\n Content-Type: application/json\r\n {} \r\n')
    while True:
        data = s.recv(10)
        if data:
            print(str(data, 'utf8'), end='')
        else:
            break
    s.close()
    return 0 

def http_post(url,data):
    _, _, host, path = url.split('/', 3)
    port = 8080
    print(host)
    print(path)

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    s.connect((host,8080))

    payloadSchema = ("POST /{path} HTTP/1.1\r\n" # send headers
        "HOST:  {host}\r\n"
        "Content-Length: {content_length}\r\n"
        "Content-Type: {content_type}\r\n"
        "\r\n" # blank line seperating headers from body 
        "{body_json}")

    body = "{\"username\":\""+ str(binascii.hexlify(machine.unique_id(), '_').decode()) +"\" , \"password\":\"" + str(binascii.hexlify(machine.unique_id(), '_').decode()) +"\"} "
    print(body)
    body_bytes = str(body).encode('ascii')
    payload = payloadSchema.format(
        path = path,
        content_type="application/json",
        content_length=len(body_bytes),
        host=str(host) + ":" + str(port),
        body_json=body
    )
    
    print(payload)
    s.send(payload)
    while True:
        data = s.recv(4096)
        if data:
            print(str(data, 'utf8'), end='')
        else:
            break
    s.close()