all: build

.PHONY: build codeql

build:
	mvn package

codeql:
	mvn clean
	codeql database create java_command_injection_tests.db --language=java --command="mvn package" --overwrite

clean:
	mvn clean
	rm -rf java_command_injection_tests.db