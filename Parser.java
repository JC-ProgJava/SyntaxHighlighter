import java.util.ArrayList;

public class Parser {
  ArrayList<Token> tokens;
  String outCodeHTML = "";
  String outCodeCSS = "";
  String outPlain = "";

  public Parser(ArrayList<Token> tokens) {
    this.tokens = tokens;

  }

  public String[] parse() {
    outCodeCSS = """
            span {
              display: inline;
              white-space: pre;
            }
                        
            .keyword {
              color: #003399;
              font-weight: bold;
            }
                        
            .identifer {
              color: black;
            }
                        
            .number {
              color: #1740E6;
            }
                        
            .string {
              color: #106B10;
            }
                        
            .space {
              color: black;
            }
                        
            .newline {
              color: black;
            }
                        
            .tab {
              color: black;
            }
                        
            .importname {
              color: #006699;
            }
                        
            .headdatatype {
              color: #034524;
            }
                        
            .otherpunctuation {
              color: black;
            }
                        
            .constant {
              color: #660E7A;
            }
            """;
    outCodeHTML += "<code>";
    for (Token token : tokens) {
      switch (token.type.toString()) {
        case "KEYWORD", "IDENTIFIER", "NUMBER", "STRING", "IMPORTNAME", "HEADDATATYPE", "OTHERPUNCTUATION" -> {
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
      }
    }
    outCodeHTML += "</code>";
    return new String[]{outCodeHTML, outCodeCSS, outPlain};
  }

  private String cleanse(String text) {
    return text.replaceAll("[&]", "&amp;").replaceAll("[ ]", "&nbsp;").replaceAll("[{]", "&lbrace;").replaceAll("[<]", "&lt;").replaceAll("[>]", "&gt;");
  }
}
