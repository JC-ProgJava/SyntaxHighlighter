public enum TokenType {
  IDENTIFIER, // variables, other names that usually appear black in editors.
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
  ANNOTATION, // annotations (i.e., @Override)
  METHODNAME, // i.e., add() {} ('add')
  COMMENT, // i.e., '//' or '/**/'
  CLASSNAME, // i.e., class Head {} ('Head')
  DATATYPE,  // i.e., Short, Double, String
  LITERAL // i.e., null, true, false
}
