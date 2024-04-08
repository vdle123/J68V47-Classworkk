import javax.swing.*;
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
        frame.add(textField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));

        String[][] buttonLabels = {
                {"%", "CE", "del", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"0", ".", "+/-", "="}
        };

        for (String[] row : buttonLabels) {
            for (String label : row) {
                JButton button = new JButton(label);
                button.addActionListener(new ButtonClickListener());
                buttonPanel.add(button);
            }
        }

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
                        textField.setText(Double.toString(result));
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
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
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
