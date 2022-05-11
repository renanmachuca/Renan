package client;

import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

public class Jokenpô {
    
    public static void main (String[]args) {
        int jogador,computador;
        Scanner teclado = new Scanner(System.in);
        System.out.println("JokenPô");
        System.out.println("");
        System.out.println("1.Pedra");
        System.out.println("2.Papel");
        System.out.println("3.Tesoura");
        System.out.print("Digite a opção desejada");
        
    //Logica do jogador
        jogador = teclado.nextInt();
        System.out.println("");
        teclado.close();
        switch(jogador){
        case 1:
            System.out.println("Jogador escolheu PEDRA");
            break;
    
        case 2:
        System.out.println("Jogador escolheu PAPEL");
        break;
    
        case 3:
    System.out.println("Jogador escolheu TESOURA");
    break;
    default:
    System.out.println("opção inválida");

}
    //lógica do computador
    computador = (int)(Math.random()*3 + 1);
    teclado.close();
    switch(computador){
        case 1:
            System.out.println("CPU escolheu PEDRA");
            break;
    
        case 2:
        System.out.println("CPU escolheu PAPEL");
        break;
    
        case 3:
    System.out.println("CPU escolheu TESOURA");
    break;
    }
    //lógica para determinar o vencedor
    System.out.println("");
    if(jogador == computador) {
        System.out.println("Empate");
    } else {
        if ((jogador == 1 && computador == 3) || (jogador == 2 && computador == 1) || (jogador == 3 && computador == 2)){
            System.out.println("Jogador venceu!");
            
        } else {
            System.out.println("CPU venceu!");
        }
    }

    }
    
}
