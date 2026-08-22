all:
	mkdir -p bin
	javac --module-path lib --add-modules javafx.controls,javafx.fxml -d bin src/**/*.java

run:
	java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin main.App

clean:
	rm -rf bin
