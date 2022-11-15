package ui;

import domain.FiniteAutomata;
import domain.ProgramInternalForm;
import domain.Scanner;
import domain.SymbolTable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UI {
    Map<String, Runnable> commands = new HashMap<>();
    SymbolTable ST;
    ProgramInternalForm PIF;
    Scanner scanner;
    FiniteAutomata FA;
    boolean isRunning = true;
    java.util.Scanner keyboard = new java.util.Scanner(System.in);

    public UI(SymbolTable ST, ProgramInternalForm PIF, Scanner scanner, FiniteAutomata fa) {
        this.ST = ST;
        this.PIF = PIF;
        this.scanner = scanner;
        this.FA = fa;
        populateCommandsMap();
    }

    public void run() {
        printMenu();

        while(isRunning) {
            System.out.println(">> Command: ");
            commands.get(keyboard.next()).run();
        }
    }

    private void printMenu() {
        System.out.println(
                """
                0. Exit
                ---- Scanner ----
                1. -
                ---- FA ----
                2. Display the set of states
                3. Display the alphabet
                4. Display all the transitions
                5. Display the initial state
                6. Display the set of final states
                7. Display 2-7 together
                8. Check if sequence is accepted
                """);

    }

    private void populateCommandsMap() {
        commands.put("0", this::stopProgram);
        commands.put("2", () -> System.out.println(FA.getStates()));
        commands.put("3", () -> System.out.println(FA.getAlphabet()));
        commands.put("4", () -> System.out.println(FA.getTransitions()));
        commands.put("5", () -> System.out.println(FA.getInitialState()));
        commands.put("6", () -> System.out.println(FA.getFinalStates()));
        commands.put("7", () -> System.out.println(FA.toString()));
        commands.put("8", this::checkIfSequenceIsAccepted);
    }

    private void checkIfSequenceIsAccepted() {
        System.out.println("Enter sequence: ");
        String sequence = keyboard.next();
        String result = FA.isAccepted(sequence);
        if(result != null)
            System.out.println("Sequence is accepted...Final state achieved: " + result);
        else
            System.out.println("Sequence is not accepted");
    }

    private void stopProgram() {
        this.isRunning = false;
    }
}
