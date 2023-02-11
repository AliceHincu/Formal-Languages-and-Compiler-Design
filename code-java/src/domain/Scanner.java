package domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    private final SymbolTable ST;
    private final ProgramInternalForm PIF;
    private final FiniteAutomata FA;
    private final String localPath = "src\\files\\";
    private final String tokensFileName = "token.in";
    private final List<String> keywords = new ArrayList<>();
    private final List<String> operators = new ArrayList<>();
    private final List<String> separators = new ArrayList<>();

//    Pattern identifier = Pattern.compile("^[a-z]([a-zA-Z_]|[0-9])*$");
//    Pattern constant = Pattern.compile("^(0|[+-]?[1-9][0-9]*)$|^\".\"$|^\".*\"$");
    private final String faIdentifierFile = "fa-identifier.in";
    private final String faIntFile = "fa-int.in";
    private final String faStringFile = "fa-string.in";

    public Scanner(SymbolTable ST, ProgramInternalForm PIF, FiniteAutomata FA) {
        this.ST = ST;
        this.PIF = PIF;
        this.FA = FA;
        try {
            readTokensFile();
        } catch (IOException e) {
            System.err.println("Problem reading " + tokensFileName + "\n Message: " + e.getMessage());
        }
    }

    public void scan(String filename) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(localPath + filename));
        } catch (FileNotFoundException exception) {
            System.err.println("File " + filename + " not found!");
            return;
        }

        String line;
        StringBuilder exceptionMessage = new StringBuilder();
        int lineNumber = 1;

        // Scanning Algorithm
        while ((line = reader.readLine()) != null) {
            List<String> tokens = tokenize(line);
            for (String token : tokens) {
                if (keywords.contains(token) || separators.contains(token) || operators.contains(token)) {
                    if (Objects.equals(token, " "))
                        continue;
                    PIF.add(token, -1);
                } else if (this.isIdentifier(token)) {
                    PIF.add("IDENTIFIER", ST.add(token));
                } else if (this.isConstant(token)) {
                    PIF.add("CONSTANT", ST.add(token));
                } else {
                    exceptionMessage.append("Lexical error at token ").append(token).append(" - at line ").append(lineNumber).append("\n");
                }
            }
            lineNumber += 1;
        }

        String errOut = exceptionMessage.toString();
        if (!errOut.equals("")) {
            System.err.println(errOut);
            ST.deleteContentOfFile();
            PIF.deleteContentOfFile();
        }
        else {
            System.out.println("Lexically correct");
            ST.writeToFile();
            PIF.writeToFile();
        }


    }

    private List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        String token = "";
        int index = 0;
        while (index < line.length()) {
            char ch = line.charAt(index);
            if (this.isPartOfOperator(ch)) {
                // in case there aren't any separators between the last token and this operator
                if (!token.equals(""))
                    tokens.add(token);
                Pair pair = this.getOperatorToken(line, index);
                token = pair.getFirst();
                index = pair.getSecond();
                tokens.add(token);
                token = "";
            } else if (ch == '\"') {
                if (!token.equals(""))
                    tokens.add(token);
                Pair pair = this.getStringToken(line, index);
                token = pair.getFirst();
                index = pair.getSecond();
                tokens.add(token);
                token = "";
            } else if (separators.contains(ch+"")) {
                // add the token that existed before the separator
                if (!token.equals(""))
                    tokens.add(token);
                index = index + 1;
                tokens.add(String.valueOf(ch));
                token = "";
            } else {
                token += String.valueOf(ch);
                index += 1;
            }
        }

        if (!token.equals(""))
            tokens.add(token);

        return tokens;
    }

    /**
     * Checks if a character is part of an operator.
     *
     * @param ch the character to be checked
     * @return true if it's part of an operator. False otherwise.
     */
    private boolean isPartOfOperator(char ch) {
        for (String operator : operators) {
            if (operator.indexOf(ch) >= 0)
                return true;
        }

        return false;
    }

    /**
     * Finds the next token which is an operator.
     *
     * @param line  the current line from the program
     * @param index current position in line
     * @return a Pair where the first element is the resulted token (the operator) and the second element is the resulted index
     */
    private Pair getOperatorToken(String line, Integer index) {
        StringBuilder token = new StringBuilder();

        while (index < line.length() && this.isPartOfOperator(line.charAt(index))) {
            token.append(line.charAt(index));
            index += 1;
        }

        return new Pair(token.toString(), index);
    }

    /**
     * Finds the next token which is a string (between "")
     *
     * @param line  the current line from the program
     * @param index current position in line
     * @return
     */
    private Pair getStringToken(String line, Integer index) {
        StringBuilder token = new StringBuilder();
        int quotes = 0;

        while (index < line.length() && quotes < 2) {
            if (line.charAt(index) == '\"')
                quotes += 1;
            token.append(line.charAt(index));
            index += 1;
        }

        return new Pair(token.toString(), index);
    }

    private boolean isIdentifier(String token) {
//        Matcher matcher = identifier.matcher(token);
//        return matcher.find();
        return FA.isAccepted(faIdentifierFile, token);
    }

    private boolean isConstant(String token) {
        /* Matcher matcher = constant.matcher(token);
        return matcher.find(); */
        return FA.isAccepted(faStringFile, token) || FA.isAccepted(faIntFile, token);
    }

    private void readTokensFile() throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(localPath + tokensFileName));
        } catch (FileNotFoundException exception) {
            System.err.println("File " + tokensFileName + " not found!");
            return;
        }

        String line;

        while (!Objects.equals(line = reader.readLine(), "---")) {
            operators.add(line);
        }
        while (!Objects.equals(line = reader.readLine(), "---")) {
            switch (line) {
                case "<space>" -> separators.add(" ");
                case "<indent>" -> separators.add("\t");
                case "<newline>" -> separators.add("\n");
                default -> separators.add(line);
            }
        }
        while((line = reader.readLine()) != null){
            keywords.add(line);
        }
    }
}
