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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

import java.io.*;
import java.net.*;

public class ClientScreen extends JPanel implements ActionListener, KeyListener {

    private JTextField textInput;
    // private String chatMessage;
    private String hostName = "localhost";
    private String name = "";
    private boolean loggedIn = false;

    private String string; // 6 letters
    private String typedWord; // current typing word
    private DLList<Character> availableLetters;

    // private String chat = "";

    // private ArrayList<Image> images = new ArrayList<>();
    // private JButton sendButton;
    // private boolean drawing = false;

    // private JTextArea textArea = new JTextArea();

    // private PrintWriter out;
    private ObjectOutputStream outObj;

    // private int count = 0;

    // private Square[][] grid;

    // private int size, length;
    // private int dimension;
    // private int gridX, gridY;

    // private Color color;
    // private Color[] colors;

    public ClientScreen() {
        // color = Color.RED;
        // colors = new Color[] { Color.BLACK, Color.WHITE, Color.GRAY, Color.RED,
        // Color.ORANGE, Color.YELLOW, Color.GREEN,
        // Color.BLUE, Color.PINK };
        // size = 400;
        // length = 20;
        // dimension = size / length;
        // grid = new Square[length][length];
        // gridX = 50;
        // gridY = 50;

        textInput = new JTextField();
        textInput.setBounds(100, 300, 200, 30);
        this.add(textInput);
        textInput.addActionListener(this);
        textInput.setText("NICKNAME");

        typedWord = "";
        availableLetters = new DLList<>();

        // textArea = new JTextArea();
        // textArea.setBounds(100, 400, 200, 200);
        // add(textArea);

        // for (int i = 0; i < grid.length; i++) {
        // for (int j = 0; j < grid[i].length; j++) {
        // grid[i][j] = new Square(dimension);
        // }
        // }
        // addMouseListener(this);
        // addMouseMotionListener(this);
        addKeyListener(this);        
		setFocusable(true);
        setLayout(null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (loggedIn) {
            // background
            g.setColor(new Color(166, 127, 235));
            g.fillRect(0, 0, 600, 600);

            drawTypedLetters(g);
            drawAvailableLetters(g);

            g.drawString("Player: " + name, 50, 50);
        }

        repaint();

    }

    public void poll() throws IOException {

        int portNumber = 1024;
        try {
            // ServerSocket clientServer = new ServerSocket(portNumber);
            Socket serverSocket = new Socket(hostName, portNumber);
            outObj = new ObjectOutputStream(serverSocket.getOutputStream());

            // out = new PrintWriter(serverSocket.getOutputStream(), true);

            ObjectInputStream inObj = new ObjectInputStream(serverSocket.getInputStream());

            // outObj.writeObject("Connection Successful!");

            // BufferedReader in = new BufferedReader(new
            // InputStreamReader(serverSocket.getInputStream()));

            // Receive connection message
            // Waits for and receives an object
            // readObject() requires a ClassNOtFoundException
            // String serverMessage = (String) in.readObject();
            // System.out.println(serverMessage);

            // Receive server location
            while (true) {
                try {
                    Object o = inObj.readObject();
                    // System.out.println(o);
                    if (o instanceof String) {

                        String s = (String) o;
                        System.out.println(s);
                        System.out.println(s.split(" ")[0]);
                        if (s.split(" ")[0].equals("word")) {
                            // drawing = true;
                            System.out.println("true");
                            System.out.println(s);
                            System.out.println(s.split(" ")[1]);
                            string = s.split(" ")[1];

                            char[] arr = string.toCharArray();
                            for (char each : arr){
                                availableLetters.add(each);
                            }
                        } else {
                            // textArea.setText(textArea.getText() + "\n" + s);
                            // if (s.contains(word)) {
                            // outObj.reset();
                            // outObj.writeObject(s);
                            // }
                            // if (s.charAt(0) != '_') {
                            // serverSocket.close();
                            // }
                        }
                    }
                    // else if (o instanceof Square[][] && !drawing) {

                    // grid = (Square[][]) o;
                    // }
                    repaint();
                } catch (Exception e) {

                }
            }
            // game.getTable();

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
                loggedIn = true;
                textInput.setVisible(false);
            }
            try {
                outObj.reset();
                outObj.writeObject(textInput.getText());
            } catch (Exception err) {

            }
            textInput.setText(null);

            // if (out != null) {
            // // out.println(sendText);
            // System.out.println(sendText);
            // // chatMessage += sendText + "\n";
            // }
        }
        repaint();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Character c = e.getKeyChar();
        int code = e.getKeyCode();
        System.out.println("You have pressed " + c);

        for (Character each : string.toCharArray()){
            if (each == c){
                type(c);
            }
        }

        if (code == 8){
            backspace();
        }

        System.out.println(typedWord);
        System.out.print("avails = " + availableLetters.toString());

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void drawTypedLetters(Graphics g) {
        g.setColor(Color.white);
        for (int i=0; i<6; i++){
            int x = 15 + i * 100;
            g.fillRect(x, 400, 70, 70);
        }
        g.setColor(Color.black);
        for (int i = 0; i < typedWord.length(); i++) {
            int x = 15 + i * 100;
            Character c = typedWord.toCharArray()[i];
            g.drawString("" + c, x + 50, 440);
        }
    }

    public void drawAvailableLetters(Graphics g) {
        for (int i = 0; i < 6; i++) {
            int x = 15 + i * 100;
            Character c = availableLetters.get(i);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, 500, 70, 70);
            g.setColor(Color.black);
            g.drawString("" + c, x + 50, 540);
        }
    }

    // take one of the available letters and put in first available spot
    public void type(Character c) {
        boolean avail = false;
        for (int i=0; i<availableLetters.size(); i++){
            if (c == availableLetters.get(i)){
                avail = true;
                break;
            }
        }
        if (typedWord.length() <= 6 && avail) {
            typedWord += c;
            availableLetters.set(availableLetters.getIndex(c), ' ');
        }
    }

    // remove last char of typed word
    public void backspace() {
        if (typedWord.length() > 0) {
            Character removed = typedWord.toCharArray()[typedWord.length()-1];
            typedWord = typedWord.substring(0, typedWord.length()-1);

            int index = -1;
            for (int i=0; i<6; i++){
                if (removed == string.toCharArray()[i]){
                    index = i;
                }
            }
            availableLetters.set(index, removed);
        }
    }

}
