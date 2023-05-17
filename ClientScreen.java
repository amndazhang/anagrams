import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.util.Timer;

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
import java.util.*;

public class ClientScreen extends JPanel implements ActionListener, KeyListener {

    private JTextField textInput;
    // private String chatMessage;
    private String hostName = "localhost";
    private String name = "";
    private boolean loggedIn = false;

    private String string; // 6 letters
    private String typedWord; // current typing word
    // private String currLetterBank = "";
    private DLList<Character> availableLetters;
    private DLList<String> wordBankList;

    private DLList<String> addedWords; // correct guesses
    private int score;
    private DLList<String> finalScores;

    private double startTime;
    private boolean gameStarted, gameEnded;

    // private String chat = "";

    // private ArrayList<Image> images = new ArrayList<>();
    private JButton startButton;
    // private boolean drawing = false;

    // private JTextArea textArea = new JTextArea();

    // private PrintWriter out;
    private ObjectOutputStream outObj;

    public ClientScreen() throws FileNotFoundException {
        wordBankList = new DLList<String>();
        addedWords = new DLList<String>();
        score = 0;
        gameStarted = false;
        gameEnded = false;
        finalScores = new DLList<>();
        try {
            Scanner scan = new Scanner(new FileReader("wordbanks/tsignr.txt"));
            // reads one line at a time
            while (scan.hasNextLine()) {
                String word = scan.nextLine();
                wordBankList.add(word);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        textInput = new JTextField();
        textInput.setBounds(100, 300, 200, 30);
        this.add(textInput);
        textInput.addActionListener(this);
        textInput.setText("NICKNAME");
        
        startButton = new JButton();
        startButton.setBounds(20, 500, 200, 30);
        startButton.setText("START");
        this.add(startButton);
        startButton.addActionListener(this);
        startButton.setVisible(false);

        typedWord = "";
        availableLetters = new DLList<>();

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

        if (loggedIn && !gameStarted && !gameEnded) { // start screen
            g.setColor(new Color(166, 127, 235));
            g.fillRect(0, 0, 600, 600);

            startButton.setVisible(true);
        } else if (loggedIn && gameStarted && !gameEnded) { // play screen
            startButton.setVisible(false);

            g.setColor(new Color(166, 127, 235));
            g.fillRect(0, 0, 600, 600);

            drawTypedLetters(g);
            drawAvailableLetters(g);

            g.drawString("Player: " + name, 50, 50);
            g.drawString("Score: " + score, 50, 70);
            g.drawString("Time remaining: " + (60000.0 - (System.currentTimeMillis() - startTime))/1000.0, 50, 90);

            g.setColor(Color.white);
            g.fillRect(400, 50, 150, 325);
            int wordX = 410;
            int wordY = 70;
            g.setColor(Color.black);
            for (int i = 0; i < addedWords.size(); i++) {
                g.drawString(addedWords.get(i), wordX, wordY);
                wordY += 20;
            }

            if (System.currentTimeMillis() - startTime >= 1000*10){ // TIME
                gameEnded = true;

                try {
                    outObj.reset();
                    outObj.writeObject(name + " " + score);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        } else if (loggedIn && gameStarted && gameEnded) { // end game
            g.setColor(Color.white);
            g.fillRect(0, 0, 600, 600);
            g.setColor(Color.black);
            g.drawString("Player: " + name, 50, 50);
            g.drawString("END", 100, 100);


            sort(finalScores);
            for(int i = 0; i < finalScores.size(); i++){
                String name = finalScores.get(i).split("\\s")[0];
                int score = Integer.valueOf(finalScores.get(i).split("\\s")[1]);
                
                g.drawString(name + ": " + score, 100, 300+i*20);
            
            }
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
                    if (o instanceof String) {

                        String s = (String) o;
                        // System.out.println("s1: " + s);
                        if (s.split(" ")[0].equals("word")) {
                            string = s.split(" ")[1];

                            char[] arr = string.toCharArray();
                            for (char each : arr) {
                                availableLetters.add(each);
                            }

                        } else {
                            //.out.println("s(in else): " + s);
                            //nt score = Integer.valueOf(s.split("\\s")[1]);
                            // int scoe = (int)(s.split(" ")[1]);
                            //String name = s.split("\\s")[0];
                            //System.out.println("score + name: " + score + " " + name);
                            finalScores.add(s);
                        }
                    }
                    if(o instanceof Integer){
                        Integer i = (Integer) o;
                        System.out.println("Other User's score: " + i);
                    }
                    
                    repaint();
                } catch (Exception e) {

                }
            }

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection");
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
        }
        if (e.getSource() == startButton) {
            gameStarted = true;
            startTime = System.currentTimeMillis();
        }
        repaint();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Character c = e.getKeyChar();
        int code = e.getKeyCode();

        for (Character each : string.toCharArray()) {
            if (each == c) {
                type(c);
            }
        }

        if (code == 8) {
            backspace();
        }

        if (code == 10) {
            enter();
        }

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
        for (int i = 0; i < 6; i++) {
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
        for (int i = 0; i < availableLetters.size(); i++) {
            if (c == availableLetters.get(i)) {
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
            Character removed = typedWord.toCharArray()[typedWord.length() - 1];
            typedWord = typedWord.substring(0, typedWord.length() - 1);

            int index = -1;
            for (int i = 0; i < 6; i++) {
                if (removed == string.toCharArray()[i]) {
                    index = i;
                }
            }
            availableLetters.set(index, removed);
        }
    }

    public void enter() {
        boolean correct = false;
        for (int i = 0; i < wordBankList.size(); i++) {
            if (wordBankList.get(i).equals(typedWord) && !addedWords.contains(typedWord)) {
                addedWords.add(typedWord);
                addToScore(typedWord);
                correct = true;
                break;
            }
        }
        if (correct) {
            correctSound();
        } else {
            wrongSound();
        }
        typedWord = "";
        availableLetters = new DLList<>();
        char[] arr = string.toCharArray();
        for (char each : arr) {
            availableLetters.add(each);
        }
    }

    public void addToScore(String word) {
        int len = word.length();
        if (len == 3) {
            score += 100;
        } else if (len == 4) {
            score += 400;
        } else if (len == 5) {
            score += 1200;
        } else if (len == 6) {
            score += 2000;
        }
    }

    public void correctSound() {

        try {
            URL url = this.getClass().getClassLoader().getResource("sounds/correct.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    public void wrongSound() {

        try {
            URL url = this.getClass().getClassLoader().getResource("sounds/wrong.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    public void sort(DLList<String> list){
        for (int i=0; i<list.size()-1; i++){
            for (int j=0; j<list.size()-i-1; j++){
                int score1 = Integer.valueOf(list.get(j).split("\\s")[1]);
                int score2 = Integer.valueOf(list.get(j+1).split("\\s")[1]);

                if (score1 < score2){
                    String tmp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, tmp);
                }
            }
        }
    }

}
