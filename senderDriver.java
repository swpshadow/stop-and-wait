import java.net.InetSocketAddress;

public class senderDriver {

	public static void main(String[] args) {
		RSendUDP sender = new RSendUDP();
		sender.setTimeout(6000);
		sender.setFilename("important.txt");
		sender.setLocalPort(2987);
		sender.setReceiver(new InetSocketAddress("localhost", 4933));
		sender.sendFile();

	}

}
