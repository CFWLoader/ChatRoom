package evan.client.utils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created by cfwloader on 3/16/15.
 */
public class ChatClient extends Frame {

    private TextField textField = new TextField(50);

    private TextArea textArea = new TextArea(40, 80);

    private Socket socket;

    private DataOutputStream output;

    private DataInputStream input;

    private MassageListenThread massageListener;

    private Thread massageListenThread;

    public static void main(String[] args){
        new ChatClient().launchFrame();
    }

    public void launchFrame(){

        this.setLocation(600, 150);
        this.setSize(960, 540);
        this.setTitle("Chat Room");

        this.add(textArea, BorderLayout.NORTH);
        this.add(textField, BorderLayout.SOUTH);
        this.pack();

        //System.out.println("Frame launched");

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //System.out.println("Closing event triggered.");
                try {
                    massageListener.setStopRequest(true);
                    input.close();
                    output.close();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    File exceptionFile = new File("errorlog.txt");
                    try {
                        FileOutputStream outputStream = new FileOutputStream(exceptionFile);
                        PrintWriter printWriter = new PrintWriter(outputStream);
                        for (int i = 0; i < e1.getStackTrace().length; ++i) {
                            printWriter.println(e1.getStackTrace()[i]);
                        }
                    } catch (FileNotFoundException e2) {
                        e2.printStackTrace();
                        System.out.println("Fatal error.");
                        System.exit(-4);
                    }
                }
                System.exit(0);
            }
        });

        //System.out.println("Event added.");

        textField.addActionListener(new TextFieldListener());

        this.connect();

        massageListener = new MassageListenThread(input, textArea);
        massageListenThread = new Thread(massageListener);
        massageListenThread.start();

        this.setVisible(true);
    }

    private class TextFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String str = textField.getText().trim();
            //textArea.setText(str);
            /*
            textArea.append(String.valueOf(new Date(System.currentTimeMillis())) + "\n");
            textArea.append("    " + str + "\n\n");
            */
            try {
                output.writeUTF(str);
                output.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            textField.setText("");
        }
    }

    public void connect(){
        try {
            socket = new Socket("127.0.0.1", 4991);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private class MassageListenThread implements Runnable{

        private DataInputStream input;

        private TextArea textArea;

        private boolean stopRequest;

        private MassageListenThread(DataInputStream input, TextArea textArea) {
            this.input = input;
            this.textArea = textArea;
            stopRequest = false;
        }

        @Override
        public void run() {
            String words;
            while(true && !stopRequest){
                try {
                    words = input.readUTF();                       //The old reason that stop closing actions is used BufferedReader readline method.
                    if(words == null)break;                         //It will block the thread even the input stream is closed. I have used DataInputStream to replace it.
                    textArea.append(String.valueOf(new Date(System.currentTimeMillis())) + "\n");
                    textArea.append("    " + words + "\n\n");
                } catch (EOFException eofException){
                    input = null;
                    textArea = null;
                } catch (IOException e) {
                    //e.printStackTrace();
                    input = null;
                    textArea = null;
                }
            }

            System.out.println("Listen thread stopped.");
            input = null;
            textArea = null;
        }

        public void setStopRequest(boolean stopRequest) {
            this.stopRequest = stopRequest;
        }
    }
}