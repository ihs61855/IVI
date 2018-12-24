from os import listdir
from os.path import isfile, join
from ffpyplayer.player import MediaPlayer
import json

stopFlag = False
path = r'C:\Users\Hansol\Desktop\hhhh\Prj\IVI\audio'
player = None

def read_audio_dir():
    dirFiles = ""
    for f in listdir(path):
        if isfile(join(path, f)):
            if dirFiles == "":
                dirFiles += f
            else:
                dirFiles += "," + f
    return dirFiles

def startAudio(filename):
    global player
    player = MediaPlayer(path + "/" + filename)

def playAudio():
    player.set_pause(False)

def pauseAudio():
    player.set_pause(True)

def stopAudio():
    global player
    player.close_player()
    