import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class SyntaxHighlighter {
  public static void main(String[] args) {
    System.out.print("Import source filepath: ");
    Scanner input = new Scanner(System.in);
    String filepath = input.nextLine();
    try {
      String code = new String(Files.readAllBytes(Path.of(filepath)));
      ArrayList<Token> tokens = new ArrayList<>(new Tokenizer().tokenize(code));

      StringBuilder out = new StringBuilder();
      for (Token token : tokens) {
        out.append(token.text);
      }

      System.out.println("If the below tests output false, please file an issue in the Github repository.");
      System.out.println("Input matches output: " + out.toString().equals(code));
      Parser parser = new Parser(tokens);
      String[] output = parser.parse();
      System.out.println("Parser matches input: " + output[2].equals(code));
      System.out.println("-".repeat(40));

      String codeHTMLAndCSS = """
              <!DOCTYPE html>
              <html>
              <head>
              <title>Page Title</title>
              <style>""" +
              output[1] +
              """
                      </style>
                      </head>
                      <body>
                      """
              + output[0] +
              """
                      </body>
                      </html>
                      """;
      System.out.print(codeHTMLAndCSS);

      FileWriter writer = new FileWriter("index.html");
      writer.write(codeHTMLAndCSS);
      writer.close();
    } catch (IOException e) {
      System.err.println("Could not read filepath '" + filepath + "'.");
    }
  }
}
