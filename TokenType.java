public enum TokenType {
  IDENTIFIER, // variables
  KEYWORD, // 'class', etc. also includes data types (i.e., double)
  NUMBER, STRING,
  SPACE, NEWLINE, TAB,
  IMPORTNAME, // the name after 'import' (i.e., java.util.Scanner)
  HEADDATATYPE, // the name of a class who is being insantiated (i.e., BigInteger)
  CONSTANT, // an identifier that is in uppercase (and may contain '_')
  OTHERPUNCTUATION
  // i.e., ';', '()', '{}', '<>', '[]', ',', '*', '='
  //       '+', '-', '/', '%', '!', '~', '&', '|'
  //       '?', ':', '^', '.', '''
}
