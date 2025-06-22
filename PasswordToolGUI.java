import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.*;

public class PasswordToolGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PasswordToolGUI().createGUI());
    }

    public void createGUI() {
        JFrame frame = new JFrame("🔐 Password Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("🛠 Generate", createGeneratePanel());
        tabs.add("🧪 Check", createCheckPanel());

        frame.add(tabs);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createGeneratePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField lengthField = new JTextField(10);
        JCheckBox lower = new JCheckBox("Include lowercase (a-z)");
        JCheckBox upper = new JCheckBox("Include uppercase (A-Z)");
        JCheckBox digits = new JCheckBox("Include digits (0-9)");
        JCheckBox symbols = new JCheckBox("Include symbols (!@#$...)");
        JButton generateBtn = new JButton("🎲 Generate");
        JTextField output = new JTextField();
        output.setEditable(false);
        JButton copyBtn = new JButton("📋 Copy");

        panel.add(new JLabel("Enter Password Length:"));
        panel.add(lengthField);
        panel.add(lower);
        panel.add(upper);
        panel.add(digits);
        panel.add(symbols);
        panel.add(generateBtn);
        panel.add(new JLabel("Generated Password:"));
        panel.add(output);
        panel.add(copyBtn);

        generateBtn.addActionListener(e -> {
            try {
                int len = Integer.parseInt(lengthField.getText());
                java.util.List<Character> pool = new ArrayList<>();
                Random r = new Random();

                if (lower.isSelected()) for (char c = 'a'; c <= 'z'; c++) pool.add(c);
                if (upper.isSelected()) for (char c = 'A'; c <= 'Z'; c++) pool.add(c);
                if (digits.isSelected()) for (char c = '0'; c <= '9'; c++) pool.add(c);
                if (symbols.isSelected()) {
                    String s = "!@#$%^&*()-_=+[]{};:'\",.<>?/\\|`~";
                    for (int i = 0; i < s.length(); i++) pool.add(s.charAt(i));
                }

                if (pool.isEmpty()) {
                    output.setText("⚠️ Select at least one character type.");
                    return;
                }

                StringBuilder pass = new StringBuilder();
                for (int i = 0; i < len; i++) {
                    pass.append(pool.get(r.nextInt(pool.size())));
                }
                output.setText(pass.toString());
            } catch (NumberFormatException ex) {
                output.setText("❌ Invalid length");
            }
        });

        copyBtn.addActionListener(e -> {
            StringSelection selection = new StringSelection(output.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(panel, "Password copied to clipboard! ✅");
        });

        return panel;
    }

    private JPanel createCheckPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPasswordField input = new JPasswordField();
        JButton checkBtn = new JButton("🔍 Check Strength");
        JLabel result = new JLabel("Type a password and press check");
        result.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(new JLabel("Enter Password:"));
        panel.add(input);
        panel.add(checkBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(result);

        checkBtn.addActionListener(e -> {
            String p = new String(input.getPassword());
            int score = 0;
            boolean capital = false, small = false, num = false, symbol = false;
            String symbols = "!@#$%^&*()-_=+[]{};:'\",.<>?/\\|`~";

            if (p.length() >= 8) score++;

            for (int i = 0; i < p.length(); i++) {
                char ch = p.charAt(i);
                if (ch >= 'A' && ch <= 'Z') capital = true;
                else if (ch >= 'a' && ch <= 'z') small = true;
                else if (ch >= '0' && ch <= '9') num = true;
                else if (symbols.indexOf(ch) != -1) symbol = true;
            }

            if (capital) score++;
            if (small) score++;
            if (num) score++;
            if (symbol) score++;

            if (score <= 2) {
                result.setText("❌ Weak Password");
                result.setForeground(Color.RED);
            } else if (score <= 4) {
                result.setText("⚠️ Medium Password");
                result.setForeground(new Color(255, 165, 0)); // orange
            } else {
                result.setText("✅ Strong Password");
                result.setForeground(new Color(0, 128, 0)); // green
            }
        });

        return panel;
    }
}
