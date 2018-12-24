from pyftpdlib.authorizers import DummyAuthorizer
from pyftpdlib.handlers import FTPHandler
from pyftpdlib.servers import FTPServer
import os

authorizer = DummyAuthorizer()
path = os.getcwd()
authorizer.add_user('user', "12345", '.', perm='elradfmwMT')

handler = FTPHandler
handler.passive_ports = range(60000, 65535)
handler.authorizer = authorizer

server = FTPServer(("192.168.0.4", 21), handler)
server.serve_forever()