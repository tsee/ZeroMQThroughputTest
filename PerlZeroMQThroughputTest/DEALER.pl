use strict;
use warnings;
use ZMQ::LibZMQ2;
use ZMQ::Constants qw(:all);
use Time::HiRes qw(time);

my $volume=150000;

my $ctx = zmq_init(1);
my $sock = zmq_socket($ctx, ZMQ_DEALER);
zmq_setsockopt($sock, ZMQ_HWM, $volume*3);
zmq_setsockopt($sock, ZMQ_LINGER, 200);
zmq_connect($sock, "tcp://127.0.0.1:5555");

my $TenBytes = "x" x 10;
my $KiloBytes = "x" x 1024;
my $TenKiloBytes = "x" x (10*1024);

get_replies_in_batch($volume, $TenBytes);
get_replies_in_batch($volume, $KiloBytes);
get_replies_in_batch($volume, $TenKiloBytes);

sub size {
  my $msg = shift;
  if (length($msg) > 1024) {
    return sprintf("%uKilobyte(s)", length($msg));
  } else {
    return sprintf("%uByte(s)", length($msg));
  }
}

sub get_replies_in_batch {
  my ($n, $msg) = @_;

  my $before = time();
  my $o = zmq_msg_init_data($msg);
  zmq_send($sock, zmq_msg_init_data($msg)) for 1..$n;
  $o = zmq_recv($sock) for 1..$n;
  my $d = time()-$before;

  my $msgtext = zmq_msg_data($o);
  printf "It took %.2f seconds to process %i messages of size %s in a batch.\n", $d, $n, size($msgtext);
  print( ($volume/$d) . " messages/second\n" );
}

