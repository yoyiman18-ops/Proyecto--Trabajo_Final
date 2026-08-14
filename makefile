all:
	mkdir -p bin
	javac -d bin src/*.java

run:
	java -cp bin App

clean:
	rm -rf bin
