import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class User {
    String userId;
    String password;

    User(String id, String pass) {
        userId = id;
        password = pass;
    }
}



class Question {
    String question;
    String[] options;
    int correct;

    Question(String q, String[] o, int c) {
        question = q;
        options = o;
        correct = c;
    }
}



public class OnlineExamGUI {

    static User user = new User("vasu", "1234");

    static Question[] questions = {
        new Question("Java is a ___ language?",
                new String[]{"Programming", "Cooking", "Driving", "None"}, 0),

        new Question("Which is not OOP concept?",
                new String[]{"Encapsulation", "Inheritance", "Compilation", "Polymorphism"}, 2),

        new Question("Keyword to create class?",
                new String[]{"define", "class", "struct", "object"}, 1)
    };

    static int current = 0, score = 0, timeLeft = 60;
    static Timer timer;

    public static void main(String[] args) {
        loginScreen();
    }



    static void loginScreen() {
        JFrame f = new JFrame("Login");
        f.setSize(300, 200);
        f.setLayout(new GridLayout(3, 2, 5, 5));

        JTextField id = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton login = new JButton("Login");

        f.add(new JLabel("User ID"));
        f.add(id);
        f.add(new JLabel("Password"));
        f.add(pass);
        f.add(new JLabel(""));
        f.add(login);

        login.addActionListener(e -> {
            if (id.getText().equals(user.userId) &&
                String.valueOf(pass.getPassword()).equals(user.password)) {
                f.dispose();
                menuScreen();
            } else {
                JOptionPane.showMessageDialog(f, "Invalid Login");
            }
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    

    static void menuScreen() {
        JFrame f = new JFrame("Menu");
        f.setSize(300, 200);
        f.setLayout(new GridLayout(3, 1, 10, 10));

        JButton update = new JButton("Update Password");
        JButton exam = new JButton("Start Exam");
        JButton logout = new JButton("Logout");

        f.add(update);
        f.add(exam);
        f.add(logout);

        update.addActionListener(e -> {
            String newPass = JOptionPane.showInputDialog("Enter new password:");
            if (newPass != null && !newPass.isEmpty()) {
                user.password = newPass;
                JOptionPane.showMessageDialog(f, "Password Updated!");
            }
        });

        exam.addActionListener(e -> {
            f.dispose();
            startExam();
        });

        logout.addActionListener(e -> {
            f.dispose();
            loginScreen();
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    

    static void startExam() {
        JFrame f = new JFrame("Online Exam");
        f.setSize(500, 400);
        f.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel timerLabel = new JLabel("Time: " + timeLeft);
        timerLabel.setHorizontalAlignment(JLabel.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(timerLabel, BorderLayout.NORTH);
        topPanel.add(questionLabel, BorderLayout.CENTER);

        

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        ButtonGroup group = new ButtonGroup();
        JRadioButton[] options = new JRadioButton[4];

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 14));
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }



        JPanel bottomPanel = new JPanel();
        JButton next = new JButton("Next");
        bottomPanel.add(next);

        f.add(topPanel, BorderLayout.NORTH);
        f.add(optionsPanel, BorderLayout.CENTER);
        f.add(bottomPanel, BorderLayout.SOUTH);

    


        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);

            if (timeLeft <= 0) {
                timer.stop();
                showResult(f);
            }
        });
        timer.start();

        

        loadQuestion(questionLabel, options);

        

        next.addActionListener(e -> {
            int selected = -1;

            for (int i = 0; i < 4; i++) {
                if (options[i].isSelected()) {
                    selected = i;
                }
            }

            if (selected == questions[current].correct) {
                score++;
            }

            current++;

            if (current < questions.length) {
                loadQuestion(questionLabel, options);
                group.clearSelection();
            } else {
                timer.stop();
                showResult(f);
            }
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }




    static void loadQuestion(JLabel qLabel, JRadioButton[] options) {
        qLabel.setText("Q" + (current + 1) + ": " + questions[current].question);

        for (int i = 0; i < 4; i++) {
            options[i].setText(questions[current].options[i]);
        }
    }




    static void showResult(JFrame f) {
        f.dispose();
        JOptionPane.showMessageDialog(null,
                "Exam Finished!\nScore: " + score + "/" + questions.length);

        current = 0;
        score = 0;
        timeLeft = 60;

        menuScreen();
    }
}