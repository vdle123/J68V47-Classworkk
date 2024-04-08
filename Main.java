import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main{
    static String num1 = "0";
    static JLabel input_label = new JLabel(num1);
    public static void main(String[] args) {
        Color Theme_Color = new Color(19, 19, 19); //This color will be used as main background color

        //MAIN FRAME
        JFrame Main_Page = new JFrame("Calculator");

        //INPUT/OUTPUT AREA
        JPanel Input_Panel = new JPanel();
        //JLabel input_label = new JLabel(num1);
        //input_label.setLocation(0, 200);
        input_label.setFont(new Font("Verdana", Font.BOLD, 70));
        input_label.setForeground(Color.BLACK);
        Input_Panel.setBackground(Color.BLUE);
        //Input_Panel.add(input_label);

        JPanel Func_Panel = new JPanel(new GridLayout(6, 4, 2, 2));
        JPanel Switch_Panel = new JPanel();

        //MAIN BUTTONS
        String[] button_array = {"%", "CE", "C", "del", "1⁄x", "x²", "2√x", "/", "7", "8", "9", "x", "4", "5", "6", "-", "1", "2", "3", "+", "+/-", "0", ".", "="};

        ButtonActionListener buttonListener = new ButtonActionListener();

        for (int i = 0; i < button_array.length; i++) {
            JButton button = new JButton(button_array[i]);
            button.addActionListener(buttonListener);
            Func_Panel.add(button);
            button.setFont(new Font("Verdana", Font.BOLD, 25));
        }
        //SWITCH BUTTONS
        JButton Func = new JButton("Func");
        JButton Func1 = new JButton("Func1");
        Switch_Panel.add(Func);
        Switch_Panel.add(Func1);
        Func.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Switch_Panel.setBackground(Color.YELLOW);
            }
        });

        Func1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Switch_Panel.setBackground(Color.DARK_GRAY);
            }
        });
        // Set layouts
        Main_Page.setLayout(new BorderLayout());
        Input_Panel.setLayout(new BorderLayout());

        // Set sizes (optional)
        Input_Panel.setPreferredSize(new Dimension(800, 200));
        input_label.setPreferredSize(new Dimension(800, 200));
        Func_Panel.setPreferredSize(new Dimension(400, 600));
        Switch_Panel.setPreferredSize(new Dimension(200, 600));

        // Set backgrounds
        Switch_Panel.setBackground(Color.ORANGE);
        Func_Panel.setBackground(Color.RED);
        Input_Panel.setBackground(Color.DARK_GRAY);
        input_label.setBackground(Color.BLUE);

        // Add components
        //Main_Page.add(Input_Panel, BorderLayout.PAGE_START);
        Main_Page.add(Func_Panel, BorderLayout.CENTER);
        Main_Page.add(Switch_Panel, BorderLayout.WEST);
        Main_Page.add(input_label,BorderLayout.NORTH);

        Main_Page.setSize(800, 800);
        Main_Page.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main_Page.setVisible(true);
    }

    static class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            // Perform actions based on the command
            switch (command) {
                case "%":
                    System.out.println("% has been pressed");
                    break;
                case "CE":
                    System.out.println("CE has been pressed");
                    break;
                case "C":
                    System.out.println("C has been pressed");
                    break;
                case "del":
                    System.out.println("del has been pressed");
                    break;
                case "1⁄x":
                    System.out.println("1/x has been pressed");
                    break;
                case "x²":
                    System.out.println("x^2 has been pressed");
                    break;
                case "2√x":
                    System.out.println("vx has been pressed");
                    break;
                case "/":
                    System.out.println("/ has been pressed");
                    break;
                case "7":
                    System.out.println("7 has been pressed");
                    break;
                case "8":
                    System.out.println("8 has been pressed");
                    break;
                case "9":
                    System.out.println("9 has been pressed");
                    break;
                case "x":
                    System.out.println("x has been pressed");
                    break;
                case "4":
                    System.out.println("4 has been pressed");
                    break;
                case "5":
                    System.out.println("5 has been pressed");
                    break;
                case "6":
                    System.out.println("6 has been pressed");
                    break;
                case "-":
                    System.out.println("- has been pressed");
                    break;
                case "1":
                    System.out.println("1 has been pressed");
                    num1 = "1";
                    input_label.setText(num1);

                    break;
                case "2":
                    System.out.println("2 has been pressed");
                    break;
                case "3":
                    System.out.println("3 has been pressed");
                    num1="+";
                    input_label.setText(num1);
                    break;
                case "+":
                    System.out.println("+ has been pressed");
                    break;
                case "+/-":
                    System.out.println("+/_ has been pressed");
                    break;
                case "0":
                    System.out.println("0 has been pressed");
                    break;
                case ".":
                    System.out.println(". has been pressed");
                    break;
                case "=a":
                    System.out.println("= has been pressed");
                    break;

                default:
                    System.out.println("Error, button has been pressed outside of the list");
                    break;
            }
        }
    }
}