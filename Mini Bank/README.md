# MiniBank (CEUE203 - Object-Oriented Programming)

Console banking application built from all Part B practicals (1-14).
Needs JDK 17+.

## Build and run
```bash
mkdir out
javac -d out $(find src -name "*.java")
java -cp out MiniBank
```

## Runnable JAR
```bash
echo "Main-Class: MiniBank" > manifest.txt
jar cfm minibank.jar manifest.txt -C out .
java -jar minibank.jar
```

## Run the JUnit tests
Download `junit-platform-console-standalone-<version>.jar` from Maven Central, then:
```bash
javac -cp junit.jar:out -d out-test test/*.java
java -jar junit.jar --class-path out:out-test --scan-class-path
```
(On Windows use `;` instead of `:` in the class path.)

## Data
Saved in the `data/` folder (not committed): `accounts.dat`, `logs/transactions.log`, `report.txt`.
Accounts are loaded on start-up and saved when you choose EXIT.

## Batch commands (menu option 8)
```
DEPOSIT AC0001 500
WITHDRAW AC0002 200
```
Finish with a blank line. They run in parallel on a pool of 4 threads.
