# The 1st target gets built when you type "make"
main: puzzle/Node.java puzzle/Search.java puzzle/Play.java
	javac puzzle/*.java

play: puzzle/Play.class
	java puzzle.Play

# Cleans machine code/libraries/executables
.PHONY: clean
clean: 
	rm -f puzzle/*.class
