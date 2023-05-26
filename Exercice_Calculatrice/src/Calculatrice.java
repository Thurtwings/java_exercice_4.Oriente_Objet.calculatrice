
/*(pseudo code) Fonction Evaluer(expression):

  Créer une pile pour les opérandes (operandStack)
  Créer une pile pour les opérateurs (operatorStack)

  Pour chaque caractère (c) dans l'expression:

    Si c est un espace, passe au caractère suivant

    Si c est un chiffre, un point, ou un signe '-' suivi d'un chiffre:

      Si c est '-', marque le prochain chiffre comme négatif et passe au chiffre suivant

      Récupère tout le nombre à partir de l'index courant (y compris la partie décimale)

      Converti le nombre en Double et ajoute-le à operandStack

    Sinon, si c est un opérateur connu:

      Tant que la pile operatorStack n'est pas vide et que la priorité de c est inférieure ou égale à la priorité du sommet de operatorStack:
        Exécute l'opération au sommet de operatorStack

      Ajoute c à operatorStack

    Sinon, lance une erreur "Invalid character: c"

  Tant que la pile operatorStack n'est pas vide:
    Exécute l'opération au sommet de operatorStack

  Renvoie le sommet de operandStack comme le résultat final

Fin de la Fonction
*/

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculatrice
{
    // Ok, ici on va mettre en relation nos opérateurs avec les actions qu'ils sont censés effectuer.
    private Map<Character, IOperation> operations;

    public Calculatrice()
    {
        // On initialise nos opérations basiques de calculatrice ici.
        operations = new HashMap<>();
        operations.put('+', new Addition());
        operations.put('-', new Soustraction());
        operations.put('*', new Multiplication());
        operations.put('/', new Division());
    }


    public double Evaluer(String expression)
    {
        // Ces deux piles sont nos outils pour déchiffrer l'expression.
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        // Allez, on se tape toute l'expression caractère par caractère.
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // Si c'est juste un espace, on s'en fout, on passe au suivant.
            if (Character.isWhitespace(c)) {
                continue;
            }

            // Bon, là, on a trouvé un nombre (peut-être négatif ou décimal).
            if (Character.isDigit(c) || c == '.' || (c == '-' && (i+1 < expression.length() && Character.isDigit(expression.charAt(i+1))))) {
                boolean negative = false;
                // Si c'est un '-', le nombre est négatif.
                if (c == '-') {
                    negative = true;
                    i++;
                    c = expression.charAt(i);
                }
                // On récupère tout le nombre, même s'il a une partie décimale.
                int start = i;
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    i++;
                }
                // Ok, on transforme notre nombre en Double et on le stocke.
                double operand = Double.parseDouble(expression.substring(start, i));
                operandStack.push(negative ? -operand : operand);
                i--;
                // Cool, on a un opérateur. On va voir ce qu'on en fait.
            } else if (operations.containsKey(c)) {
                // On dépile et on fait les opérations en attente qui ont une priorité plus grande ou égale.
                while (!operatorStack.isEmpty() && precedence(c) <= precedence(operatorStack.peek())) {
                    performOperation(operandStack, operatorStack);
                }
                // On stocke notre opérateur pour plus tard.
                operatorStack.push(c);
            } else {
                // Quoi ?! On sait pas ce que c'est ce caractère. On arrête tout !
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }

        // On a fini de lire l'expression, on dépile et on fait toutes les opérations restantes.
        while (!operatorStack.isEmpty()) {
            performOperation(operandStack, operatorStack);
        }

        // Et voilà, on a notre résultat. On le renvoie et c'est fini !
        return operandStack.pop();
    }

    // Ici, on va prendre une opération en attente et l'exécuter.
    private void performOperation(Stack<Double> operandStack, Stack<Character> operatorStack) {
        if (operandStack.size() < 2) {
            throw new IllegalArgumentException("Insufficient values for operation");
        }
        // On récupère les deux derniers nombres et l'opérateur.
        double b = operandStack.pop();
        double a = operandStack.pop();
        char op = operatorStack.pop();
        // On fait l'opération et on met le résultat sur la pile.
        operandStack.push(operations.get(op).Apply(a, b));
    }

    // La priorité c'est super important pour savoir dans quel ordre faire les opérations.
    private int precedence(char operator)
    {
        switch (operator) {
            case '+':
            case '-':
                return 1; // Ces deux là sont les plus faibles, ils passent en dernier.
            case '*':
            case '/':
                return 2; // Ceux-là sont plus costauds, ils passent avant.
            default:
                return -1; // Et si on sait pas ce que c'est, on dit -1.
        }
    }
}
