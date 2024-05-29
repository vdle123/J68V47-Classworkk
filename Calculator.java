import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Robot;

public class Calculator {
    static String[] darkerButtons = {"Func1", "Func2", "%", "CE", "del", "/", "*", "-", "+", "(", ")", "^", "abs", "round", "e", "π", "√", "!", "mean", "cos", "sin", "tan", ","};

    enum Theme {
        LIGHT,
        DARK
    }

    Theme currentTheme = Theme.DARK; //Theme by default
    private final Color lightBackgroundColor = Color.WHITE;
    private final Color darkBackgroundColor = Color.BLACK;
    private final Color lightForegroundColor = Color.BLACK;
    private final Color darkForegroundColor = Color.WHITE;
    static Color darker_gray = new Color(100, 100, 100);
    static Color light_blue = new Color(123, 159, 207);
    private JFrame frame;
    private static JTextField textField;
    private JPanel buttonPanel;
    private JMenuBar menubar;
    private JMenu menu;

    static private boolean clearFlag = false; // Flag to determine if the text field should be cleared after pressing CE

    public static void setTextField(String error) {
        textField.setText(error);
    }

    public static void setClearFlagTrue() {
        clearFlag = true;
    }

    public Calculator() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setMinimumSize(new Dimension(500, 600));
        frame.setLayout(new BorderLayout());

        menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        menu = new JMenu("Themes");
        menubar.add(menu);

        JMenuItem lightThemeItem = new JMenuItem("Light Mode");
        JMenuItem darkThemeItem = new JMenuItem("Dark Mode");

        menu.add(lightThemeItem);
        menu.add(darkThemeItem);


        textField = new JTextField();
        textField.setEditable(false);
        textField.setBackground(new Color(39, 39, 39));
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Verdana", Font.PLAIN, (frame.getHeight() + frame.getWidth()) / 30));
        textField.setFocusable(false);
        textField.setBorder(null);
        textField.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 5));
        frame.add(textField, BorderLayout.NORTH);

        try { //Gets image from url & places it on icons
            URL imageUrl = new URL("https://imgur.com/fVXag8E.jpg");
            Image image = ImageIO.read(imageUrl);
            frame.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 6, 1, 1));

        String[][] buttonLabels = {
                {"(", ")", "^", "!", "abs", "round"},
                {"Func1", "Func2", "%", "CE", "del", "/"},
                {"e", "√", "7", "8", "9", "*"},
                {"π", "mean", "4", "5", "6", "-"},
                {"sin", "cos", "1", "2", "3", "+"},
                {"tan", ",", "0", ".", "+/-", "="}
        };
        String[] darkerButtons = {"Func1", "Func2", "%", "CE", "del", "/", "*", "-", "+", "(", ")", "^", "abs", "round", "e", "π", "√", "!", "mean", "cos", "sin", "tan", ","};
        for (String[] row : buttonLabels) {
            for (String label : row) {
                JButton button = new JButton(label);
                button.setFont(new Font("Verdana", Font.BOLD, (frame.getHeight() + frame.getWidth()) / 60));
                button.setForeground(Color.WHITE);
                button.setBackground(Color.GRAY);
                Border originalBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
                button.setBorder(originalBorder);
                Border hoverBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2);

                // Add a MouseListener to detect mouse enter and exit events
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        // Change border on mouse enter
                        if (button.getBackground() == darker_gray || button.getBackground() == Color.GRAY) {
                            button.setBorder(hoverBorder);
                        }
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        // Revert border on mouse exit
                        button.setBorder(originalBorder);
                    }
                });
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBorder(hoverBorder);
                    }
                });

                button.addActionListener(new ButtonClickListener());
                button.setFocusPainted(false);
                if (label.equals("=")) {
                    button.setBackground(light_blue);
                }
                if (Arrays.asList(darkerButtons).contains(label) && currentTheme == Theme.DARK) {
                    button.setBackground(darker_gray); // Set background color to gray
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
                textField.setFont(new Font("Verdana", Font.PLAIN, (frame.getHeight() + frame.getWidth()) / 30));
                double ratio = (double) frame.getSize().height / (double) frame.getWidth();
                if (ratio > 1.5) {
                    int height = (int) (frame.getWidth() * 1.5);
                    frame.setSize(frame.getWidth(), height);
                    try {
                        Robot robot = new Robot();
                        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                    } catch (AWTException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                // Calculate the new font size based on the frame's width and height
                int newFontSize = (frame.getHeight() + frame.getWidth()) / 60;

                // Loop through each button in the button panel and update its font size
                for (Component component : buttonPanel.getComponents()) {
                    if (component instanceof JButton button) {
                        button.setFont(new Font("Verdana", Font.BOLD, newFontSize));
                    }
                }
            }

        });
        lightThemeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTheme(Theme.LIGHT);
                updateButtonColors();
            }
        });

        darkThemeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTheme(Theme.DARK);
                updateButtonColors();
            }
        });
    }

    private void applyTheme() {
        switch (currentTheme) {
            case LIGHT:
                frame.getContentPane().setBackground(lightBackgroundColor);
                textField.setBackground(lightBackgroundColor);
                textField.setForeground(lightForegroundColor);
                buttonPanel.setBackground(Color.WHITE);


                break;
            case DARK:
                frame.getContentPane().setBackground(darkBackgroundColor);
                textField.setBackground(new Color(39, 39, 39));
                textField.setForeground(darkForegroundColor);
                buttonPanel.setBackground(Color.DARK_GRAY);
                break;
        }
    }

    private void updateButtonColors() {
        if (currentTheme == Theme.LIGHT) {
            for (Component component : buttonPanel.getComponents()) {
                if (component instanceof JButton) {
                    JButton button = (JButton) component;
                    if (Arrays.asList(darkerButtons).contains(button.getText())) {
                        button.setBackground(Color.lightGray);
                        button.setForeground(Color.BLACK);
                    } else if (button.getText() == "=") {
                        button.setBackground(light_blue);

                    } else {
                        button.setBackground(Color.WHITE);
                        button.setForeground(Color.BLACK);
                    }
                }
            }
        } else {
            for (Component component : buttonPanel.getComponents()) {
                if (component instanceof JButton) {
                    JButton button = (JButton) component;
                    if (Arrays.asList(darkerButtons).contains(button.getText())) {
                        button.setBackground(darker_gray);
                        button.setForeground(Color.WHITE);
                    } else if (button.getText() == "=") {
                        button.setBackground(light_blue);
                    } else {
                        button.setBackground(Color.GRAY);
                        button.setForeground(Color.WHITE);
                    }

                }
            }
        }
    }

    private void setTheme(Theme theme) {
        currentTheme = theme;
        applyTheme();
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String buttonText = clickedButton.getText();

            if (buttonText.equals("Func2")) {
                replaceButtonLabels(new String[]{"(", ")", "^", "!", "abs", "round", "Func1", "Func2", "%", "CE", "del", "/", "asin", "acos", "7", "8", "9", "*", "atan", "stdevp", "4", "5", "6", "-", "nPr", "nCr", "1", "2", "3", "+", "log", "ln", "0", ".", "+/-", "="});
            } else if (buttonText.equals("Func1")) {
                replaceButtonLabels(new String[]{"(", ")", "^", "!", "abs", "round", "Func1", "Func2", "%", "CE", "del", "/", "e", "√", "7", "8", "9", "*", "π", "mean", "4", "5", "6", "-", "sin", "cos", "1", "2", "3", "+", "tan", ",", "0", ".", "+/-", "="});
            } else {
                String command = e.getActionCommand();
                switch (command) {
                    case "=" -> {
                        try {
                            String expression = textField.getText();
                            double result = evaluateExpression(expression);
                            if (result == (long) result) {
                                textField.setText(String.format("%d", (long) result));
                            } else {
                                textField.setText(Double.toString(result));
                            }
                            clearFlag = true;
                        } catch (NumberFormatException ex) {
                            textField.setText("Invalid input error");
                            clearFlag = true;
                        } catch (ArithmeticException ex) {
                            textField.setText("Division by zero");
                            clearFlag = true;
                        }
                    }
                    case "CE" -> textField.setText("");
                    case "del" -> {
                        String text = textField.getText();
                        if (!text.isEmpty()) {
                            textField.setText(text.substring(0, text.length() - 1));
                        }
                    }
                    case "+/-" -> {
                        String expression = textField.getText();
                        StringBuilder temp = new StringBuilder();
                        int lastNumberStart = -1;
                        int lastNumberEnd = expression.length() - 1;

                        for (int i = expression.length() - 1; i >= 0; i--) {
                            char ch = expression.charAt(i);
                            if (Character.isDigit(ch) || ch == '.') {
                                if (lastNumberEnd == expression.length() - 1) {
                                    lastNumberEnd = i;
                                }
                                temp.insert(0, ch);
                            } else if (ch == '-') {
                                if (i == 0 || !Character.isDigit(expression.charAt(i - 1))) {
                                    temp.insert(0, ch);
                                    lastNumberStart = i;
                                } else {
                                    lastNumberStart = i + 1;
                                }
                                break;
                            } else {
                                lastNumberStart = i + 1;
                                break;
                            }
                        }

                        if (lastNumberStart == -1) {
                            lastNumberStart = 0;
                        }

                        if (!temp.isEmpty()) {
                            if (temp.charAt(0) == '-') {
                                temp.deleteCharAt(0);
                            } else {
                                temp.insert(0, '-');
                            }
                        }

                        expression = expression.substring(0, lastNumberStart) + temp + expression.substring(lastNumberEnd + 1);
                        textField.setText(expression);
                    }
                    default -> {
                        if (clearFlag) {
                            textField.setText("");
                            clearFlag = false;
                        }
                        textField.setText(textField.getText() + command);
                    }
                }
            }
        }

        private void replaceButtonLabels(String[] newLabels) {
            Component[] components = buttonPanel.getComponents();
            for (int i = 0; i < Math.min(components.length, newLabels.length); i++) {
                if (components[i] instanceof JButton button) {
                    button.setText(newLabels[i]);
                }
            }
        }
    }


    private double evaluateExpression(String expression) {
        return CalculatorEngine.evaluate(expression);
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
                    Calculator.setTextField("Invalid negative input");
                    Calculator.setClearFlagTrue();
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
                            Calculator.setTextField("Division by zero");
                            Calculator.setClearFlagTrue();
                            throw new ArithmeticException("Division by zero");
                        }
                        x /= divisor;
                    } else if (eat('%')) {
                        double mod = parseFactor();
                        if (mod == 0) {
                            Calculator.setTextField("Modulus by zero");
                            Calculator.setClearFlagTrue();
                            throw new ArithmeticException("Modulus by zero");
                        }
                        x %= mod;
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
                        pos += 2;
                        eat('a');
                        eat('(');
                        x = Math.abs(parseExpression());
                        eat(')');
                    }
                    if (expression.startsWith("atan", pos) || expression.startsWith("acos", pos) || expression.startsWith("asin", pos)) {
                        String type = expression.substring(pos, pos + 4);
                        this.pos += 3;
                        eat('a');
                        eat('(');
                        switch (type) {
                            case "atan" -> {
                                x = Math.atan(parseExpression());
                                x = Math.toDegrees(x);
                            }
                            case "acos" -> {
                                x = Math.acos(parseExpression());
                                x = Math.toDegrees(x);
                            }
                            case "asin" -> {
                                x = Math.asin(parseExpression());
                                x = Math.toDegrees(x);
                            }
                        }
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
                    eat('!');
                    x = factorial((int) parseExpression());
                } else if (ch == 's') {
                    if (pos + 2 < expression.length() && expression.startsWith("sin", pos)) {
                        pos += 2;
                        eat('s');
                        eat('(');
                        x = Math.toRadians(parseExpression());
                        x = Math.sin(x);
                        eat(')');
                    } else if (pos + 5 < expression.length() && expression.startsWith("stdevp", pos)) {
                        pos += 5;
                        eat('s');
                        eat('(');
                        ArrayList<Double> values = new ArrayList<>();
                        while (expression.charAt(pos) != ')') {
                            values.add(parseExpression());
                            if (expression.charAt(pos) == ',') {
                                eat(',');
                            }
                        }
                        eat(')');
                        double sum = 0;
                        for (double value : values) {
                            sum += value;
                        }
                        double mean = sum / values.size();
                        double sumOfSquares = 0;
                        for (double value : values) {
                            sumOfSquares += Math.pow(value - mean, 2);
                        }
                        return Math.sqrt(sumOfSquares / values.size());
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
                        x = Math.log10(parseExpression());
                        eat(')');
                    } else if (pos + 1 < expression.length() && expression.startsWith("ln", pos)) {
                        pos += 1;
                        eat('l');
                        eat('(');
                        x = Math.log(parseExpression());
                        eat(')');
                    }
                } else if (ch == 'm') {
                    if (pos + 3 < expression.length() && expression.startsWith("mean", pos)) {
                        pos += 3;
                        eat('m');
                        eat('(');
                        double sum = 0;
                        int count = 0;
                        for (; ; ) {
                            double num = parseExpression();
                            sum += num;
                            count++;
                            if (eat(',')) continue;
                            if (ch == ')') break;
                            else throw new RuntimeException("Unexpected: " + (char) ch);
                        }
                        eat(')');
                        if (count == 0)
                            throw new ArithmeticException("Mean is not defined for an empty list of numbers");
                        return sum / count;
                    }
                } else if (ch == 'n') {
                    if (pos + 2 < expression.length() && (expression.startsWith("nPr", pos) || expression.startsWith("nCr", pos))) {
                        String type = expression.substring(pos, pos + 3);
                        pos += 2;
                        eat('n');
                        eat('(');
                        double n = parseExpression();
                        eat(',');
                        double r = parseExpression();
                        eat(')');
                        if (r < 0 || r > n) {
                            Calculator.setTextField("Invalid input");
                            Calculator.setClearFlagTrue();
                            throw new IllegalArgumentException("Invalid input. n and r must be non-negative, and r must be less than or equal to n.");
                        }
                        if (type.equals("nPr")) {
                            return factorial((int) n) / factorial((int) (n - r));
                        } else if (type.equals("nCr")) {
                            return factorial((int) n) / (factorial((int) r) * factorial((int) (n - r)));
                        }
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());
                return x;
            }
        }.parse();
    }
}
