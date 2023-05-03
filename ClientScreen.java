import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

import java.io.*;
import java.net.*;

public class ClientScreen extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

    private JTextField textInput;
    private String chatMessage;
    private String hostName = "localhost";
    private String name = "";
    private String chat = "";

    // private ArrayList<Image> images = new ArrayList<>();
    // private JButton sendButton;

    private JTextArea textArea = new JTextArea();

    // private PrintWriter out;
    private ObjectOutputStream outObj;

    private String word = "";
    private DLList<String> possibleWords = new DLList<>();
    private String guess = "";

    public ClientScreen() {

        textInput = new JTextField();
        textInput.setBounds(600, 300, 200, 30);
        this.add(textInput);
        textInput.addActionListener(this);

        textArea = new JTextArea();
        textArea.setBounds(600, 400, 200, 200);
        add(textArea);

        addMouseListener(this);
        addMouseMotionListener(this);
        setLayout(null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        for (int i = 0; i < 6; i++) {
            drawCharacter(g, 100+i*50, 100, word.toCharArray()[i]);
        }

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        // System.out.println("f");
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        // if (drawing) {
        // fill(e.getX(), e.getY());
        // changeColor(e.getX(), e.getY());
        // // System.out.println(grid.toString());
        // try {

        // outObj.reset();
        // outObj.writeObject(grid);
        // // System.out.println(grid.toString());
        // } catch (Exception ex) {

        // }
        // }

    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        // if (drawing) {
        // fill(e.getX(), e.getY());

        // // System.out.println(grid.toString());
        // try {

        // outObj.reset();
        // outObj.writeObject(grid);
        // } catch (Exception ex) {

        // }
        // }
    }

    public void poll() throws IOException {

        int portNumber = 1024;
        try {
            try (// ServerSocket clientServer = new ServerSocket(portNumber);
                    Socket serverSocket = new Socket(hostName, portNumber)) {
                outObj = new ObjectOutputStream(serverSocket.getOutputStream());

                // out = new PrintWriter(serverSocket.getOutputStream(), true);

                ObjectInputStream inObj = new ObjectInputStream(serverSocket.getInputStream());

                outObj.writeObject("Connection Successful!");

                // BufferedReader in = new BufferedReader(new
                // InputStreamReader(serverSocket.getInputStream()));

                // Receive connection message
                // Waits for and receives an object
                // readObject() requires a ClassNOtFoundException
                // String serverMessage = (String) in.readObject();
                // System.out.println(serverMessage);

                // Receive server location

                while (true) {
                    System.out.println("entered while loop");
                    try {
                        Object o = inObj.readObject();
                        if (o instanceof MyHashMap) {
                            MyHashMap<String, DLList<String>> tempMap = (MyHashMap<String, DLList<String>>) o;

                            word = tempMap.keySet().toDLList().get(0);
                            possibleWords = tempMap.get(word);
                        }
                        System.out.println(o);
                        System.out.println("word: " + word);
                        System.out.println(possibleWords);

                        if (o instanceof String) {

                            String s = (String) o;
                            textArea.setText(textArea.getText() + "\n" + s);
                        }

                        repaint();
                    } catch (Exception e) {

                    }
                }
                // game.getTable();
            }

            // in.close();

            // outObj.close();

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection");
            // System.exit(1);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == textInput) {
            String sendText = textInput.getText();
            if (name.length() == 0) {
                name = sendText;
            }
            try {
                outObj.reset();
                outObj.writeObject(textInput.getText());
            } catch (Exception err) {

            }
            textInput.setText(null);
            ;
            // if (out != null) {
            // // out.println(sendText);
            // System.out.println(sendText);
            // // chatMessage += sendText + "\n";
            // }
        }
        repaint();

    }

    public void drawBackground(Graphics g) {
        g.setColor(new Color(166, 127, 235));
        // g.fillRect(0, 0, 600, 600);
        g.setColor(Color.white);
        int x = 100;
        for (int i = 0; i < 6; i++) {
            g.fillRect(x + i * 50, 50, 20, 20);
        }
    }

    public void drawCharacter(Graphics g, int x, int y, Character c) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, 20, 20);
        g.drawString("" + c, x, y);
    }

}