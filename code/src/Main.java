import domain.FiniteAutomata;
import domain.ProgramInternalForm;
import domain.Scanner;
import domain.SymbolTable;
import ui.UI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SymbolTable ST = new SymbolTable();
        ProgramInternalForm PIF = new ProgramInternalForm();
        Scanner scanner = new Scanner(ST, PIF);
//        scanner.scan("p1.txt");
        FiniteAutomata FA = new FiniteAutomata();
        FA.readFaElementsFromFile();
        UI ui = new UI(ST, PIF, scanner, FA);
        ui.run();
    }
}