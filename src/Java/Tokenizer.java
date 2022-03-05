import java.util.ArrayList;
import java.util.Arrays;

public class Tokenizer {
  private static final ArrayList<Token> tokens = new ArrayList<>();
  private static final ArrayList<String> keywords = new ArrayList<>();
  private static final ArrayList<String> literals = new ArrayList<>();
  private static final ArrayList<String> dataTypes = new ArrayList<>();
  private static final ArrayList<String> classKeywords = new ArrayList<>();
  private static int line = 1;
  private static int col = 1;
  private static int start = 0;
  private static int current = 0;
  private static String sourceCode = "";

  public Tokenizer() {
    String[] keys = new String[]{
      "abstract", "assert", "boolean", "break", "byte", "case",
      "catch", "char", "class", "continue", "const", "default",
      "do", "double", "else", "enum", "exports", "extends", "final",
      "finally", "float", "for", "goto", "if", "implements", "import",
      "instanceof", "int", "interface", "long", "module", "native",
      "new", "non-sealed", "package", "private", "protected", "public",
      "requires", "return", "short", "static", "strictfp", "super",
      "switch", "synchronized", "this", "throw", "throws", "transient",
      "try", "var", "void", "volatile", "while", "yield", "sealed", "record",
      "permits", "System"
    };
    keywords.addAll(Arrays.asList(keys));
    String[] lit = new String[]{
      "true", "false", "null"
    };
    literals.addAll(Arrays.asList(lit));


    String[] dataType = new String[]{
      "Byte", "Short", "Integer", "Long", "Float",
      "Double", "Boolean", "Character"
    };
    dataTypes.addAll(Arrays.asList(dataType));


    String[] classKeys = new String[]{
      "class", "interface", "enum", "extends", "implements"
    };
    classKeywords.addAll(Arrays.asList(classKeys));
  }

  private static char peek() {
    if (current + 1 < sourceCode.length()) {
      return sourceCode.charAt(current + 1);
    } else {
      return '\0';
    }
  }

  private static boolean isAlphabet(char currentChar) {
    return String.valueOf(currentChar).matches("[a-zA-Z_$]");
  }

  private static boolean isIdentifierEnding(char currentChar) {
    return String.valueOf(currentChar).matches("[a-zA-Z_$0-9]");
  }

  private static boolean notAtEnd() {
    return current < sourceCode.length();
  }

  public ArrayList<Token> tokenize(String code) {
    sourceCode = code;
    if (sourceCode.charAt(sourceCode.length() - 1) != '\n') {
      sourceCode += "\n";
    }

    while (notAtEnd()) {
      char currentChar = sourceCode.charAt(current);
      if (isAlphabet(currentChar)) {
        while (notAtEnd() && isIdentifierEnding(peek())) {
          col++;
          current++;
        }
        if (keywords.contains(sourceCode.substring(start, current + 1))) {
          tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.KEYWORD));
        } else if (literals.contains(sourceCode.substring(start, current + 1))) {
          tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.LITERAL));
        } else if (dataTypes.contains(sourceCode.substring(start, current + 1))) {
          tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.DATATYPE));
        } else {
          tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.IDENTIFIER));
        }
      } else {
        if (isNumerical(currentChar)) {
          while (notAtEnd()) {
            if (isNumerical(peek()) || peek() == '.') {
              col++;
              current++;
            } else {
              break;
            }
          }

          tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.NUMBER));
        } else {
          switch (currentChar) {
            case ' ':
            case '\t':
              col++;
              current++;
              continue;
            case '\n':
              tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.NEWLINE));
              line++;
              col = 0;
              break;
            case '@':
              annotation();
              current++;
              tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.ANNOTATION));
              break;
            case '"':
              // Supports multiline strings
              if (peek() == '\"' && peekNext() == '\"') {
                current += 2;
                multilineString();
              } else {
                string();
              }
              current++;
              tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.STRING));
              break;
            case '\'':
              character();
              current++;
              tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.STRING));
              break;
            case '/':
              if (peek() == '/') {
                comment();
                current++;
                tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.COMMENT));
              } else if (peek() == '*') {
                if (peekNext() != '*' || peekAfterNext() == '/') {
                  multilineComment();
                  current++;
                  tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.COMMENT));
                } else {
                  multilineComment();
                  current++;
                  tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.JAVADOC));
                }
              } else {
                tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.OTHERPUNCTUATION));
              }
              break;
            case ';':
            case '(':
            case ')':
            case '{':
            case '}':
            case '<':
            case '>':
            case '[':
            case ']':
            case ',':
            case '*':
            case '=':
            case '+':
            case '-':
            case '%':
            case '!':
            case '~':
            case '&':
            case '|':
            case '?':
            case ':':
            case '^':
            case '.':
              tokens.add(new Token(sourceCode.substring(start, current + 1), line, col, TokenType.OTHERPUNCTUATION));
              break;
            default:
              System.err.println("Could not interpret character: '" + currentChar + "'. [ln: " + line + "]");
              break;
          }
        }
      }
      col++;
      current++;
      start = current;
    }

    for (int index = 0; index < tokens.size(); index++) {
      if (tokens.get(index).text.equals("import") || tokens.get(index).text.equals("package")) {
        int indexStart = index + 1;
        while (!tokens.get(indexStart).text.equals(";")) {
          Token token = tokens.get(indexStart);
          tokens.set(indexStart, new Token(token.text, token.line, token.col, TokenType.IMPORTNAME));
          indexStart++;
        }
      } else if (tokens.get(index).type == TokenType.IDENTIFIER || tokens.get(index).text.matches("[])}]")) {
        current = tokens.get(index).col;
        if (tokens.size() > index + 1 && tokens.get(index + 1).text.equals(".")) {
          int indexStart = index + 1;
          while (tokens.get(indexStart).type != TokenType.OTHERPUNCTUATION || tokens.get(indexStart).text.equals(".")) {
            if (tokens.get(indexStart).text.equals(".")) {
              indexStart++;
              continue;
            }
            Token token = tokens.get(indexStart);
            tokens.set(indexStart, new Token(token.text, token.line, token.col, TokenType.HEADDATATYPE));
            indexStart++;
          }
        }
      }

      if ((tokens.get(index).type == TokenType.IDENTIFIER || tokens.get(index).type == TokenType.HEADDATATYPE) && tokens.get(index + 1).text.equals("(")) {
        Token token = tokens.get(index);
        tokens.set(index, new Token(token.text, token.line, token.col, TokenType.METHODNAME));
      }

      if (index - 1 > 0 && classKeywords.contains(tokens.get(index - 1).text)) {
        while (tokens.get(index).type == TokenType.SPACE || tokens.get(index).type == TokenType.NEWLINE || tokens.get(index).type == TokenType.TAB) {
          index++;
        }
        Token token = tokens.get(index);
        tokens.set(index, new Token(token.text, token.line, token.col, TokenType.CLASSNAME));
      }

      if (isUppercase(tokens.get(index).text) && (tokens.get(index).type == TokenType.HEADDATATYPE || tokens.get(index).type == TokenType.IDENTIFIER)) {
        Token token = tokens.get(index);
        tokens.set(index, new Token(token.text, token.line, token.col, TokenType.CONSTANT));
      }
    }

    return tokens;
  }

  private char peekAfterNext() {
    if (current + 3 < sourceCode.length()) {
      return sourceCode.charAt(current + 3);
    } else {
      return '\0';
    }
  }

  private void annotation() {
    while (notAtEnd()) {
      if (peek() == ' ') {
        break;
      } else if (peek() == '\n') {
        line++;
        col = 1;
        break;
      }
      col++;
      current++;
    }
  }

  private void multilineString() {
    while (notAtEnd()) {
      if (sourceCode.charAt(current) == '"' && peek() == '"' && peekNext() == '"') {
        if ((current - 1 >= 0 && sourceCode.charAt(current - 1) != '\\') || current == 0) {
          break;
        }
      } else if (peek() == '\n') {
        line++;
        col = 1;
      }
      col++;
      current++;
    }
    col++;
    current++;
  }

  private void multilineComment() {
    while (notAtEnd()) {
      if (peek() == '*' && peekNext() == '/') {
        break;
      } else if (peek() == '\n') {
        line++;
        col = 1;
      }
      col++;
      current++;
    }
    col++;
    current++;
  }

  private char peekNext() {
    if (current + 2 < sourceCode.length()) {
      return sourceCode.charAt(current + 2);
    } else {
      return '\0';
    }
  }

  private boolean isUppercase(String text) {
    return text.matches("^[A-Z_$][A-Z_$0-9]*$");
  }

  private void comment() {
    while (notAtEnd() && peek() != '\n') {
      col++;
      current++;
    }
  }

  private void character() {
    while (notAtEnd() && (peek() != '\'' || sourceCode.charAt(current) == '\\')) {
      if (sourceCode.charAt(current) == '\\') {
        col++;
        current++;
        if (peek() == '\'') {
          break;
        }
        continue;
      }
      col++;
      current++;
    }
  }

  private void string() {
    while (notAtEnd() && (peek() != '"' || sourceCode.charAt(current) == '\\')) {
      if (sourceCode.charAt(current) == '\\') {
        col++;
        current++;
        if (peek() == '"') {
          break;
        }
        continue;
      }
      col++;
      current++;
    }
  }

  private boolean isNumerical(char currentChar) {
    return String.valueOf(currentChar).matches("[0-9]");
  }
}
