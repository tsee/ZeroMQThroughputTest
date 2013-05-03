package ThroughputTest;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;


public class Router {
    private static String addr="tcp://127.0.0.1:5557";
    private static int volume=100000;
    
    public static void main(String[] args) {
        ZContext ctx=new ZContext();
        ZMQ.Socket router=ctx.createSocket(ZMQ.ROUTER);
        router.setRcvHWM(volume*3);
        router.setSndHWM(volume*3);
        router.bind(addr);
        
        System.out.println("Waiting for messages...");
        while(true) {
            ZMsg msg=ZMsg.recvMsg(router);
            msg.send(router);
        }
    }
}