
import java.net.InetSocketAddress;
import java.net.DatagramPacket;
import edu.utulsa.unet.UDPSocket;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Arrays;

public class RSendUDP implements edu.utulsa.unet.RSendUDPI {

  private int mode = 0; //0 is stop and wait, 1 is sliding window
  private long modeParameter = 256;
  private InetSocketAddress receiver;
  private long timeout = 1000;
  private String fname;
  private int port = 12987;
  private InetAddress sender;

  public RSendUDP(){
    try{
      sender = InetAddress.getByName("localhost");
    }
    catch(UnknownHostException e)
    {
      System.out.printf("error: %s", e);
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**change the algorithm used
  * 1: change to sliding window alg
  * 0: change to stop-and-wait alg
  */
  public boolean setMode(int mode){
    if (mode != 1 && mode != 0){
      return false;
    }
    this.mode = mode;
    return true;
  }
  /**retuns the mode the class is currently using
  */
  public int getMode(){
    return mode;
  }

  public boolean setModeParameter(long n){
    if(mode == 1){
      modeParameter = n;
      return true;
    }
    return false;
  }

  public long getModeParameter(){
    return modeParameter;
  }

  public void setFilename(String fname){
    this.fname = fname;
  }

  public String getFilename(){
    return fname;
  }

  public boolean setTimeout(long timeout){
    if(timeout > 0){
      this.timeout = timeout;
      return true;
    }
    return false;
  }
  public long getTimeout(){
    return timeout;
  }

  public boolean setLocalPort(int port){
    if (port > 0){
      this.port = port;
      return true;
    }
    return false;
  }

  public int getLocalPort(){
    return port;
  }

  public boolean setReceiver(InetSocketAddress receiver){
    if(receiver != null){
      this.receiver = receiver;
      return true;
    }
    return false;
  }
  public InetSocketAddress getReceiver(){
    return receiver;
  }
  public boolean sendFile(){
	
    try{
      long t = System.currentTimeMillis();
      File f = new File(fname);
      System.out.printf("Sending %s from %s:%d to %s with %d bytes\n", fname, sender.getHostAddress(), port, receiver.toString(),  fname.length());
      if(mode == 0){
    	  //header: msg num, total length,  
        System.out.println("using stop-and-wait");
        
        byte msg[] = Files.readAllBytes(f.toPath());
        UDPSocket sock = new UDPSocket(port);
        sock.setSoTimeout((int) timeout);
        byte count = 0;
	    int buffSize = sock.getSendBufferSize();
	    for( int i = 0; i <= msg.length/buffSize; i ++)
	    {
	    	int idx = (i+1)*(buffSize - 6);
	    	if (idx >= msg.length) {
	    		idx = msg.length - 1;
	    	}
	    	count += 1;
		    byte pack[] = makePacket(Arrays.copyOfRange(msg, i*(buffSize - 6), idx ), count, (byte) (msg.length/buffSize + 1));
		    int attempts = 0;
		    boolean notReceived = true;
		    while( attempts <= 5 && notReceived )
		    {
		    	attempts += 1;
			    try {
			    	sock.send(new DatagramPacket(pack, pack.length, receiver));
			    	System.out.printf("Message %d sent with %d bytes of actual data\n", 
			    			count, idx - i*(buffSize - 6));
			    	byte[] buf = new byte[1];
			    	DatagramPacket p = new DatagramPacket(buf, buf.length);
			    	sock.receive(p);
			    	
			    	if ((int) (p.getData()[0]) == count)
			    	{
			    		notReceived = false;
				    	System.out.printf("message %d acknowledged\n", p.getData()[0]);
			    	}
			    	else
			    	{
			    		System.out.printf("no ACK received\n");
			    	}
			    	
			    }
			    catch(Exception e)
			    {
//			    	if ((byte)count == (byte) (msg.length/buffSize + 1)) {
//			    		System.out.printf("message %d acknowledged\n", count);
//			    		break;
//			    	}
			    	System.out.println("timeout has occured");
			    	if (attempts < 5) {
			    		System.out.printf("attempting to resend message %d\nThis is the %d attempt\n", count, attempts);
			    	}
			    }
		    }
		    if (attempts >= 5)
		    {
		    	System.out.printf("Could not send file after %d attempts\n", attempts);
		    	return false;
		    }
	    }
	    System.out.printf("Successfully transferred %s (%d bytes) in %.2f seconds\n", 
	    		fname, msg.length - 1,(System.currentTimeMillis() - t)/1000.0 );
      }
      else{
        System.out.println("using sliding window");
        System.out.println("This has not been implemented");
        return false;
      }
    }
    // catch(FileNotFoundException e){
    //   System.out.printf("the file %s could not be found: ", fname, e);
    //   System.exit(1);
    // }
    catch(Exception e){
      System.out.printf("there was an error: %s", e);
      e.printStackTrace();
      return false;
    }
    return true;
  }
  private byte[] makePacket(byte[] msg, byte msgNum, byte numMsgs)
  {
	  //msg num in 1 bytes
	  //num msgs total in 1 bytes
	  //length in 2 bytes
	  //port num in 2 bytes
	  byte message [] = new byte[msg.length + 6];
	  int count = 6;
	  for(byte b : msg) {
		  message[count++] = b;
	  }
	  message[0] = msgNum;
	  message[1] = numMsgs;
	  byte b1 = (byte) (msg.length >> 8);
	  byte b2 = (byte) msg.length;
	  message[2] = b1;
	  message[3] = b2;
	  b1 = (byte) ((port >> 8) & 0xff);
	  b2 = (byte) (port& 0xff);
	  message[4] = b1;
	  message[5] = b2;
	  return message;
  }
}
