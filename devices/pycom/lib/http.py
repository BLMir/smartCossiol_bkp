import socket

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
    print(host)
    print(path)
    addr = socket.getaddrinfo(host, 8088)[0][-1]
    print(addr)
    s = socket.socket()
    s.connect(addr)
    DATA = ("POST /soil HTTP/1.1\r\n" # send headers
        # "HOST: http://192.168.1.131:8088/\r\n"
        "Content-Length: 16\r\n"
        "Content-Type: application/json\r\n"
        "\r\n" # blank line seperating headers from body 
        "{\"value\":"+str(data)+"} ")

    print(DATA)
    s.send(DATA)
    # while True:
        # data = s.recv(4096)
        # if data:
            # print(str(data, 'utf8'), end='')
        # else:
            # break
    s.close()