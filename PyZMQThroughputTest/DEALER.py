import zmq
import time
from datetime import datetime, timedelta

def size(msg):
    length=len(msg)
    if(length >= 1024):
        return "{0}Kilobyte(s)".format(length/1024.0)
    else:
        return "{0}Byte(s)".format(length)

def getRepliesInBatch(n, msg):
    start=datetime.now()
    for j in range(1, n+1):
        sock.send(msg)
    
    for j in range(1, n+1):
        sock.recv()
    end=datetime.now()
    elapsed=(end-start).total_seconds()
    
    print("It took {0} seconds to process {1} messages of size {2} in a batch".format(elapsed, n, size(msg)))
    print(volume/elapsed, " messages/second\n")

volume=150000

ctx=zmq.Context()
sock=ctx.socket(zmq.DEALER)
sock.hwm=volume
sock.setsockopt(zmq.LINGER, 200)
sock.connect("tcp://127.0.0.1:5556")

TenBytes=b"1234567890"
KiloBytes=b"1"
for i in range(1, 11):
    KiloBytes=KiloBytes+KiloBytes

TenKiloBytes=KiloBytes
for i in range(1, 10):
    TenKiloBytes+=KiloBytes
    
getRepliesInBatch(volume, TenBytes)
getRepliesInBatch(volume, KiloBytes)
getRepliesInBatch(volume, TenKiloBytes)