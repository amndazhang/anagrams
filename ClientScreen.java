import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    private int score, totalScore;
    private DLList<String> finalScores;

    private double startTime;
    private boolean gameStarted, gameEnded, scoreViewed, gameFinalEnded;

    private Color green;

    private JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(textArea);
    private JTextArea textArea2 = new JTextArea();
    private JScrollPane scrollPane2 = new JScrollPane(textArea2);

    private JButton startButton, nextButton, backButton, nextGameButton;

    private ObjectOutputStream outObj;

    private Color purple = new Color(166, 127, 235);
    private Font f1 = new Font("Serif", Font.BOLD, 35);
    private Font f2 = new Font("Serif", Font.PLAIN, 22);
    private Font f3 = new Font("Serif", Font.PLAIN, 15);

    public ClientScreen() throws FileNotFoundException {
        wordBankList = new DLList<String>();
        addedWords = new DLList<String>();
        score = 0;
        gameStarted = false;
        gameEnded = false;
        scoreViewed = false;
        finalScores = new DLList<>();

        textInput = new JTextField();
        textInput.setBounds(100, 300, 200, 30);
        this.add(textInput);
        textInput.addActionListener(this);
        textInput.setText("NICKNAME");
        textInput.setFont(f3);

        startButton = new JButton();
        startButton.setBounds(200, 400, 200, 30);
        startButton.setText("START");
        this.add(startButton);
        startButton.setOpaque(true);
        startButton.setContentAreaFilled(true);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setBackground(purple);
        startButton.addActionListener(this);
        startButton.setVisible(false);
        startButton.setFont(f3);

        nextButton = new JButton();
        nextButton.setBounds(200, 530, 200, 30);
        nextButton.setText("NEXT");
        this.add(nextButton);
        nextButton.setOpaque(true);
        nextButton.setContentAreaFilled(true);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.addActionListener(this);
        nextButton.setVisible(false);
        nextButton.setFont(f3);

        backButton = new JButton();
        backButton.setBounds(200, 510, 200, 30);
        backButton.setText("BACK");
        this.add(backButton);
        backButton.setOpaque(true);
        backButton.setContentAreaFilled(true);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(this);
        backButton.setVisible(false);
        backButton.setFont(f3);

        nextGameButton = new JButton();
        nextGameButton.setBounds(200, 550, 200, 30);
        nextGameButton.setText("NEXT GAME");
        this.add(nextGameButton);
        nextGameButton.setOpaque(true);
        nextGameButton.setContentAreaFilled(true);
        nextGameButton.setBorderPainted(false);
        nextGameButton.setFocusPainted(false);
        nextGameButton.addActionListener(this);
        nextGameButton.setVisible(false);
        nextGameButton.setFont(f3);

        typedWord = "";
        availableLetters = new DLList<>();

        green = new Color(25, 110, 45);

        textArea.setBounds(50, 100, 200, 400);
        textArea.setEditable(false);
        textArea.setText(addedWords.toString());
        textArea.setFont(f3);
        add(textArea);
        textArea.setVisible(false);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(50, 100, 200, 400);
        add(scrollPane);
        scrollPane.setVisible(false);

        textArea2.setBounds(300, 100, 200, 400);
        textArea2.setEditable(false);
        textArea2.setText(wordBankList.toString());
        textArea2.setFont(f3);
        add(textArea2);
        textArea2.setVisible(false);

        scrollPane2 = new JScrollPane(textArea2);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.setBounds(300, 100, 200, 400);
        add(scrollPane2);
        scrollPane2.setVisible(false);

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
        
        g.setColor(purple);
        g.fillRect(0, 0, 600, 600);

        if (loggedIn && !gameStarted && !gameEnded && !scoreViewed) { // start screen

            g.setColor(Color.white);
            g.fillRect(100, 100, 400, 400);

            g.setColor(Color.black);
            g.setFont(f1);
            g.drawString("How to Play:", 205, 160);
            g.setFont(f2);
            g.drawString("Combine letters to make words. Make as", 125, 220);
            g.drawString("many words as you can in 60s", 160, 240);

            DLList<Character> tmp = new DLList<>();
            tmp.add('a');
            tmp.add('b');
            tmp.add('c');
            tmp.add('d');
            tmp.add('e');
            tmp.add('f');

            g.setFont(f2);
            for (int i = 0; i < 6; i++) {
                int x = 125 + i * 60;
                Character c = tmp.get(i);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x, 300, 50, 50);
                g.setColor(Color.black);
                g.drawString("" + c, x - 20 + 50, 330);
            }

            startButton.setVisible(true);
            nextButton.setVisible(false);
            backButton.setVisible(false);
            nextGameButton.setVisible(false);
        } else if (loggedIn && gameStarted && !gameEnded && !scoreViewed) { // play screen
            startButton.setVisible(false);
            nextButton.setVisible(false);
            backButton.setVisible(false);
            nextGameButton.setVisible(false);

            drawTypedLetters(g);
            drawAvailableLetters(g);

            g.setColor(Color.white);
            g.fillRect(35, 50, 270, 140);

            g.setFont(f2);
            g.setColor(Color.black);
            g.drawString("Player: " + name, 50, 75);
            g.drawString("Score: " + score, 50, 105);
            g.drawString("Time remaining: " + (60000.0 - (System.currentTimeMillis() - startTime)) / 1000.0, 50, 145);

            g.setColor(Color.white);
            g.fillRect(400, 50, 150, 325);
            int wordX = 410;
            int wordY = 70;
            g.setColor(Color.black);
            for (int i = 0; i < addedWords.size(); i++) {
                g.drawString(addedWords.get(i), wordX, wordY);
                wordY += 20;
            }

            if (System.currentTimeMillis() - startTime >= 1000 * 10) { // TIMEOUT
                gameEnded = true;

                textArea.setText(addedWords.toString());
                textArea2.setText(wordBankList.toString());

                totalScore += score;
                score = 0;

                try {
                    outObj.reset();
                    outObj.writeObject(name + " " + totalScore);
                    System.out.println(name + " " + totalScore);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } else if (loggedIn && gameStarted && gameEnded && !scoreViewed) { // end game, your results
            g.setColor(purple);
            g.fillRect(0, 0, 600, 600);
            g.setColor(Color.black);

            g.setFont(f2);
            g.drawString("Player: " + name, 50, 50);
            g.drawString("Your guesses:", 50, 80);
            g.drawString("Possible anagrams:", 300, 80);

            nextButton.setVisible(true);
            backButton.setVisible(false);
            nextGameButton.setVisible(false);

            textArea.setVisible(true);
            scrollPane.setVisible(true);
            textArea2.setVisible(true);
            scrollPane2.setVisible(true);

        } else if (loggedIn && gameStarted && gameEnded && scoreViewed) { // end game, see leaderboard
            g.setColor(Color.black);

            g.setFont(f3);
            g.drawString("Player: " + name, 50, 50);

            g.setColor(purple);
            g.fillRect(0, 0, 600, 600);
            g.setColor(Color.white);
            g.fillRect(100, 100, 400, 400);

            g.setColor(Color.black);
            g.setFont(f1);
            g.drawString("Leaderboard: ", 200, 200);

            nextButton.setVisible(false);
            backButton.setVisible(true);
            nextGameButton.setVisible(true);
            textArea.setVisible(false);
            scrollPane.setVisible(false);
            textArea2.setVisible(false);
            scrollPane2.setVisible(false);

            sort(finalScores);
            ///System.out.println("finalScorestoStr:" + finalScores.toString());
            for (int i = 0; i < finalScores.size(); i++) {
                String name = finalScores.get(i).split("\\s")[0];
                int s = Integer.valueOf(finalScores.get(i).split("\\s")[1]);
                g.setColor(Color.black);
                g.setFont(f2);
                g.drawString(name + ": " + s, 200, 250 + i * 25);
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

            ObjectInputStream inObj = new ObjectInputStream(serverSocket.getInputStream());

            // Receive server location
            while (true) {
                try {
                    Object o = inObj.readObject();
                    if (o instanceof String) {

                        String s = (String) o;
                        if (s.split(" ")[0].equals("word")) {
                            string = s.split(" ")[1];

                            Scanner scan;
                            if (string.equals("tsignr")) {
                                scan = new Scanner(new FileReader("wordbanks/tsignr.txt"));
                            } else if (string.equals("lteicn")) {
                                scan = new Scanner(new FileReader("wordbanks/lteicn.txt"));
                            } else if (string.equals("spneal")) {
                                scan = new Scanner(new FileReader("wordbanks/spneal.txt"));
                            } else if (string.equals("ecstok")) {
                                scan = new Scanner(new FileReader("wordbanks/ecstok.txt"));
                            } else if (string.equals("tderah")) {
                                scan = new Scanner(new FileReader("wordbanks/tderah.txt"));
                            } else if (string.equals("armsfe")) {
                                scan = new Scanner(new FileReader("wordbanks/armsfe.txt"));
                            } else {
                                scan = new Scanner(new FileReader("wordbanks/null.txt"));
                            }

                            wordBankList = new DLList<>();
                            while (scan.hasNextLine()) {
                                String word = scan.nextLine();
                                wordBankList.add(word);
                            }

                            availableLetters = new DLList<>();
                            char[] arr = string.toCharArray();
                            for (char each : arr) {
                                availableLetters.add(each);
                            }

                        } else {
                            // finalScores = new DLList<String>();
                            System.out.println("s: " + s);
                            finalScores.add(s);
                        }
                    }
                    if (o instanceof Integer) {
                        Integer i = (Integer) o;
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
        if (e.getSource() == nextButton) {
            scoreViewed = true;
        }
        if (e.getSource() == backButton) {
            backButton.setVisible(false);
            scoreViewed = false;
        }
        if (e.getSource() == nextGameButton) {

            loggedIn = true;
            gameStarted = false;
            gameEnded = false;
            scoreViewed = false;

            typedWord = "";

            finalScores = new DLList<String>();
            addedWords = new DLList<String>();

            for (int i = 0; i < 6; i++) {
                availableLetters.remove(0);
            }
            char[] arr = string.toCharArray();
            for (char each : arr) {
                availableLetters.add(each);
            }

            try {
                outObj.reset();
                outObj.writeObject(true);
            } catch (Exception err) {

            }
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
        g.setFont(f1);
        g.setColor(Color.black);
        for (int i = 0; i < typedWord.length(); i++) {
            int x = 5 + i * 100;
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
            g.drawString("" + c, x - 10 + 50, 540);
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

    public void sort(DLList<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                int score1 = Integer.valueOf(list.get(j).split("\\s")[1]);
                int score2 = Integer.valueOf(list.get(j + 1).split("\\s")[1]);

                if (score1 < score2) {
                    String tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                }
            }
        }
    }

}
