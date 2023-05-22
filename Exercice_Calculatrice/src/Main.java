import java.io.*;
import java.text.DecimalFormat;
public class Main {
    public static void main(String[] args) {
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";
        DecimalFormat df = new DecimalFormat("#.##");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            String line;
            Calculatrice calculatrice = new Calculatrice();

            while ((line = reader.readLine()) != null) {
                try {
                    double resultat = calculatrice.Evaluer(line);  // ici, le calcul est effectué
                    writer.write(line + " = " + df.format(resultat));
                } catch (ArithmeticException e) {
                    writer.write(line + " = " + e.getMessage());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
