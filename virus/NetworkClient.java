import java.io.*;
import java.net.*;

public class NetworkClient {
	private static int SLEEP_TIME = 50;
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Missing port!");
			System.exit(-1);
		}

		int port = Integer.parseInt(args[0]);

		FileInputStream in = new FileInputStream("/dev/urandom");
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] buffer = new byte[512];
		while (true) {
			in.read(buffer);
			InetAddress IPAddress = InetAddress.getByName("localhost");
			DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, IPAddress, port);
			clientSocket.send(sendPacket);
			Thread.sleep(SLEEP_TIME);
		}
	}
}
