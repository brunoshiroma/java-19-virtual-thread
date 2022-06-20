build:
	javac --release 19 --enable-preview Main.java

run:
	java --enable-preview Main virtual
	java --enable-preview Main normal

run-scl:
	java --source 19 --enable-preview Main.java virtual
	java --source 19 --enable-preview Main.java normal