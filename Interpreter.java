
import sun.misc.Queue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Umar on 25-Apr-16.
 */
public class Interpreter {

    HashMap<String, Integer> variables;
    String fname;

    public Interpreter(String filename) {
        variables = new HashMap<>();
        fname = filename;

    }

    void interpret() throws InterruptedException {

        FileReader fr = null;
        try {
            fr = new FileReader(fname);
            BufferedReader tr = new BufferedReader(fr);
            String sCurrentLine;
            int line = 1;
            while ((sCurrentLine = tr.readLine()) != null) {
                String[] splitted = sCurrentLine.split(" +");
                /**
                 * If a variable declaration appears
                 *
                 */
                if (splitted[0].equals("var")) {
                    if (isNumeric(splitted[1])) {
                        System.out.println("Syntax Error Variable cannot be a Number@ Line " + line);
                        System.exit(-1);
                    }
                    String[] s2 = sCurrentLine.split("var");
                    String g = s2[1].trim();
                    s2 = g.split("=");
                    if (s2.length == 1) {
                        System.out.println("Syntax Error Value Not assigned @ Line " + line);
                        System.exit(-1);
                    }
                    variables.put(s2[0].trim(), Integer.parseInt(s2[1].trim()));
                }
                /**
                 * If a print statement is encountered
                 */
                else if (splitted[0].equals("print")) {
                    // If only print is given variable or expression missing
                    if (sCurrentLine.split(" +").length == 1) {
                        System.out.println("Syntax Error Nothing to Print @ Line " + line);
                        System.exit(-1);
                    }
                    String[] c = sCurrentLine.split("print");
                    String k = c[1].trim();
                    if (k.length() == 1) {
                        if (variables.get(k) == null) {
                            System.out.println("Variable not declared Peror @ Line " + line);
                            System.exit(-1);

                        } else
                            System.out.println(variables.get(k));

                    } else {
                        System.out.println(evaluate(k, line) + "");
                    }
                }
                line++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int evaluate(String evaluate, int line) throws InterruptedException {

        Stack<String> ops = new Stack<>();
        Stack<Integer> vals = new Stack<>();
        evaluate = evaluate.replace(")", " ) ");
        evaluate = evaluate.replace("(", "( ");
        evaluate = evaluate.replace("+", " + ");
        evaluate = evaluate.replace("*", " * ");
        evaluate = evaluate.replace("-", " - ");
        evaluate = evaluate.replace("/", " / ");
        String[] exp = evaluate.split(" +");

        for (int i = 0; i < exp.length; i++) {
//            System.out.print(exp[i]);
            String s = exp[i];
            if (s.equals("(") || s.equals(" ") ) {
            } else if (s.equals("+") || s.equals("*") || s.equals("/") || s.equals("-")) {
                if (i!=0 ){
                    String j= exp[i-1];
                    if (j.equals("+") || j.equals("*") || j.equals("/") || j.equals("-")){
                        System.out.println(" Expression Error @ Line " + line );
                        System.exit(-1);
                    }
                }
                else if (i==0 ){
                    if (s.equals("+") || s.equals("*") || s.equals("/") || s.equals("-")){
                        System.out.println(" Expression Error @ Line " + line );
                        System.exit(-1);
                    }
                }
                ops.push(s);
//                ops.push(s);
            } else if (s.equals(")")) {
                getComp(ops, vals, line);
            } else {
                if (isNumeric(s))
                    vals.push(Integer.parseInt(s));
                else {
                    if (variables.get(s) == null) {
                        System.out.println("Variable not declared Eerror @ Line " + line );
                        System.exit(-1);
                    } else
                        vals.push(variables.get(s));
                }
            }
        }

        while (!ops.isEmpty()){
            getComp(ops, vals, line);
//            System.out.println(ops.size());
        }
        int val=vals.pop();
        if (vals.empty())
        return val;
        else {
            System.out.println("Incomplete Expression Eerror @ Line " + line );
            System.exit(-1);
            return val;
        }
    }

    public boolean isNumeric(String s) {
        return s.matches("[+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)");
    }

    private static void getComp(Stack<String> ops, Stack<Integer> vals, int line) throws InterruptedException {
        String op = ops.pop();

        int y = vals.pop(), x;

        x = vals.pop();
        if (op.equals("+")) {
            vals.push(x + y);
        } else if (op.equals("*")) {
            vals.push(x * y);
        } else if (op.equals("/")) {
            vals.push(x / y);
        } else if (op.equals("-")) {
            vals.push(x - y);
        }

    }

    public static void main(String[] args) throws InterruptedException {

        String s = "x+y/z";
        String[] x = s.split("[-+*/]");
//        System.out.println(x.length);
        Interpreter in = new Interpreter("C:\\Users\\Umar\\IdeaProjects\\Lab9\\src\\test.txt");
        in.interpret();
        // System.out.println(in.variables.toString());
    }
}
