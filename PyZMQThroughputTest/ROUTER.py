import zmq

volume=500000

ctx=zmq.Context()
sock=ctx.socket(zmq.ROUTER)
sock.hwm=volume
sock.setsockopt(zmq.LINGER, 200)
sock.bind("tcp://127.0.0.1:5556")

print("waiting for messages...")
while 1:
    msg=sock.recv_multipart()
    sock.send_multipart(msg)
