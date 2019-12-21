
public class recDriver {

	public static void main(String[] args) {
		RReceiveUDP rec = new RReceiveUDP();
		rec.setFilename("new.txt");
		rec.setLocalPort(4933);
		rec.receiveFile();
	}

}
