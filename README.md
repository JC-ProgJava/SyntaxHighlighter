# SyntaxHighlighter

[![CodeFactor](https://www.codefactor.io/repository/github/jc-progjava/syntaxhighlighter/badge)](https://www.codefactor.io/repository/github/jc-progjava/syntaxhighlighter)

A syntax highlighting tool made with Java, HTML, CSS & JS.

### Supported Languages
This tool supports syntax highlighting in:
- Java (Versions ≤16)
- (More will be added over time)

### How to use
There are two versions of the syntax highlighter:
- Java source: compile and run `SyntaxHighlighter.java`. Follow the input prompts (to input filepath), and run the program. It should output a file `index.html` that contains a HTML rendering of your code after syntax highlighting.
- Javascript source: attach this file to your webpages to have all `<code>` tags syntax highlighted.

There are some example output files (.html) in the examples folder that you can take a look at.

### Examples
You can take a look at example output files [here](https://github.com/JC-ProgJava/SyntaxHighlighter/tree/main/examples). See live previews in the links below:
- [SmartEditor.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/smarteditor.html)
- [Parser.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/parser.html)
- [Tokenizer.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/tokenizer.html)
- [SmartEditor-Dark.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/smarteditor-dark.html)
- [Parser-Dark.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/parser-dark.html)
- [Tokenizer-Dark.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/tokenizer-dark.html)
- [SmartEditor-Exotic.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/smarteditor-exotic.html)
- [Parser-Exotic.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/parser-exotic.html)
- [Tokenizer-Exotic.html](https://htmlpreview.github.io/?https://raw.githubusercontent.com/JC-ProgJava/SyntaxHighlighter/main/examples/tokenizer-exotic.html)

- Try highlighting your own source code [here](https://jc-progjava.github.io/SyntaxHighlighter/)!

### Features
- Support for Java 16 (multiline strings, records, new keywords [permits, record, sealed, non-sealed])
- Customizable (through the JS version (find the CSS selector values at the bottom of the source file). You can also customize the Java version by tweaking the CSS selector values. See what the different token types represent [here](https://github.com/JC-ProgJava/SyntaxHighlighter/blob/main/src/Java/TokenType.java))
- Simple and small: the entire JS script file (that includes CSS styling) is less than 700 lines long (~16KB). After minification and zipping, the file only takes up **3KB** of space.

### Why use this syntax highlighter?
- It is easy to use: just download the JS script and link using `<script>` to webpages and get fully syntax-highlighted code in `<code>` tags. For the Java version, just input the filepath and you get a HTML code file with the syntax-highlighted version of your source file.
- It is simple and small: as listed in the Features section, the entire JS file is approximately **3KB** after minification and compression. The expanded (and beautified (done using this [tool](https://prettier.io/))) version is also easy to read and understand for developers who are curious how syntax highlighters are made.
- It doesn't require any external dependencies.
