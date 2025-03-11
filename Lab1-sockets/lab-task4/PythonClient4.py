import socket;

serverIP = "127.0.0.1"
serverPort = 9009
msg = "Python Client"

print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(bytes(msg, 'cp1250'), (serverIP, serverPort))

received = False

while not received:
    data, addr = client.recvfrom(1024)
    print(str(data, 'cp1250'))
    received = True