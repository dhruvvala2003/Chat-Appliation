import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

class Server extends JFrame{


    ServerSocket server;
    Socket socket;
    PrintWriter out;
    BufferedReader br;

    //compo for gui

    JLabel heading=new JLabel("Server Area");
    JTextArea messageArea=new JTextArea();
    JTextField messageInput=new JTextField();
    Font font=new Font("Roboto",Font.PLAIN,20);



    public Server()
    {

        try{
            server=new ServerSocket(777);
            System.out.println("server is started...");
            System.out.println("waiting...");
    
            socket=server.accept();

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());


            createGui();
            handleEvent();


            startReading();
            // startWriting();

            
        }
        catch(Exception e)
        {
            System.out.println(e);

        }

    }


     void handleEvent()
    {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
               }

            @Override
            public void keyReleased(KeyEvent e) {
              
                //System.out.println("key released:"+ e.getKeyCode());
                // enter keycode is 10
                
                if(e.getKeyCode()==10)
                {
                    System.out.println("key released:"+ e.getKeyCode());
              
                    String contentToSend=messageInput.getText();
                    out.println(contentToSend);
                    messageArea.append("Me: "+contentToSend+"\n");
           
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
            
            }
            

        });
    }

    void createGui()
    {
        //gui
        //this->window
            this.setTitle("Server Messager[END]");
            this.setSize(600,600);
            this.setLocationRelativeTo(null);// to do window center 
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);

            // component coding...
            heading.setFont(font);
            // heading.setIcon(new ImageIcon("messageImg.png"));
            heading.setHorizontalTextPosition(SwingConstants.CENTER);
            heading.setVerticalTextPosition(SwingConstants.BOTTOM);

            heading.setHorizontalAlignment(SwingConstants.CENTER);
            heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            messageArea.setEditable(false);

            messageArea.setFont(font);
            messageInput.setFont(font);
            messageInput.setHorizontalAlignment(SwingConstants.CENTER);

            this.setLayout(new BorderLayout());  // devideong layout in 5 . north,center,south & ( east,west -> not visible default)

            // adding component to frame

            this.add(heading,BorderLayout.NORTH);
            JScrollPane jScrollPane=new JScrollPane(messageArea);
            this.add(jScrollPane,BorderLayout.CENTER);
            this.add(messageInput,BorderLayout.SOUTH);

    }   


    void startReading()
    {
        Runnable r1=()->{

            System.out.println("reading start");

            try{
                
                while (true) {
                    
                    // System.out.println("server: "+ br.readLine());
                    String msg=br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Client stop execution");
                        JOptionPane.showMessageDialog(this, "Server Terminated Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;

                    }

           

                    messageArea.append("Client: "+ msg+"\n");
          
                }

                // System.out.println("connetion closes");
                  }
            catch(Exception e)
            {
                System.out.println("connection close  :" + e);

            }

        };


        new Thread(r1).start();
    }



    void startWriting()
    {
        Runnable r2=()->{
            
            try{
                
            while (true && !socket.isClosed()) {
                
                BufferedReader br1=new BufferedReader(new InputStreamReader(System.in)); // reading from console witch client type

                String msg=br1.readLine();

                out.println(msg);
                out.flush();

                if(msg.equals("exit"))
                {
                    socket.close();
                }
               
            }

        

        }
        catch(Exception e)
        {
            System.out.println(e);

        }

    };

    new Thread(r2).start();


    }


    public static void main(String args[])
    {
        new Server();

    }
}