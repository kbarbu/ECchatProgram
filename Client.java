import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {

public static void main(String[] args)throws IOException {
    int Port = 50003;
String IP = JOptionPane.showInputDialog("Input Your IP Server : ");
Socket sock=new Socket("10.88.192.231", 50003);
DataInputStream in= new DataInputStream(sock.getInputStream());
System.out.println(in.readUTF());
DataOutputStream out =new DataOutputStream(sock.getOutputStream());
out.writeUTF("waiting for connection");
sock.close();
}}
