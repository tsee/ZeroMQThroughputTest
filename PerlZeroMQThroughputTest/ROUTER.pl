use strict;
use warnings;
use ZMQ::LibZMQ2;
use ZMQ::Constants ':all';

my $ctx = zmq_init(1);
my $sock = zmq_socket($ctx, ZMQ_ROUTER);
zmq_setsockopt($sock, ZMQ_HWM, 1e9);
zmq_setsockopt($sock, ZMQ_LINGER, 200);
zmq_bind($sock, "tcp://*:5555");

print "waiting for messages...\n";
my ($framing, $msg);
while (1) {
    $framing = zmq_recv($sock);
    $msg = zmq_recv($sock);
    #warn zmq_msg_data($msg);
    zmq_send($sock, $framing, ZMQ_SNDMORE);
    zmq_send($sock, $msg);
}
