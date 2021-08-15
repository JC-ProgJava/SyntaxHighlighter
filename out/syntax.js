const TokenType=Object.freeze({IDENTIFIER:"IDENTIFIER",KEYWORD:"KEYWORD",NUMBER:"NUMBER",STRING:"STRING",SPACE:"SPACE",NEWLINE:"NEWLINE",TAB:"TAB",IMPORTNAME:"IMPORTNAME",HEADDATATYPE:"HEADDATATYPE",CONSTANT:"CONSTANT",OTHERPUNCTUATION:"OTHERPUNCTUATION",JAVADOC:"JAVADOC",ANNOTATION:"ANNOTATION",METHODNAME:"METHODNAME",COMMENT:"COMMENT",CLASSNAME:"CLASSNAME",DATATYPE:"DATATYPE",LITERAL:"LITERAL"});class Parser{tokens=[];outCodeHTML="";stylesheet="";outPlain="";styles={};style;constructor(t,e){this.tokens=t,this.styles.Normal=["rgb(244, 242, 240)","#003399; font-weight: bold","black","#1740E6","#106B10","black","black","black","#006699","#034524","black","#660E7A","#bf8f1d","#808000","black","#106B10","black","#003399; font-weight: bold","black"],this.styles.Exotic=["inherit","#136F75; font-weight: bold","#C3301E","#1F5D15","#8A2428","black","black","black","#2E690A","#BF005C","#940066","#2E690A","#8A2428","#8A2428","#8A07B3","#8A2428","#C3301E","#136F75; font-weight: bold","#C3301E"],this.styles.AtomOneDark=["#282C34","#C678DD; font-weight: normal","#abb2bf","#d19a66","#98c379","#abb2bf","#abb2bf","#abb2bf","#C678DD","#abb2bf","#abb2bf","#d19a66","#5c6370; font-style: italic","#c678dd","#61aeee","#5c6370; font-style: italic","#d19a66","#56b6c2; font-weight: normal","#e6c07b"],this.style=e,this.stylesheet="code {\n                           display: block;\n                           background-color: %s;\n                           transition: background-color 200ms;\n                         }\n\n                         span {\n                           display: inline;\n                           white-space: pre;\n                         }\n\n                         .keyword {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .identifier {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .number {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .string {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .space {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .newline {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .tab {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .importname {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .headdatatype {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .otherpunctuation {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .constant {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .javadoc {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .annotation {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .methodname {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .comment {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .datatype {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .literal {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n\n                         .classname {\n                           color: %s;\n                           transition: color 200ms;\n                         }\n                       "}parse(){for(let t=0;t<this.styles[this.style].length;t++)this.stylesheet=this.stylesheet.replace("%s",this.styles[this.style][t]);for(let e=0;e<this.tokens.length;e++){var t=this.tokens[e];switch(t.type){case"IDENTIFIER":case"KEYWORD":case"NUMBER":case"STRING":case"IMPORTNAME":case"HEADDATATYPE":case"OTHERPUNCTUATION":case"JAVADOC":case"ANNOTATION":case"CONSTANT":case"METHODNAME":case"COMMENT":case"DATATYPE":case"LITERAL":case"CLASSNAME":this.outCodeHTML+='<span class="'+String(t.type).toLowerCase()+'">'+this.cleanse(t.text)+"</span>",this.outPlain+=t.text;break;case"NEWLINE":this.outCodeHTML+='<span class="'+String(t.type).toLowerCase()+'"><br /></span>',this.outPlain+="\n";break;case"TAB":this.outCodeHTML+='<span class="'+String(t.type).toLowerCase()+'">    </span>',this.outPlain+="\t";break;case"SPACE":this.outCodeHTML+='<span class="'+String(t.type).toLowerCase()+'"> </span>',this.outPlain+=" ";break;default:console.log("Unexpected enum: "+String(t.type))}}return[this.outCodeHTML,this.stylesheet,this.outPlain]}cleanse(t){return t.replaceAll("&","&amp;").replaceAll(" ","&nbsp;").replaceAll("{","&lbrace;").replaceAll("<","&lt;").replaceAll(">","&gt;")}}class Token{constructor(t,e,s,n){this.text=t,this.line=e,this.col=s,this.type=n}}class Tokenizer{tokens=[];keywords=[];literals=[];dataTypes=[];classKeywords=[];line=1;col=1;start=0;current=0;sourceCode="";constructor(){this.keywords=["abstract","assert","boolean","break","byte","case","catch","char","class","continue","const","default","do","double","else","enum","exports","extends","final","finally","float","for","goto","if","implements","import","instanceof","int","interface","long","module","native","new","non-sealed","package","private","protected","public","requires","return","short","static","strictfp","super","switch","synchronized","this","throw","throws","transient","try","var","void","volatile","while","yield","sealed","record","permits","System"];this.literals=["true","false","null"];this.dataTypes=["Byte","Short","Integer","Long","Float","Double","Boolean","Character"];this.classKeywords=["class","interface","enum","extends","implements"]}peek(){return this.current+1<this.sourceCode.length?this.sourceCode.charAt(this.current+1):"\0"}isAlphabet(t){return null!=String(t).match(new RegExp("[a-zA-Z_$]"))}isIdentifierEnding(t){return null!=String(t).match(new RegExp("[a-zA-Z_$0-9]"))}notAtEnd(){return this.current<this.sourceCode.length}tokenize(t){for(this.sourceCode=t,"\n"!=this.sourceCode.charAt(this.sourceCode.length-1)&&(this.sourceCode+="\n");this.notAtEnd();){let t=this.sourceCode.charAt(this.current);if(this.isIdentifierEnding(t)){for(;this.notAtEnd()&&this.isIdentifierEnding(this.peek());)this.col++,this.current++;this.keywords.includes(this.sourceCode.substring(this.start,this.current+1))?this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.KEYWORD)):this.literals.includes(this.sourceCode.substring(this.start,this.current+1))?this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.LITERAL)):this.dataTypes.includes(this.sourceCode.substring(this.start,this.current+1))?this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.DATATYPE)):this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.IDENTIFIER))}else if(this.isNumerical(t)){for(;this.notAtEnd()&&(this.isNumerical(this.peek())||"."==this.peek());)this.col++,this.current++;this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.NUMBER))}else switch(t){case" ":this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.SPACE));break;case"\n":this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.NEWLINE)),this.line++,this.col=0;break;case"\t":this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.TAB));break;case"@":this.annotation(),this.current++,this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.ANNOTATION));break;case'"':'"'==this.peek()&&'"'==this.peekNext()?(this.current+=2,this.multilineString()):this.string(),this.current++,this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.STRING));break;case"'":this.character(),this.current++,this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.STRING));break;case"/":"/"==this.peek()?(this.comment(),this.current++,this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.COMMENT))):"*"==this.peek()?"*"!=this.peekNext()||"/"==this.peekAfterNext()?(this.multilineComment(),this.current++,this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.COMMENT))):(this.multilineComment(),this.current++,this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.JAVADOC))):this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.OTHERPUNCTUATION));break;case";":case"(":case")":case"{":case"}":case"<":case">":case"[":case"]":case",":case"*":case"=":case"+":case"-":case"%":case"!":case"~":case"&":case"|":case"?":case":":case"^":case".":this.tokens.push(new Token(this.sourceCode.substring(this.start,this.current+1),this.line,this.col,TokenType.OTHERPUNCTUATION));break;default:console.log("Could not interpret character: '"+t+"'. [ln: "+this.line+"]")}this.col++,this.current++,this.start=this.current}for(let t=0;t<this.tokens.length;t++){if(this.current=this.tokens[t].col,"import"===this.tokens[t].text||"package"===this.tokens[t].text){let e=t+1;for(;";"!==this.tokens[e].text;){let t=this.tokens[e];this.tokens.splice(e,0,new Token(t.text,t.line,t.col,TokenType.IMPORTNAME)),this.tokens.splice(e+1,1),e++}}else if((this.tokens[t].type==TokenType.IDENTIFIER||null!=this.tokens[t].text.match(/[})\]]/g))&&this.tokens.length>t+1&&"."===this.tokens[t+1].text){let e=t+1;for(;this.tokens[e].type!=TokenType.OTHERPUNCTUATION||"."===this.tokens[e].text;){if("."===this.tokens[e].text){e++;continue}if(e+1==this.tokens.length)break;let t=this.tokens[e];this.tokens.splice(e,0,new Token(t.text,t.line,t.col,TokenType.HEADDATATYPE)),this.tokens.splice(e+1,1),e++}}if(this.notAtEnd()&&(this.tokens[t].type==TokenType.IDENTIFIER||this.tokens[t].type==TokenType.HEADDATATYPE)&&"("===this.tokens[t+1].text){let e=this.tokens[t];this.tokens.splice(t,0,new Token(e.text,e.line,e.col,TokenType.METHODNAME)),this.tokens.splice(t+1,1)}if(t-1>0&&this.classKeywords.includes(this.tokens[t-1].text)){for(;this.tokens[t].type==TokenType.SPACE||this.tokens[t].type==TokenType.NEWLINE||this.tokens[t].type==TokenType.TAB;)t++;let e=this.tokens[t];this.tokens.splice(t,0,new Token(e.text,e.line,e.col,TokenType.CLASSNAME)),this.tokens.splice(t+1,1)}if(this.isUppercase(this.tokens[t].text)&&(this.tokens[t].type==TokenType.HEADDATATYPE||this.tokens[t].type==TokenType.IDENTIFIER)){let e=this.tokens[t];this.tokens.splice(t,0,new Token(e.text,e.line,e.col,TokenType.CONSTANT)),this.tokens.splice(t+1,1)}}return this.tokens}peekAfterNext(){return this.current+3<this.sourceCode.length?this.sourceCode.charAt(this.current+3):"\0"}annotation(){for(;this.notAtEnd()&&" "!=this.peek();){if("\n"==this.peek()){this.line++,this.col=1;break}this.col++,this.current++}}multilineString(){for(;this.notAtEnd();){if('"'==this.sourceCode.charAt(this.current)&&'"'==this.peek()&&'"'==this.peekNext()){if(this.current-1>=0&&"\\"!=this.sourceCode.charAt(this.current-1)||0==this.current)break}else"\n"==this.peek()&&(this.line++,this.col=1);this.col++,this.current++}this.col++,this.current++}multilineComment(){for(;this.notAtEnd()&&("*"!=this.peek()||"/"!=this.peekNext());)"\n"==this.peek()&&(this.line++,this.col=1),this.col++,this.current++;this.col++,this.current++}peekNext(){return this.current+2<this.sourceCode.length?this.sourceCode.charAt(this.current+2):"\0"}isUppercase(t){return null!=t.match(new RegExp("^[A-Z_$][A-Z_$0-9]*$"))}comment(){for(;this.notAtEnd()&&"\n"!=this.peek();)this.col++,this.current++}character(){for(;this.notAtEnd()&&("'"!=this.peek()||"\\"==this.sourceCode.charAt(this.current));)if("\\"!=this.sourceCode.charAt(this.current))this.col++,this.current++;else if(this.col++,this.current++,"'"==this.peek())break}string(){for(;this.notAtEnd()&&('"'!=this.peek()||"\\"==this.sourceCode.charAt(this.current));)if("\\"!=this.sourceCode.charAt(this.current))this.col++,this.current++;else if(this.col++,this.current++,'"'==this.peek())break}isNumerical(t){return null!=String(t).match(new RegExp("[0-9]"))}}var head=document.head||document.getElementsByTagName("head")[0],style=document.createElement("style");function highlight(t){let e,s=document.getElementsByTagName("code");for(let n=0;n<s.length;n++){let i=(new Tokenizer).tokenize(s[n].innerText),o=new Parser(i,t).parse();e=o[1],s[n].innerHTML=o[0]}style.styleSheet?style.styleSheet.cssText=e:style.appendChild(document.createTextNode(e))}function setTheme(t){let e=new Parser(null,null).styles[t],s=new Parser(null,null).stylesheet;for(let t=0;t<e.length;t++)s=s.replace("%s",e[t]);style.styleSheet?style.styleSheet.cssText=s:style.appendChild(document.createTextNode(s))}head.appendChild(style),style.type="text/css",highlight("Normal");