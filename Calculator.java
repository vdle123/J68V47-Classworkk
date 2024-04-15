import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {

    private JFrame frame;
    private JTextField textField;

    private boolean clearFlag = false; // Flag to determine if the text field should be cleared after pressing CE

    public Calculator() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());

        textField = new JTextField();
        textField.setEditable(false);
        textField.setBackground(new Color(39,39,39));
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Verdana", Font.PLAIN, 20));
        textField.setFocusable(false);
        textField.setBorder(null);
        frame.add(textField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 1, 1));

        String[][] buttonLabels = {
                {"%", "CE", "del", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"0", ".", "+/-", "="},
                {"(", ")", "^"}
        };
        for (String[] row : buttonLabels) {
            for (String label : row) {
                JButton button = new JButton(label);
                button.setFont(new Font("Verdana",Font.BOLD,17));
                button.setForeground(Color.WHITE);
                button.setBackground(Color.GRAY);
                Border originalBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
                button.setBorder(originalBorder);
                Border hoverBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2); // Customize as per your preference

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
                buttonPanel.add(button);
            }
        }
        buttonPanel.setBackground(Color.DARK_GRAY);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "=" -> {
                    // Evaluate expression
                    try {
                        String expression = textField.getText();
                        double result = evaluateExpression(expression);
                        if (result == (long) result) {
                            // If it's a whole number, display without decimals
                            textField.setText(String.format("%d", (long) result));
                        } else {
                            // If it's not a whole number, display it with decimal places
                            textField.setText(Double.toString(result));
                        }
                        //textField.setText(Double.toString(result));
                        clearFlag = true;
                    } catch (NumberFormatException | ArithmeticException ex) {
                        textField.setText("Error");
                    }
                }
                case "CE" -> textField.setText("");
                case "del" -> {
                    String text = textField.getText();
                    if (!text.isEmpty()) {
                        textField.setText(text.substring(0, text.length() - 1));
                    }
                }
                case "%" -> {
                    try {
                        String expression = textField.getText();
                        double result = evaluatePercentage(expression);
                        textField.setText(Double.toString(result));
                        clearFlag = true;
                    } catch (NumberFormatException | ArithmeticException ex) {
                        textField.setText("Error");
                    }
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

        private double evaluateExpression(String expression) {
            return CalculatorEngine.evaluate(expression);
        }

        private double evaluatePercentage(String expression) {
            double result = CalculatorEngine.evaluate(expression);
            return result / 100;
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}

class CalculatorEngine {
    public static double evaluate(String expression) {
        return new Object() {
            int pos = -1, ch;

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
                    if(eat('*')) {
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
                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());
                return x;
            }
        }.parse();
    }
}