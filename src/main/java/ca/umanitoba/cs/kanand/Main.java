package ca.umanitoba.cs.kanand;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Please enter your name below");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        PrintMap p = new PrintMap();
    }
}