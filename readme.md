# Virtual Threads in native in Java 19(Preview)
Very simple test of virtual thread (user-threads, green-threads) in Java 19, more details on the JEP [doc](https://openjdk.org/jeps/425)

## Running
I recommend using vscode + dev container, already have configs on repo.

```bash
make build
make run
```

Output sample, running 10000 `threads`:
```bash
java --enable-preview Main virtual


Virtual
Initial ram usagem
Total mem 312.000000MB, free mem 309.431213MB
Time 1107ms
Final ram usagem
Total mem 312.000000MB, free mem 270.967957MB
GC count 2

java --enable-preview Main normal


Normal
Initial ram usagem
Total mem 312.000000MB, free mem 309.431213MB
Time 10650ms
Final ram usagem
Total mem 2.250000GB, free mem 1.141226GB
GC count 14
```