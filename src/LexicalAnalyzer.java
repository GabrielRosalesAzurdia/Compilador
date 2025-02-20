import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.regex.*;

public class LexicalAnalyzer extends JFrame {
    final private JTextArea textArea;
    final private JTextArea tokensArea;
    final private JFileChooser fileChooser;

    public LexicalAnalyzer() {
        setTitle("Analizador Léxico");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton loadButton = new JButton("Cargar Archivo");
        JButton analyzeButton = new JButton("Ejecutar Análisis Léxico");
        topPanel.add(loadButton);
        topPanel.add(analyzeButton);
        add(topPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        tokensArea = new JTextArea();
        textArea.setEditable(false);
        tokensArea.setEditable(false);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(textArea), new JScrollPane(tokensArea));
        add(splitPane, BorderLayout.CENTER);

        fileChooser = new JFileChooser();

        loadButton.addActionListener(_ -> loadFile());

        analyzeButton.addActionListener(_ -> analyzeText());
    }

    private void loadFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void analyzeText() {
        String text = textArea.getText();
        tokensArea.setText("");

        String tokenPatterns = "(?<ID>[a-zA-Z_][a-zA-Z0-9_]*)|" +
                "(?<NUM>\\d+)|" +
                "(?<STRING>\".*?\")|" +
                "(?<OP>[+\\-*/=<>!&|{}();,])";
        Pattern pattern = Pattern.compile(tokenPatterns);
        Matcher matcher = pattern.matcher(text);

        int tokenCount = 1;
        while (matcher.find()) {
            if (matcher.group("ID") != null) {
                tokensArea.append("TOKEN " + tokenCount++ + " - ID: " + matcher.group("ID") + "\n");
            } else if (matcher.group("NUM") != null) {
                tokensArea.append("TOKEN " + tokenCount++ + " - NUM: " + matcher.group("NUM") + "\n");
            } else if (matcher.group("STRING") != null) {
                tokensArea.append("TOKEN " + tokenCount++ + " - STRING: " + matcher.group("STRING") + "\n");
            } else if (matcher.group("OP") != null) {
                tokensArea.append("TOKEN " + tokenCount++ + " - OP: " + matcher.group("OP") + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LexicalAnalyzer().setVisible(true));
    }
}
