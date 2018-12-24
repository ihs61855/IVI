import _socketServer as socketServer
import threading
import queue
from time import sleep

recv_q = queue.Queue()
send_q = queue.Queue()
iviClient = {}
mobileClient = {}

def recvThread(soc):
    global recv_q
    while True:
        recv_q.put([soc, socketServer.recv_Socket(soc)])

def sendThread():
    global send_q
    while True:
        if not send_q.empty():
            data = send_q.get()
            socketServer.send_Socket(data[0], data[1])

def connectIVI():
    global iviClient
    id = 0
    while True:
        iviClient[id] = socketServer.connectServer(socketServer.createServer(4000))
        threading.Thread(target=recvThread, args=[iviClient[len(iviClient) - 1][0]]).start()
        id = id + 1
        print("IVI", id - 1, "is Connect Success")

def connectMobile():
    global mobileClient, send_q
    id = 0
    while True:
        mobileClient[id] = socketServer.connectServer(socketServer.createServer(3000))
        threading.Thread(target=recvThread, args=[mobileClient[len(mobileClient) - 1][0]]).start()
        id = id + 1
        print("Mobile", id - 1, "Connect Success")

if __name__ == '__main__':
    connectThreads = [threading.Thread(target=connectIVI), threading.Thread(target=connectMobile), threading.Thread(target=sendThread)]
    for threadsNumber in range(len(connectThreads)):
        connectThreads[threadsNumber].start()
    try:
        while True:
            if not recv_q.empty():
                recv_data = recv_q.get()
                print(recv_data[1])
                if recv_data[1]["Query"] == "RequestID":
                    for key in iviClient.keys():
                        if iviClient[key][0] == recv_data[0]:
                            send_q.put([recv_data[0], {"Query" : "ResponseID", "ID" : str(key)}])
                            break

                    for key in mobileClient.keys():
                        if mobileClient[key][0] == recv_data[0]:
                            send_q.put([recv_data[0], {"Query" : "ResponseID", "ID" : str(key)}])
                            break
                
                elif recv_data[1]["Query"] == "ResponseList":
                    send_q.put([mobileClient[0][0], recv_data[1]])
                
                else:
                    send_q.put([iviClient[0][0], recv_data[1]])
    except:
        for key in iviClient.keys():
            socketServer.terminateServer(iviClient[key][0])
        for key in mobileClient.keys():
            socketServer.terminateServer(mobileClient[key][0])
