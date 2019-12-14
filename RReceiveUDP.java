import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import edu.utulsa.unet.UDPSocket;

public class RReceiveUDP implements edu.utulsa.unet.RReceiveUDPI{

  private int mode = 0; //0 is stop and wait, 1 is sliding window
  private long modeParameter = 256;
  private String fname = "important.txt";
  private int port = 12986;
 
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
	  if(mode == 0){
    	  //header: msg num, total length,  
		  long t = System.currentTimeMillis();
		  try 
		  {
			  byte [] ack = new byte[1];
			  UDPSocket sock = new UDPSocket(port);
			  byte[] buff = new byte[1500];
			  DatagramPacket packet = new DatagramPacket(buff, buff.length);
			  byte[] data = new byte[0];
			  byte msgNum = 1;
			  while (true) {
				  sock.receive(packet);
				  byte[] buffer = packet.getData();
				  int clientPort = (((buffer[4] & 0xFF) << 8) | (buffer[5] & 0xFF)) ;
				  ack[0] = buffer[0];
				  
				  System.out.printf("Message %d received with %d bytes of actual data\n",
						  buffer[0], (((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF))) ;
				  
				  InetAddress client = packet.getAddress();
				  sock.send(new DatagramPacket(ack, ack.length, client, clientPort));
				  System.out.printf("sending ACK %d\n%d\n", buffer[0], msgNum);
				  if(buffer[0] == msgNum)
				  {
					  int len = (((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF));
					  byte data2[] = new byte [data.length + len];
					  int i = 0;
					  for (byte b: data) {
						  data2[i++] = b;
					  }
					  for (int j = 0; j < len; j++)
					  {
						  data2[i++] = buffer[j + 6];
					  }
					  i = 0;
					  data = new byte[data2.length];
					  for (byte b: data2) {
						  data[i++] = b;
					  }
							  
					  msgNum +=1;
				  }
				  if (buffer[0] == buffer[1])
				  {
					  //to ensure the sender gets the ACK we will send it a lot
					  for(int k = 0; k < 10; k++)
					  { 
						  sock.send(new DatagramPacket(ack, ack.length, client, clientPort));
					  }
					  break;
				  }
			  }
			  try (FileOutputStream f = new FileOutputStream(fname)) 
			  {
				   f.write(data);
			  }
			  catch(Exception e)
			  {
				 e.printStackTrace();
				 return false;
			  }
			  System.out.printf("Successfully received %s (%d bytes) in %.2f seconds\n", 
					  fname, data.length, (System.currentTimeMillis() - t)/1000.0 );
			  
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
	  }
	  else
	  {
		  return false;
	  }
	  return true;

  }


}
