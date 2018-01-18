JFLAGS = -g
JC = javac
JV = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) src/*.java

CLASSES = \
	src/Anim.java \
	src/Bubble.java \
	src/Canon.java \
	src/CloudBubbles.java \
	src/Game.java \
	src/Menu.java \
	src/Main.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) src/*.class

run:
	$(JV) -cp src/ Main
