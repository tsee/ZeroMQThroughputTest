import zmq

volume=150000

ctx=zmq.Context()
sock=ctx.socket(zmq.ROUTER)
sock.hwm=volume*3
sock.setsockopt(zmq.LINGER, 200)
sock.bind("tcp://127.0.0.1:5555")

print("waiting for messages...")
while 1:
    msg=sock.recv_multipart()
    sock.send_multipart(msg)
