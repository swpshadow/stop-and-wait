import java.net.InetSocketAddress;

public class senderDriver {

	public static void main(String[] args) {
		RSendUDP sender = new RSendUDP();
		sender.setTimeout(5000);
		sender.setFilename("important.txt");
		sender.setLocalPort(23455);
		sender.setReceiver(new InetSocketAddress("localhost", 32456));
		sender.sendFile();

	}

}
