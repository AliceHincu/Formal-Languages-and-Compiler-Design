package domain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FiniteAutomata {
    List<String> Q = new ArrayList<>(); // states
    List<String> E = new ArrayList<>(); // inputs
    String q0; // initial state
    List<String> F = new ArrayList<>(); // final states
    Map<String, Map<String, List<String>>> S = new HashMap<>(); // transitions

    private final String localPath = "src\\files\\";
    private final String faFileName = "fa1.in";

    public void readFaElementsFromFile() throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(localPath + faFileName));
        } catch (FileNotFoundException exception) {
            System.err.println("File " + faFileName + " not found!");
            return;
        }

        String line;

        Q = getLine(reader);
        E = getLine(reader);
        q0 = getLine(reader).get(0);
        F = getLine(reader);

        reader.readLine(); // S =

        // get all transitions
        String src, input, dest;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("->");
            String left = parts[0].trim();
            left = left.substring(1, left.length() - 1);

            src = left.split(",")[0];
            input = left.split(",")[1];
            dest = parts[1].trim();

            // check if src exists...if not, add source
            if (!S.containsKey(src))
                S.put(src, new HashMap<>());
            // check if input exists...if not, add it
            if (!S.get(src).containsKey(input))
                S.get(src).put(input, new ArrayList<>());
            // check if dest exists...if not, add it
            if (!S.get(src).get(input).contains(dest))
                S.get(src).get(input).add(dest);

        }

        System.out.println(S);
    }

    private List<String> getLine(BufferedReader reader) throws IOException {
        String[] line = reader.readLine().trim().split("=");
        return Arrays.stream(line[1].trim().split(" ")).toList();
    }

    /**
     * In DFA, there is only one path for specific input from the current state to the next state.
     * @return true if is DFA, false otherwise
     */
    public boolean isDFA() {
        for (String src : S.keySet()) {
            Map<String, List<String>> inputs = S.get(src);
            for (String input : inputs.keySet())
                if (inputs.get(input).size() > 1) return false;
        }
        return true;
    }

    /**
     * Go through each symbol from the sequence and check that it can be reached by the given FA's transitions
     * @return the final state if the sequence is accepted, else null.
     */
    public String isAccepted(String sequence) {
        if(!this.isDFA()) return null;

        String currentState = this.q0;
        for(int i=0; i<sequence.length(); i++){
            String input = sequence.charAt(i)+"";
            if(S.containsKey(currentState) && S.get(currentState).containsKey(input))
                currentState = S.get(currentState).get(input).get(0); // get first transition
            else
                return null;
        }
        return F.contains(currentState) ? currentState : null;
    }

    public List<String> getStates() {
        return Q;
    }

    public List<String> getAlphabet() {
        return E;
    }

    public String getInitialState() {
        return q0;
    }

    public List<String> getFinalStates() {
        return F;
    }

    public Map<String, Map<String, List<String>>> getTransitions() {
        return S;
    }

    @Override
    public String toString() {
        return "--- FiniteAutomata from file: " + faFileName + " --- \n" +
                "\tSet of states: " + Q + "\n" +
                "\tAlphabet: " + E + "\n" +
                "\tInitial state: " + q0 + '\n' +
                "\tFinal states: " + F + "\n" +
                "\tTransitions: " + S;
    }
}
