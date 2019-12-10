package Proj2;

import java.net.InetSocketAddress;
import java.net.DatagramPacket;
import edu.utulsa.unet.UDPSocket;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
      sender = InetAddress.getLocalHost();
    }
    catch(UnknownHostException e)
    {
      System.out.printf("error: %s", e);
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
      File f = new File(fname);
      System.out.printf("Sending %s from %s:%d to %s with %d bytes\n", fname, sender.getHostAddress(), port, receiver.toString(),  fname.length());
      if(mode == 0){
        System.out.println("using stop-and-wait");
      }
      else{
        System.out.println("using sliding window");
      }
    }
    // catch(FileNotFoundException e){
    //   System.out.printf("the file %s could not be found: ", fname, e);
    //   System.exit(1);
    // }
    catch(Exception e){
      System.out.printf("there was an error: %s", e);
      System.exit(1);
    }
    return true;
  }
}
