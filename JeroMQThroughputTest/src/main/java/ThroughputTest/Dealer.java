package ThroughputTest;

import java.util.Calendar;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Dealer implements AutoCloseable {
    private ZContext ctx;
    private ZMQ.Socket dealer;
    private String addr="tcp://127.0.0.1:5557";
    private static int volume=600000;
    
    public Dealer() {
        ctx=new ZContext();
        dealer=ctx.createSocket(ZMQ.DEALER);
        dealer.setHWM(volume*2);
        dealer.connect(addr);
    }
    
    public static void main(String[] args) {
        byte[] TenBytes=new byte[10];
        byte[] KiloBytes=new byte[1024];
        byte[] TenKiloBytes=new byte[10240];
        
        for(int i=0; i<10; ++i)
            TenBytes[i]=0;
        for(int i=0; i<1024; ++i)
            KiloBytes[i]=0;
        for(int i=0; i<10240; ++i)
            TenKiloBytes[i]=0;
        
        Dealer dealer=new Dealer();
        
        dealer.getRepliesInBatch(volume, TenBytes);
        dealer.getRepliesInBatch(volume, KiloBytes);
        dealer.getRepliesInBatch(volume, TenKiloBytes);
        
        dealer.close();
    }
    
    public void getRepliesInBatch(int n, byte[] msg) {
        long start=Calendar.getInstance().getTimeInMillis();
        for(int i=0; i<n; ++i)
            dealer.send(msg);
        for(int i=0; i<n; ++i)
            dealer.recv();
        long elapsed=Calendar.getInstance().getTimeInMillis()-start;
        double elapsedSec=elapsed/1000.0;
        
        System.out.format("It took %.2f seconds to process %d messages of size %s in a batch\n",
                elapsedSec, n, size(msg));
        System.out.format("%.2f messages/second\n\n", n/elapsedSec);
    }
    
    private String size(byte[] msg) {
        int length=msg.length;
        if(length >= 1024)
            return String.format("%.1f KiloByte(s)", length/1024.0);
        
        return String.format("%d Byte(s)", length);
    }

    public void close() {
        ctx.close();
    }
}
