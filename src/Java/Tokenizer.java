import java.util.ArrayList;

public class Tokenizer {
  private static int line = 1;
  private static int col = 0;
  private static int start = 0;
  private static int current = 0;
  private static ArrayList<Token> tokens = new ArrayList<>();
  private static String sourceCode = "";
  private static ArrayList<String> keywords = new ArrayList<>();

  public Tokenizer() {
    keywords.add("abstract");
    keywords.add("assert");
    keywords.add("boolean");
    keywords.add("break");
    keywords.add("byte");
    keywords.add("case");
    keywords.add("catch");
    keywords.add("char");
    keywords.add("class");
    keywords.add("continue");
    keywords.add("const");
    keywords.add("default");
    keywords.add("do");
    keywords.add("double");
    keywords.add("else");
    keywords.add("enum");
    keywords.add("exports");
    keywords.add("extends");
    keywords.add("final");
    keywords.add("finally");
    keywords.add("float");
    keywords.add("for");
    keywords.add("goto");
    keywords.add("if");
    keywords.add("implements");
    keywords.add("import");
    keywords.add("instanceof");
    keywords.add("int");
    keywords.add("interface");
    keywords.add("long");
    keywords.add("module");
    keywords.add("native");
    keywords.add("new");
    keywords.add("non-sealed");
    keywords.add("package");
    keywords.add("private");
    keywords.add("protected");
    keywords.add("public");
    keywords.add("requires");
    keywords.add("return");
    keywords.add("short");
    keywords.add("static");
    keywords.add("strictfp");
    keywords.add("super");
    keywords.add("switch");
    keywords.add("synchronized");
    keywords.add("this");
    keywords.add("throw");
    keywords.add("throws");
    keywords.add("transient");
    keywords.add("try");
    keywords.add("var");
    keywords.add("void");
    keywords.add("volatile");
    keywords.add("while");
    keywords.add("yield");
    keywords.add("sealed");
    keywords.add("record");
    keywords.add("permits");

    // literals
    keywords.add("true");
    keywords.add("false");
    keywords.add("null");
    keywords.add("System");
  }

  private static char peek() {
    return sourceCode.charAt(current + 1);
  }

  private static boolean isAlphabet(char currentChar) {
    return String.valueOf(currentChar).matches("[a-zA-Z_$]");
  }

  private static boolean isIdentifierEnding(char currentChar) {
    return String.valueOf(currentChar).matches("[a-zA-Z_$0-9]");
  }

  private static boolean isAtEnd() {
    return current >= sourceCode.length();
  }

  public ArrayList<Token> tokenize(String code) {
    sourceCode = code;
    while (!isAtEnd()) {
      char currentChar = sourceCode.charAt(current);
      if (isAlphabet(currentChar)) {
        while (!isAtEnd() && isIdentifierEnding(peek())) {
          col++;
          current++;
        }
        if (keywords.contains(code.substring(start, current + 1))) {
          tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.KEYWORD));
        } else {
          tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.IDENTIFIER));
        }
        col++;
        current++;
        start = current;
      } else {
        if (isNumerical(currentChar)) {
          while (!isAtEnd()) {
            if (isNumerical(peek()) || peek() == '.') {
              col++;
              current++;
            } else {
              break;
            }
          }

          tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.NUMBER));
        } else {
          switch (currentChar) {
            case ' ':
              tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.SPACE));
              break;
            case '\n':
              tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.NEWLINE));
              line++;
              col = -1;
              break;
            case '\t':
              tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.TAB));
              break;
            case '@':
              annotation();
              current++;
              tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.ANNOTATION));
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
              tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.STRING));
              break;
            case '\'':
              character();
              current++;
              tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.STRING));
              break;
            case '/':
              if (peek() == '/') {
                comment();
                current++;
                tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.STRING));
              } else if (peek() == '*') {
                if (peekNext() != '*') {
                  multilineComment();
                  current++;
                  tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.STRING));
                } else {
                  multilineComment();
                  current++;
                  tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.JAVADOC));
                }
              } else {
                tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.OTHERPUNCTUATION));
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
              tokens.add(new Token(code.substring(start, current + 1), line, col, TokenType.OTHERPUNCTUATION));
              break;
            default:
              System.err.println("Could not interpret character: '" + currentChar + "'. [ln: " + line + "]");
              break;
          }

        }
        col++;
        current++;
        start = current;
      }
    }

    for (int index = 0; index < tokens.size(); index++) {
      if (tokens.get(index).text.equals("import") || tokens.get(index).text.equals("package")) {
        int indexStart = index + 1;
        while (!tokens.get(indexStart).text.equals(";")) {
          Token token = tokens.get(indexStart);
          tokens.add(indexStart, new Token(token.text, token.line, token.col, TokenType.IMPORTNAME));
          tokens.remove(indexStart + 1);
          indexStart++;
        }
      } else if (tokens.get(index).type == TokenType.IDENTIFIER || tokens.get(index).text.matches("[])}]")) {
        current = index;
        if (!isAtEnd() && tokens.size() > index + 1 && tokens.get(index + 1).text.equals(".")) {
          int indexStart = index + 1;
          while (tokens.get(indexStart).type != TokenType.OTHERPUNCTUATION || tokens.get(indexStart).text.equals(".")) {
            if (tokens.get(indexStart).text.equals(".")) {
              indexStart++;
              continue;
            }
            Token token = tokens.get(indexStart);
            tokens.add(indexStart, new Token(token.text, token.line, token.col, TokenType.HEADDATATYPE));
            tokens.remove(indexStart + 1);
            indexStart++;
          }
        }
      }

      if (isUppercase(tokens.get(index).text) && (tokens.get(index).type == TokenType.HEADDATATYPE || tokens.get(index).type == TokenType.IDENTIFIER)) {
        Token token = tokens.get(index);
        tokens.add(index, new Token(token.text, token.line, token.col, TokenType.CONSTANT));
        tokens.remove(index + 1);
      }
    }

    return tokens;
  }

  private void annotation() {
    while (!isAtEnd()) {
      if (peek() == ' ') {
        break;
      } else if (peek() == '\n') {
        line++;
        break;
      }
      col++;
      current++;
    }
  }

  private void multilineString() {
    while (!isAtEnd()) {
      if (sourceCode.charAt(current) == '"' && peek() == '"' && peekNext() == '"') {
        if ((current - 1 >= 0 && sourceCode.charAt(current - 1) != '\\') || current == 0) {
          break;
        }
      } else if (peek() == '\n') {
        line++;
      }
      col++;
      current++;
    }
    col++;
    current++;
  }

  private void multilineComment() {
    while (!isAtEnd()) {
      if (peek() == '*' && peekNext() == '/') {
        break;
      } else if (peek() == '\n') {
        line++;
      }
      col++;
      current++;
    }
    col++;
    current++;
  }

  private char peekNext() {
    return sourceCode.charAt(current + 2);
  }

  private boolean isUppercase(String text) {
    return text.matches("^[A-Z_$][A-Z_$0-9]*$");
  }

  private void comment() {
    while (!isAtEnd() && peek() != '\n') {
      col++;
      current++;
    }
  }

  private void character() {
    while (!isAtEnd() && (peek() != '\'' || (sourceCode.charAt(current) == '\\' && sourceCode.charAt(current - 1) != '\\'))) {
      col++;
      current++;
    }
  }

  private void string() {
    while (!isAtEnd() && (peek() != '"' || (sourceCode.charAt(current) == '\\' && sourceCode.charAt(current - 1) != '\\'))) {
      col++;
      current++;
    }
  }

  private boolean isNumerical(char currentChar) {
    return String.valueOf(currentChar).matches("[0-9]");
  }
}
