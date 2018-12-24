from pyftpdlib import servers
from pyftpdlib.handlers import FTPHandler

def start_ftp_server(port):
    address = ("127.0.0.1", port)
    server = servers.FTPServer(address, FTPHandler)
    server.serve_forever()