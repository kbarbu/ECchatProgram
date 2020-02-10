import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
   // import all the class that you will need for functionality

   // extends jframe to develop gui's in java
public class Server extends JFrame
{

private JTextField userInput; // 
private JTextArea theChatWindow; //
private ObjectOutputStream output; // stream data out
private ObjectInputStream input; // stream data in
private ServerSocket server;
private Socket connection; // socket means set up connetion between 2 computers

//Constructor

public Server(){

    super("My Chat Service");
    userInput = new JTextField();
    userInput.setEditable(false); // set this false so you dont send messages when noone is available to chat
    // action event listener to check when the user hits enter for example
    userInput.addActionListener(new ActionListener(){

        public void actionPerformed(ActionEvent event){
            sendMessage(event.getActionCommand()); // string entered in the textfield
            userInput.setText(""); // reset text area to blank again


        }
    }
    );
    // create the chat window
    add(userInput, BorderLayout.NORTH);
    theChatWindow = new JTextArea();
    add(new JScrollPane(theChatWindow));
    setSize(300,150);
    setVisible(true);
}

// run the server after gui created
public void RunServer(){

    try{
        server = new ServerSocket(6789, 100); // 1st number is port number where the application is located on the server, 2nd number is the amount of people aloud to connect
        while(true){

            try{
                waitForConnection(); // wait for a connection between 2 computers 
                setupStreams();  // set up a stream connection between 2 computers to communicate
                whileChatting();  // send message to each other
                // connect with someone and have a conversation
            }catch(EOFException eofException){

                showMessage("\n Server ended Connection");
            }finally{

                closeChat();
            }
        }
    }catch(IOException ioException){



        ioException.printStackTrace();
    }
}


//Wait for a connection then display connection information

private void waitForConnection(){

    showMessage("waiting for someone to connect to chat room....\n");
    try {
        connection = server.accept();
    } catch (IOException ioexception) {

        ioexception.printStackTrace();
    }
    showMessage("Now connected to"+ connection.getInetAddress().getHostName());
}
 // stream function to send and recive data
private void setupStreams() throws IOException{

    output = new ObjectOutputStream(connection.getOutputStream()); // set up pathway to send data out
    output.flush(); // move data away from your machine
    input = new ObjectInputStream(connection.getInputStream()); // set up pathway to allow data in
    showMessage("\n Connection streams are now setup \n");

}

// this code while run during chat conversions
private void whileChatting() throws IOException{

    String message = " You are now connected ";
    sendMessage(message);
    allowTyping(true); // allow user to type when connection
    do{
        // have conversion while the client does not type end
        try{

            message = (String) input.readObject(); // stores input object message in a string variable
            showMessage("\n " +message);
        }catch(ClassNotFoundException classnotfoundException){

            showMessage("\n i dont not what the user has sent");
        }
    }while(!message.equals("CLIENT - END"));// if user types end program stops



}

private void closeChat(){

    showMessage("\n closing connections...\n");
    allowTyping(true);
    try{

        output.close(); // close output stream
        input.close(); // close input stream
        connection.close(); // close the main socket connection

    }catch(IOException ioexception){

        ioexception.printStackTrace();
    }
}

// send message to the client
private void sendMessage(String message){

    try{

        output.writeObject("Server - "+ message);
        output.flush(); // send all data out
        showMessage("\nServer - "+ message);

    }catch(IOException ioexception){

        theChatWindow.append("\n ERROR: Message cant send");
    }


}

// update the chat window (GUI)
private void showMessage(final String text){

    SwingUtilities.invokeLater(

            new Runnable(){

                public void run(){

                    theChatWindow.append(text);


                }
            }

            );


}

// let the user type messages in their chat window

private void allowTyping(final boolean trueOrFalse){

    SwingUtilities.invokeLater(

            new Runnable(){

                public void run(){

                    userInput.setEditable(trueOrFalse);


                }
            }

            );


}

public static void main(String args[])
{
	Frame frm = new Server();
	frm.setVisible(true);
}

}