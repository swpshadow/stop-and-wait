
public class recDriver {

	public static void main(String[] args) {
		RReceiveUDP rec = new RReceiveUDP();
		rec.setFilename("less_important.txt");
		rec.setLocalPort(32456);
		rec.receiveFile();
	}

}
