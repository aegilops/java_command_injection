# java_command_injection

Build with `mvn clean package`

Run with `java -jar target/command-injection-1.0.0.jar`

Relies on Java 8+

## Expected output

```sh
$ mvn clean package && java -jar target/command-injection-1.0.0.jar
Command injection demo
1
Running: /bin/sh "-c" "echo case 1"
case 1
2
Running: /bin/sh "-c" "echo case 2"
case 2
3
Running: /bin/cat "foo | rm foo"
cat: foo | rm foo: No such file or directory
4
Running: /bin/chmod 777 with envp of "whatever=this_is_not_an_argument"
usage:  chmod [-fhv] [-R [-H | -L | -P]] [-a | +a | =a  [i][# [ n]]] mode|entry file ...
        chmod [-fhv] [-R [-H | -L | -P]] [-E | -C | -N | -i | -I] file ...
6
Running: /bin/echo "/some/absolute/path" "/another/absolute/path" " foo | rm foo"
/some/absolute/path /another/absolute/path  foo | rm foo
Foo is still there: 
Running: ls "-l" "foo"
-rw-r--r--  1 user  group  0 Mar 13 16:28 foo
```
