package br.com.poo.atividade2;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author John
 */
public class Atividade2 {

    public static void main(String[] args) {
        List<Pessoa> contribuintes = new ArrayList<>();
        int numContribuintes;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the number of tax payers: ");
        numContribuintes = sc.nextInt();
        
        for(int i = 0 ; i < numContribuintes; i++) {
            sc.nextLine();
            
            System.out.println("Tax payer #" + (i+1) + " data:");
            System.out.print("Individual or company (i/c)? ");
            String opcao = sc.nextLine();
            
            System.out.print("Name: ");
            String nome = sc.nextLine();
            
            System.out.print("Anual income: ");
            double rendaAnual = sc.nextDouble();
            
            switch(opcao){
                case "i" -> {
                    System.out.print("Health expenditures: ");
                    double gastoSaude = sc.nextDouble();
                    contribuintes.add(new PessoaFisica(nome, rendaAnual, gastoSaude));
                }
                case "c" -> {
                    System.out.print("Number of employees: ");
                    int numFuncionarios = sc.nextInt();
                    contribuintes.add(new PessoaJuridica(nome, rendaAnual, numFuncionarios));
                }
            }
            
            contribuintes.get(i).calcImposto();
        }
        
        System.out.println("\nTAXES PAID:");
        double totalImposto = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        for(int i = 0 ; i < numContribuintes; i++){
            String imposto = df.format(contribuintes.get(i).getImposto());
            
            System.out.println(contribuintes.get(i).getNome() + ": $" + 
                               imposto);
            totalImposto += contribuintes.get(i).getImposto();
        }
        
        System.out.println("TOTAL TAXES: $ " + df.format(totalImposto));
        sc.close();
    }
}
