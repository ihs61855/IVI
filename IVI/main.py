import _socketClient as sc
import _video as video
import _picture as picture
import _audio as audio
import queue
import threading

recv_q = queue.Queue()
send_q = queue.Queue()

server_ip = 'localhost'
server_port = 4000
my_id = ""
clientSoc = None

def recvThread():
    global recv_q
    while True:
        recv_q.put(sc.recv_socket(clientSoc))

def sendThread():
    global send_q
    while True:
        if not send_q.empty():
            data = send_q.get()
            sc.send_socket(clientSoc, data)

if __name__ == '__main__':
    clientSoc = sc.start_socket(server_ip, server_port)
    threading.Thread(target=recvThread).start()
    threading.Thread(target=sendThread).start()

    send_q.put({"Query" : "RequestID"})

    try:
        while True:
            if not recv_q.empty():
                recv_data = recv_q.get()
                print(recv_data)

                if recv_data['Query'] == 'ResponseID':
                    my_id = recv_data["ID"]

                elif recv_data['Query'] == "RequestList":
                    if recv_data['Kind'] == 'Video':
                        send_q.put({"Query" : "ResponseList", "Kind" : "Video", "List" : video.read_video_dir()})
                    elif recv_data['Kind'] == 'Picture':
                        send_q.put({"Query" : "ResponseList", "Kind" : "Picture", "List" : picture.read_picture_dir()})
                    elif recv_data['Kind'] == 'Audio':
                        send_q.put({"Query" : "ResponseList", "Kind" : "Audio", "List" : audio.read_audio_dir()})
                
                elif recv_data['Query'] == "RequestStart":
                    if recv_data['Kind'] == 'Video':
                        threading.Thread(target=video.startVideo, args=[recv_data['Title']]).start()
                    elif recv_data['Kind'] == 'Audio':
                        threading.Thread(target=audio.startAudio, args=[recv_data['Title']]).start()
                    elif recv_data['Kind'] == "Picture":
                        pass

                elif recv_data['Query'] == "RequestPlay":
                    if recv_data['Kind'] == 'Video':
                        video.playVideo()
                    elif recv_data['Kind'] == 'Audio':
                        audio.playAudio()
                        pass

                elif recv_data['Query'] == "RequestPause":
                    if recv_data['Kind'] == 'Video':
                        video.pauseVideo()
                    elif recv_data['Kind'] == 'Audio':
                        audio.pauseAudio()

                elif recv_data['Query'] == "RequestStop":
                    if recv_data['Kind'] == 'Video':
                        video.stopVideo()
                    elif recv_data['Kind'] == 'Audio':
                        audio.stopAudio()
                    elif recv_data['Kind'] == 'Picture':
                        pass

    except:
         print('Critical Main() Error')
         sc.terminate_socket(clientSoc)