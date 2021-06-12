public class Token {
  String text;
  int line;
  int col;
  TokenType type;

  public Token(String text, int line, int col, TokenType type) {
    this.text = text;
    this.line = line;
    this.col = col;
    this.type = type;
  }

  @Override
  public String toString() {
    return "<" + type.toString() + " " + text + ">";
  }
}
