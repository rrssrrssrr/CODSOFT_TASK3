import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMFrame extends JFrame {
    private BankAccount userAccount;
    private int enteredPin;
    private int[] correctPins = {1111, 1112, 1113, 1114, 1115};
    private int currentChoice;

    public ATMFrame() {
        userAccount = new BankAccount(1000.0);

        setLayout(new GridLayout(1, 2));
        getContentPane().setBackground(new Color(128, 0, 128));
       
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout());
        leftPanel.setBackground(new Color(128, 0, 128));

        JLabel pinLabel = new JLabel("Enter PIN:");
        pinLabel.setForeground(Color.WHITE);
        leftPanel.add(pinLabel);

        JTextField pinField = new JTextField(10);
        leftPanel.add(pinField);

        JButton submitPinButton = new JButton("Submit PIN");
        leftPanel.add(submitPinButton);

        submitPinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enteredPin = Integer.parseInt(pinField.getText());
                validatePin();
            }
        });


JPanel rightPanel = new JPanel();
rightPanel.setLayout(new GridLayout(4, 3)); 
rightPanel.setBackground(new Color(128, 0, 128));

for (int i = 0; i <= 9; i++) {
    final int digit = i;  
    JButton numberButton = new JButton(Integer.toString(i));
    rightPanel.add(numberButton);

    numberButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           
            pinField.setText(pinField.getText() + digit);
        }
    });
}

JPasswordField passwordField = new JPasswordField();
rightPanel.add(passwordField);


add(leftPanel);
add(rightPanel);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200); 
        setVisible(true);
    }

    public void validatePin() {
        boolean pinValidated = false;
        for (int correctPin : correctPins) {
            if (enteredPin == correctPin) {
                pinValidated = true;
                break;
            }
        }

        if (pinValidated) {
            removeComponents();
            showOptions();
        } else {
            showErrorMessage("Invalid PIN. Please try again.");
        }
    }

    public void showOptions() {
        JLabel optionsLabel = new JLabel("Select an option:");
        optionsLabel.setForeground(Color.WHITE);
        add(optionsLabel);

        JButton withdrawButton = new JButton("Withdraw");
        add(withdrawButton);

        JButton depositButton = new JButton("Deposit");
        add(depositButton);

        JButton checkBalanceButton = new JButton("Check Balance");
        add(checkBalanceButton);

        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeComponents();
                showAmountInput("Enter the amount to withdraw:");
                currentChoice = 1; 
            }
        });

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeComponents();
                showAmountInput("Enter the amount to deposit:");
                currentChoice = 2; 
            }
        });

        checkBalanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performTransaction(0);
            }
        });

        pack();
    }

    public void showAmountInput(String message) {
        JLabel amountLabel = new JLabel(message);
        amountLabel.setForeground(Color.WHITE);
        add(amountLabel);

        JTextField amountField = new JTextField(10);
        add(amountField);

        JButton submitAmountButton = new JButton("Submit Amount");
        add(submitAmountButton);

        submitAmountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double amount = Double.parseDouble(amountField.getText());
                performTransaction(amount);
            }
        });

        pack();
    }

    public void performTransaction(double amount) {
        String resultMessage = "";
        switch (currentChoice) {
            case 0: 
                resultMessage = "Current balance: " + userAccount.getBalance();
                break;
            case 1: 
                if (amount <= userAccount.getBalance()) {
                    userAccount.withdraw(amount);
                    resultMessage = "Withdrawal successful. Remaining balance: " + userAccount.getBalance();
                } else {
                    resultMessage = "Insufficient funds. Withdrawal failed.";
                }
                break;
            case 2: 
                userAccount.deposit(amount);
                resultMessage = "Deposit successful. New balance: " + userAccount.getBalance();
                break;
        }

        removeComponents();
        showResultMessage(resultMessage);
    }

    public void removeComponents() {
        getContentPane().removeAll();
        repaint();
    }

    public void showResultMessage(String message) {
        JLabel resultLabel = new JLabel(message);
        resultLabel.setForeground(Color.WHITE);
        add(resultLabel);

        JButton exitButton = new JButton("Exit");
        add(exitButton);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        pack();
    }

    public void showErrorMessage(String message) {
        JLabel errorLabel = new JLabel(message);
        errorLabel.setForeground(Color.RED);
        add(errorLabel);
        pack();
    }

    class BankAccount {
        private double balance;

        public BankAccount(double initialBalance) {
            this.balance = initialBalance;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            balance += amount;
        }

        public boolean withdraw(double amount) {
            if (amount <= balance) {
                balance -= amount;
                return true;
            } else {
                return false;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMFrame());
    }
}
