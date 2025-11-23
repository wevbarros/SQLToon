import lexer.*;
import parser.*;
import ast.*;
import core.*;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("MiniSQL (TOON Edition)");
        System.out.println("Digite comandos SQL e finalize com ponto-e-vírgula ';'");
        System.out.println("Use CTRL+C para sair.");
        System.out.println();

        Database db = new Database("data");
        Executor exec = new Executor(db);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder buffer = new StringBuilder();

        while (true) {
            System.out.print("sql> ");
            String line = reader.readLine();

            if (line == null) {
                System.out.println("\nEncerrando...");
                break;
            }

            buffer.append(line).append("\n");

            // Só executa quando o comando termina com ';'
            if (!line.trim().endsWith(";"))
                continue;

            String sql = buffer.toString();
            buffer.setLength(0);

            try {
                Lexer lexer = new Lexer(sql);
                Parser parser = new Parser(lexer);

                while (true) {
                    Statement stmt = parser.parseStatement();
                    if (stmt == null) break;

                    Object result = exec.execute(stmt);

                    if (result instanceof List) {
                        List<Map<String,Object>> rows = (List<Map<String,Object>>) result;

                        if (rows.isEmpty()) {
                            System.out.println("(0 linhas)");
                        } else {
                            for (var r : rows)
                                System.out.println(r);
                        }
                    } else {
                        System.out.println("Ok.");
                    }
                }

            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

            System.out.println();
        }
    }
}
