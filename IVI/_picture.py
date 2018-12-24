from os import listdir
from os.path import isfile, join
import json

path = r'C:\Users\Hansol\Desktop\hhhh\Prj\IVI\picture'

def read_picture_dir():
    dirFiles = ""
    for f in listdir(path):
        if isfile(join(path, f)):
            if dirFiles == "":
                dirFiles += f
            else:
                dirFiles += "," + f
    return dirFiles

def show_picture():
    pass

def hide_picture():
    pass

def download_picture():
    pass

def delete_picture():
    pass