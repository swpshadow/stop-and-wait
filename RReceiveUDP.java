

public class RReceiveUDP implements edu.utulsa.unet.RReceiveUDPI{

  private int mode = 0; //0 is stop and wait, 1 is sliding window
  private long modeParameter = 256;
  private String fname = "localhost";
  private int port = 12987;
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

  public void setFileName(String fname){
    this.fname = fname;
  }

  public String getFileName(){
    return fname;
  }

  public boolean setLocalPort(int port){
    if(port > 0){
      this.port = port;
      return true;
    }
    return false;
  }

  public int getLocalPort(){
    return port;
  }

  public boolean receiveFile(){

  }

}
