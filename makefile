all:
	mkdir -p bin
	javac --module-path lib/javafx-sdk-26.0.2/lib --add-modules javafx.controls,javafx.fxml -d bin src/**/*.java
	cp -r src/recursos bin/recursos

run:
	java --module-path lib/javafx-sdk-26.0.2/lib --add-modules javafx.controls,javafx.fxml -cp bin main.App

clean:
	rm -rf bin
