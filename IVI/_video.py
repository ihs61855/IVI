from os import listdir
from os.path import isfile, join
import cv2

path = r'C:\Users\Hansol\Desktop\hhhh\Prj\IVI\video'
stopFlag = False
pauseFlag = False

def read_video_dir():
    dirFiles = ""
    for f in listdir(path):
        if isfile(join(path, f)):
            if dirFiles == "":
                dirFiles += f
            else:
                dirFiles += "," + f
    return dirFiles

def startVideo(filename):
    global stopFlag, pauseFlag
    video_path = (path + '/' + filename)
    video=cv2.VideoCapture(video_path)
    frame = video.read()
    
    while True:
        if not pauseFlag: 
            grabbed, frame=video.read()
            if not grabbed:
                print("End of video")
                break
        print(pauseFlag)
        if (cv2.waitKey(30) & 0xFF == ord("q")) or stopFlag == True:
            stopFlag = False
            break
        cv2.imshow("Video", frame)
    
    video.release()
    cv2.destroyAllWindows()

def playVideo():
    global pauseFlag
    pauseFlag = False

def pauseVideo():
    global pauseFlag
    pauseFlag = True

def stopVideo():
    global stopFlag
    stopFlag = True
