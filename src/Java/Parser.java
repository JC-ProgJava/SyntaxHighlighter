import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
  ArrayList<Token> tokens;
  String outCodeHTML = "";
  String outCodeCSS = "";
  String outPlain = "";
  HashMap<String, String[]> styles = new HashMap<>();
  String style;

  public Parser(ArrayList<Token> tokens, String style) {
    this.tokens = tokens;
    styles.put("Normal", new String[]{
      "inherit", // code background-color
      "#003399; font-weight: bold", // keyword
      "black",   // identifier
      "#1740E6", // number
      "#106B10", // string
      "black",   // space
      "black",   // newline
      "black",   // tab
      "#006699", // importname
      "#034524", // headdatatype
      "black",   // otherpunctuation
      "#660E7A", // constant
      "#bf8f1d", // javadoc
      "#808000", // annotation
      "black",   // methodname
      "#106B10", // comment
      "black", // datatype
      "#003399; font-weight: bold", // literal
      "black", // classname
    });
    styles.put("Exotic", new String[]{
      "inherit", // code background-color
      "#136F75", // keyword
      "#C3301E", // identifier
      "#1F5D15", // number
      "#8A2428", // string
      "black",   // space
      "black",   // newline
      "black",   // tab
      "#2E690A", // importname
      "#BF005C", // headdatatype
      "#940066", // otherpunctuation
      "#2E690A", // constant
      "#8A2428", // javadoc
      "#8A2428", // annotation
      "#8A07B3", // methodname
      "#8A2428", // comment
      "#C3301E", // datatype
      "#136F75", // literal
      "#C3301E", // classname
    });
    styles.put("AtomOneDark", new String[]{
      "#282C34; font-family: 'Consolas', monospace", // code background-color
      "#C678DD;", // keyword
      "#abb2bf", // identifier
      "#d19a66", // number
      "#98c379", // string
      "#abb2bf",   // space
      "#abb2bf",   // newline
      "#abb2bf",   // tab
      "#C678DD", // importname
      "#abb2bf", // headdatatype
      "#abb2bf", // otherpunctuation
      "#d19a66", // constant
      "#5c6370; font-style: italic", // javadoc
      "#c678dd", // annotation
      "#61aeee", // methodname
      "#5c6370; font-style: italic", // comment
      "#d19a66", // datatype
      "#56b6c2", // literal
      "#e6c07b", // classname
    });
    this.style = style;
  }

  public String[] parse() {
    String stylesheet = """
        code {
          display: block;
          background-color: %s;
        }
        
        code span {
          display: inline;
          white-space: pre;
        }
                    
        .keyword {
          color: %s;
        }
                    
        .identifier {
          color: %s;
        }
                    
        .number {
          color: %s;
        }
                    
        .string {
          color: %s;
        }
                    
        .space {
          color: %s;
        }
                    
        .newline {
          color: %s;
        }
                    
        .tab {
          color: %s;
        }
                    
        .importname {
          color: %s;
        }
                    
        .headdatatype {
          color: %s;
        }
                    
        .otherpunctuation {
          color: %s;
        }
                    
        .constant {
          color: %s;
        }
                    
        .javadoc {
          color: %s;
        }
                    
        .annotation {
          color: %s;
        }
        
        .methodname {
          color: %s;
        }
        
        .comment {
          color: %s;
        }
        
        .datatype {
          color: %s;
        }
        
        .literal {
          color: %s;
        }
        
        .classname {
          color: %s;
        }
      """;
    outCodeCSS = String.format(stylesheet, (Object[]) styles.get(style));
    outCodeHTML += "<code>";
    for (Token token : tokens) {
      switch (token.type.toString()) {
        case "KEYWORD", "IDENTIFIER", "NUMBER", "STRING",
          "IMPORTNAME", "HEADDATATYPE",
          "OTHERPUNCTUATION", "JAVADOC",
          "ANNOTATION", "CONSTANT", "METHODNAME",
          "COMMENT", "DATATYPE", "LITERAL", "CLASSNAME" -> {
          outCodeHTML += "<span class=\"" + token.type.toString().toLowerCase() + "\">" + cleanse(token.text) + "</span>";
          outPlain += token.text;
        }
        case "NEWLINE" -> {
          outCodeHTML += "<span class=\"" + token.type.toString().toLowerCase() + "\"><br /></span>";
          outPlain += "\n";
        }
        case "TAB" -> {
          outCodeHTML += "<span class=\"" + token.type.toString().toLowerCase() + "\">    </span>";
          outPlain += "\t";
        }
        case "SPACE" -> {
          outCodeHTML += "<span class=\"" + token.type.toString().toLowerCase() + "\"> </span>";
          outPlain += " ";
        }
        default -> System.err.println("Unexpected enum: " + token.type.toString());
      }
    }
    outCodeHTML += "</code>";
    return new String[]{outCodeHTML, outCodeCSS, outPlain};
  }

  private String cleanse(String text) {
    return text.replaceAll("[&]", "&amp;").replaceAll("[ ]", "&nbsp;").replaceAll("[{]", "&lbrace;").replaceAll("[<]", "&lt;").replaceAll("[>]",
      "&gt;");
  }
}
