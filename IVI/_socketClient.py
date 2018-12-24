import socket
import json

def start_socket(ip, port):
    try:
        soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        soc.connect((ip, port))
        return soc
    except:
        print("start_socket() Function Error")
        return False

def send_socket(soc, msg):
    try:
        soc.send(json.dumps(msg).encode())
        return True
    except:
        print("send_socket() Function Error")
        return False

def recv_socket(soc):
    try:
        while True:
            data = soc.recv(1024).decode()
            if data:
                return json.loads(data)
    except:
         print("recv_socket() Function Error")
         return False

def terminate_socket(soc):
    try:
        send_socket(soc, "end")
        soc.close()
        return True
    except:
        print("terminate_socket() Function Error")
        return False