public enum TokenType {
  IDENTIFIER, // variables
  KEYWORD, // 'class', etc. also includes data types (i.e., double)
  NUMBER, STRING,
  SPACE, NEWLINE, TAB,
  IMPORTNAME, // the name after 'import' (i.e., java.util.Scanner)
  HEADDATATYPE, // the function name after an identifier or class name
  CONSTANT, // an identifier that is in uppercase (and may contain '_')
  OTHERPUNCTUATION,
  // i.e., ';', '()', '{}', '<>', '[]', ',', '*', '='
  //       '+', '-', '/', '%', '!', '~', '&', '|'
  //       '?', ':', '^', '.', '''
  JAVADOC, // javadoc comments (highlighted in orange by default)
  ANNOTATION // annotations (i.e., @Override)
}
