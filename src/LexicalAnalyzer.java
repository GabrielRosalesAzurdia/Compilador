import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Set;
import java.util.regex.*;

public class LexicalAnalyzer extends JFrame {
    final private JTextArea textArea;
    final private JTextArea tokensArea;
    final private JFileChooser fileChooser;
    private final SymbolTable symbolTable = new SymbolTable();
    private final JTextArea symbolTableArea = new JTextArea();


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
        symbolTableArea.setEditable(false);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(textArea), new JScrollPane(tokensArea));
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, new JScrollPane(symbolTableArea));
        add(splitPane, BorderLayout.CENTER);
        add(mainSplitPane, BorderLayout.CENTER);

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

        // Lista de palabras reservadas en Java
        Set<String> keywords = getStrings();

        // Expresión regular mejorada
        String tokenPatterns = "(?<KEYWORD>\\b(?:abstract|assert|boolean|break|byte|case|catch|char|class|const|"
                + "continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|"
                + "instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|"
                + "strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while|String)\\b)|"
                + "(?<ID>[a-zA-Z_][a-zA-Z0-9_]*)|"
                + "(?<NUM>\\d+(\\.\\d+)?)|"
                + "(?<STRING>\"(\\\\.|[^\"])*\")|"
                + "(?<OP>[+\\-*/=<>!&|{}();,])";

        Pattern pattern = Pattern.compile(tokenPatterns);
        Matcher matcher = pattern.matcher(text);

        int tokenCount = 1;
        while (matcher.find()) {
            if (matcher.group("KEYWORD") != null) {
                tokensArea.append("TOKEN " + tokenCount++ + " - KEYWORD: " + matcher.group("KEYWORD") + "\n");
            } else if (matcher.group("ID") != null) {
                // Verificar si es palabra reservada
                String identifier = matcher.group("ID");
                if (keywords.contains(identifier)) {
                    tokensArea.append("TOKEN " + tokenCount++ + " - KEYWORD: " + identifier + "\n");
                } else {
                    tokensArea.append("TOKEN " + tokenCount++ + " - ID: " + identifier + "\n");
                    if (!symbolTable.contains(identifier)) {
                        symbolTable.addSymbol(identifier, "Desconocido", "Global", "Variable");
                    }
                }
            } else if (matcher.group("NUM") != null) {
                tokensArea.append("TOKEN " + tokenCount++ + " - NUM: " + matcher.group("NUM") + "\n");
            } else if (matcher.group("STRING") != null) {
                tokensArea.append("TOKEN " + tokenCount++ + " - STRING: " + matcher.group("STRING") + "\n");
            } else if (matcher.group("OP") != null) {
                tokensArea.append("TOKEN " + tokenCount++ + " - OP: " + matcher.group("OP") + "\n");
            }
        }
        symbolTableArea.setText(symbolTable.toString());
    }

    private static Set<String> getStrings() {
        String[] keywordsArray = {
                "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
                "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
                "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
                "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
                "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
                "volatile", "while", "String"
        };

        // Convertir a un conjunto para búsqueda rápida
        return new java.util.HashSet<>(java.util.Arrays.asList(keywordsArray));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LexicalAnalyzer().setVisible(true));
    }
}
