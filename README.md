# Replication Package

This is the replication package for the `RefactorCheck`. It contains the Java source code and the datasets needed to rerun the checks.

## 1. Directory Layout

```text
replicationPackage/
|-- dataset/
|   |-- HumanRefactorings/
|   |   |-- original/
|   |   |-- positive/
|   |   |-- negative/
|   |   `-- sequence/
|   `-- LLMRefactorings/
|       |-- Original/
|       |-- GPT-5.4 mini/
|       |-- MiniMax-M3/
|       `-- GPT-5.3-Codex/
|-- src/
|-- pom.xml
`-- README.md
```

## 2. Data

`dataset/Humanrefactorings/` contains 58 original methods, 58 positive refactorings, 58 negative refactorings, and 58 subsequent-refactoring cases.

- `original/`: original HumanRefactorings inputs.
- `positive/`: behavior-breaking refactorings.
- `negative/`: behavior-preserving refactorings.
- `sequence/`: additional behavior-preserving refactorings derived from the negative cases. Each `testN.java` applies one or two extra refactoring steps after `negative/testN.java`.

`dataset/LLMRefactor/` contains 3,000 extracted original Java methods and only the three selected model result sets:

- `Original/`: 3,000 original methods
- `GPT-5.4 mini/`: 3,000 refactoring results
- `MiniMax-M3/`: 3,000 refactoring results
- `GPT-5.3-Codex/`: 3,000 refactoring results

The same file name is used across the four LLM folders, for example `arthas_0001.java`.

## 3. Environment

- Java 17+
- Maven 3.8+

Build and test:

```bash
mvn test
mvn package
```

The packaged jar is written to:

```text
target/refactorcheck-0.1.0.jar
```

## 4. Running RefactorCheck

Run one pair:

```bash
java -jar target/refactorcheck-0.1.0.jar \
  --before dataset/Humanrefactorings/original/test1.java \
  --after dataset/Humanrefactorings/positive/test1.java \
  --impact on \
  --output outputs/human_positive_test1.json
```

Run one HumanRefactorings subsequent-refactoring pair:

```bash
java -jar target/refactorcheck-0.1.0.jar \
  --before dataset/Humanrefactorings/original/test1.java \
  --after dataset/Humanrefactorings/subsequent/test1.java \
  --impact on \
  --output outputs/human_subsequent_test1.json
```

Run one LLM pair:

```bash
java -jar target/refactorcheck-0.1.0.jar \
  --before dataset/LLMRefactor/Original/arthas_0001.java \
  --after "dataset/LLMRefactor/GPT-5.4 mini/arthas_0001.java" \
  --impact on \
  --output outputs/llm_arthas_0001_gpt54mini.json
```

## 5. Output Semantics

The JSON report field `consistent` is the main verdict:

- `true`: the refactoring is judged behavior-preserving.
- `false`: the refactoring is judged behavior-breaking.

For batch runs, `answer=Yes` means behavior-preserving and `answer=No` means behavior-breaking.
