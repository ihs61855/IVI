import socket
import json

def createServer(port):
    try:
        s = socket.socket()
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind(('', port))
        s.listen(5)
        return s
    except:
        print("createServer() Error")

def connectServer(soc):
    try:
        return soc.accept()
    except:
        print("connectServer() Error")

def send_Socket(soc, msg):
    try:
        soc.send(json.dumps(msg).encode())
    except:
        print("send_Socket() Error")

def recv_Socket(soc):
    try:
        while True:
            data = soc.recv(1024).decode()
            if data:
                return json.loads(data)

    except:
        print("recv_Socket() Error")

def terminateServer(soc):
    try:
        soc.close()
    except:
        print("terminateServer() Error")