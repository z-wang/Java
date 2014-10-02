package edu.stan.cs;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class SWS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			SimpleWebServer sws = new SimpleWebServer();
			sws.run();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}

class SimpleWebServer {
	private static final int PORT = 8088;
	private static ServerSocket dServerSocket;
	
	public SimpleWebServer() throws Exception {
		dServerSocket = new ServerSocket(PORT);
	}
	
	public void run() throws Exception {
		while (true) {
			Socket s = dServerSocket.accept();
			
			processRequest(s);
		}
	}
	
	public void processRequest (Socket s) throws Exception {
		BufferedReader br = new BufferedReader (new InputStreamReader (s.getInputStream()));
		OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
		String request = br.readLine();
		String command = null;
		String pathname = null;
		
		try {
			StringTokenizer st = new StringTokenizer(request, " ");
			command = st.nextToken();
			pathname = st.nextToken();
		} catch (Exception e) {
			osw.write("HTTP/1.0 400 BAD REQUEST\n\n");
			osw.close();
			return;
		}
		if (command.equals("GET")) {
			serveFile(osw, pathname);
		} else {
			osw.write("HTTP/1.0 501 NOT IMPLEMENTED\n\n");

		}
		osw.close();	
	}
	
	public void serveFile(OutputStreamWriter osw, String pathname) throws Exception {
		FileReader fr = null;
		int c = -1;
		StringBuffer sb = new StringBuffer();
		
		if(pathname.charAt(0)=='/'){
			pathname = pathname.substring(1);
		}
		
		if (pathname.equals("")){
			pathname = "index.html";
		}
		
		try {
			fr = new FileReader(pathname);
			c = fr.read();
		} catch(Exception e) {
			osw.write("HTTP/1.0 404 NOT FOUND\n\n");
			return;
		}
		
		osw.write("HTTP/1.0 200 OK\n\n");
		while(c!=-1){
			sb.append((char)c);
			c = fr.read();
		}
		osw.write(sb.toString());
	}
}
