all: sentence

sentence:
	@javac alysia.java
	@java alysia
	@rm *.class
