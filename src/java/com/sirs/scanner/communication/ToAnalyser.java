package com.sirs.scanner.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import com.sirs.scanner.Container;

public class ToAnalyser extends Sender {
    private Socket clientSocket = null;

    public ToAnalyser() throws UnknownHostException, IOException {
        this("localhost", 12000);
    }

    public ToAnalyser(String host, int port) throws UnknownHostException, IOException {
        this.clientSocket = new Socket(host, port);
    }

    @Override
    public boolean send(List<Container> c) {
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(this.clientSocket.getOutputStream());
            objectOutput.writeObject(c);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

	@Override
	public void close() {
		if(clientSocket != null){
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
