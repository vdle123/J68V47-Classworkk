import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class Calculator {
    static Color darker_gray = new Color(100,100,100);
    static Color light_blue = new Color(123,159,207);
    private JFrame frame;
    private JTextField textField;
    private JPanel buttonPanel;

    private boolean clearFlag = false; // Flag to determine if the text field should be cleared after pressing CE

    public Calculator() {


        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setMinimumSize(new Dimension(500, 600));
        frame.setLayout(new BorderLayout());


        textField = new JTextField();
        textField.setEditable(false);
        textField.setBackground(new Color(39, 39, 39));
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Verdana", Font.PLAIN, (frame.getHeight()+frame.getWidth())/30));
        textField.setFocusable(false);
        textField.setBorder(null);
        textField.setPreferredSize(new Dimension(frame.getWidth(),frame.getHeight()/5));
        frame.add(textField, BorderLayout.NORTH);

        try { //Gets image from url & places it on icons
            URL imageUrl = new URL("https://imgur.com/fVXag8E.jpg");
            Image image = ImageIO.read(imageUrl);
            frame.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 6, 1, 1));

        String[][] buttonLabels = { //PATAISYK UI $$$
                {"Func1", "Func2", "%", "CE", "del", "/"},
                {"(", ")","7", "8", "9", "*"},
                {"^", "abs","4", "5", "6", "-"},
                {"rnd", "e","1", "2", "3", "+"},
                {"π", "√","0", ".", "+/-", "="}


        };
        String[] darkerbuttons = {"Func1", "Func2", "%", "CE", "del", "/", "*", "-", "+"};
        for (String[] row : buttonLabels) {
            for (String label : row) {
                JButton button = new JButton(label);
                button.setFont(new Font("Verdana", Font.BOLD, (frame.getHeight()+frame.getWidth())/60));
                button.setForeground(Color.WHITE);
                button.setBackground(Color.GRAY);
                Border originalBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
                button.setBorder(originalBorder);
                Border hoverBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2);

                // Add a MouseListener to detect mouse enter and exit events
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        // Change border on mouse enter
                        button.setBorder(hoverBorder);
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        // Revert border on mouse exit
                        button.setBorder(originalBorder);
                    }
                });
                button.addActionListener(new ButtonClickListener());
                button.setFocusPainted(false);
                if(label.equals("=")){
                    button.setBackground(light_blue);
                }
                if(Arrays.asList(darkerbuttons).contains(label)) {
                    button.setBackground(darker_gray); // Set background color to blue
                }
                buttonPanel.add(button);
            }
        }
        buttonPanel.setBackground(Color.DARK_GRAY);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); //start program in the middle of the screen
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { //makes changes if the window has been resized

                textField.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 5));
                textField.setFont(new Font("Verdana", Font.PLAIN, (frame.getHeight()+frame.getWidth())/30));
                double ratio = (double) frame.getSize().height /(double)frame.getWidth();
                System.out.print("W: "+  frame.getWidth() + "H: "+ frame.getHeight()+"ratio "+ratio+ "\n");
                if(ratio>1.5){
                    int height = (int) (frame.getWidth()*1.5);
                    frame.setSize(frame.getWidth(), height);
                }
                // Calculate the new font size based on the frame's width and height
                int newFontSize = (frame.getHeight() + frame.getWidth()) / 60;

                // Loop through each button in the button panel and update its font size
                for (Component component : buttonPanel.getComponents()) {
                    if (component instanceof JButton) {
                        JButton button = (JButton) component;
                        button.setFont(new Font("Verdana", Font.BOLD, newFontSize));
                    }
                }
            }
        });
    }


    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String buttonText = clickedButton.getText();

            if (buttonText.equals("Func2")) {
                replaceButtonLabels(new String[]{"Func1", "Func2", "%", "CE", "del", "/", "!", "sin","7","8","9","*","cos", "tan","4","5","6","-","mean","stdevp", "1","2","3","+","atan", "acos"});
            } else if (buttonText.equals("Func1")) {
                replaceButtonLabels(new String[]{"Func1", "Func2", "%", "CE", "del", "/", "(", ")","7","8","9","*","^","abs","4","5","6","-","rnd","e", "1","2","3","+","π", "√"});
            }
            else {
                String command = e.getActionCommand();
                switch (command) {
                    case "=":
                        try {
                            String expression = textField.getText();
                            double result = evaluateExpression(expression);
                            if (result == (long) result) {
                                textField.setText(String.format("%d", (long) result));
                            } else {
                                textField.setText(Double.toString(result));
                            }
                            clearFlag = true;
                        } catch (NumberFormatException | ArithmeticException ex) {
                            textField.setText("Error");
                        }
                        break;
                    case "CE":
                        textField.setText("");
                        break;
                    case "del":
                        String text = textField.getText();
                        if (!text.isEmpty()) {
                            textField.setText(text.substring(0, text.length() - 1));
                        }
                        break;
                    case "%":
                        try {
                            String expression = textField.getText();
                            double result = evaluatePercentage(expression);
                            textField.setText(Double.toString(result));
                            clearFlag = true;
                        } catch (NumberFormatException | ArithmeticException ex) {
                            textField.setText("Error");
                        }
                        break;
                    case "+/-":
                        //perdaug darbo del tokio sudo (poto padarysiu muset)
                        break;
                    case "Func1":
                        //switch to 1st page of functions
                        break;
                    case "Func2":
                        //Switch to 2nd page of functions
                        break;
                    default:
                        if (clearFlag) {
                            textField.setText("");
                            clearFlag = false;
                        }
                        textField.setText(textField.getText() + command);
                        break;
                }
            }
        }

        private void replaceButtonLabels(String[] newLabels) {
            Component[] components = buttonPanel.getComponents();
            for (int i = 0; i < Math.min(components.length, newLabels.length); i++) {
                if (components[i] instanceof JButton) {
                    JButton button = (JButton) components[i];
                    button.setText(newLabels[i]);
                }
            }
        }
    }


        private double evaluateExpression(String expression) {
            return CalculatorEngine.evaluate(expression);
        }

        private double evaluatePercentage(String expression) {
            double result = CalculatorEngine.evaluate(expression);
            return result / 100;
        }


    public static void main(String[] args) {
        new Calculator();
    }
}

class CalculatorEngine {
    public static double evaluate(String expression) {
        return new Object() {
            int pos = -1, ch;

            double factorial(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Factorial is not defined for negative numbers");
                }
                int result = 1;
                for (int i = 2; i <= n; i++) {
                    result *= i;
                }
                return result;
            }

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) {
                        x *= parseFactor();
                    } else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        x /= divisor;
                    } else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x = 0;
                int startPos = this.pos;

                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (ch == 'a') {
                    if (this.pos + 2 < expression.length() && expression.startsWith("abs", this.pos)) {
                        this.pos += 3;
                        eat('a');
                        eat('(');
                        x = Math.abs(parseExpression());
                        eat(')');
                    } else if (pos + 3 < expression.length() && expression.startsWith("atan", pos)) {
                        pos += 3;
                        eat('a');
                        eat('(');
                        x = Math.atan(parseExpression());
                        x = Math.toDegrees(x);
                        eat(')');
                    } else if (pos + 3 < expression.length() && expression.startsWith("acos", pos)) {
                        pos += 3;
                        eat('a');
                        eat('(');
                        x = Math.acos(parseExpression());
                        x = Math.toDegrees(x);
                        eat(')');
                    } else if (pos + 3 < expression.length() && expression.startsWith("asin", pos)) {
                        pos += 3;
                        eat('a');
                        eat('(');
                        x = Math.asin(parseExpression());
                        x = Math.toDegrees(x);
                        eat(')');
                    }
                } else if (ch == 'r') {
                    if (this.pos + 4 < expression.length() && expression.startsWith("round", pos)) {
                        pos += 5;
                        eat('r');
                        eat('(');
                        x = Math.round(parseExpression());
                        eat(')');
                    }
                } else if (ch == 'e') {
                    eat('e');
                    x = Math.E;
                } else if (ch == 'π') {
                    eat('π');
                    x = Math.PI;
                } else if (ch == '√') {
                    eat('√');
                    x = Math.sqrt(parseExpression());
                } else if (ch == '!') {
                    eat('!'); //reikes pataisyt kad parseExpression() skaitytu sitam is nugaros surinkes visus skaicius ir atiduotu (nemazai darbo)
                    x = factorial((int) parseExpression());
                } else if (ch == 's') {
                    if (pos + 2 < expression.length() && expression.startsWith("sin", pos)) {
                        pos += 2;
                        eat('s');
                        eat('(');
                        x = Math.toRadians(parseExpression());
                        x = Math.sin(x);
                        eat(')');
                    }
                } else if (ch == 'c') {
                    if (pos + 2 < expression.length() && expression.startsWith("cos", pos)) {
                        pos += 2;
                        eat('c');
                        eat('(');
                        x = Math.toRadians(parseExpression());
                        x = Math.cos(x);
                        eat(')');
                    }
                } else if (ch == 't') {
                    if (pos + 2 < expression.length() && expression.startsWith("tan", pos)) {
                        pos += 2;
                        eat('t');
                        eat('(');
                        x = Math.toRadians(parseExpression());
                        x = Math.tan(x);
                        eat(')');
                    }
                } else if (ch == 'l') {
                    if (pos + 2 < expression.length() && expression.startsWith("log", pos)) {
                        pos += 2;
                        eat('l');
                        eat('(');
                        x = Math.log(parseExpression());
                        eat(')');
                    } else if (pos + 1 < expression.length() && expression.startsWith("ln", pos)) {
                        pos += 1;
                        eat('l');
                        eat('(');
                        x = Math.log1p(parseExpression());
                        eat(')');
                    }
                }
                else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());
                return x;
            }
        }.parse();
    }
}