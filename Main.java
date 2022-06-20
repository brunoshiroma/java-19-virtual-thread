import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {

    private static final long BYTES = 1024;

    private static final long KILO_BYTES = 1024 * 1024;

    private static final long MEGA_BYTES = 1024 * 1024 * 1024;

    private static final int THREAD_COUNT = 10_000;

    private static String formatBytes(long value) {
        if (value <= BYTES) {
            return String.format("%dB", value);
        }
        if (value <= KILO_BYTES) {
            return String.format("%fKB", (float) value / BYTES);
        }
        if (value <= MEGA_BYTES) {
            return String.format("%fMB", (float) value / KILO_BYTES);
        }

        return String.format("%fGB", (float) value / MEGA_BYTES);

    }

    private static void printMemoryStats() {
        var totalMemory = Runtime.getRuntime().totalMemory();
        var freeMemory = Runtime.getRuntime().freeMemory();
        System.out.printf("Total mem %s, free mem %s\n", formatBytes(totalMemory), formatBytes(freeMemory));
    }

    private static Integer doTest() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(1));
        var msg = String.format("\nThread %d, is virtual %b, running thread(s) %d",
                Thread.currentThread().threadId(),
                Thread.currentThread().isVirtual(),
                Thread.activeCount());
        return (int) Thread.currentThread().threadId();
    }

    private static void doExecuteTest(ExecutorService executorService) {
        System.out.println("Initial ram usagem");
        printMemoryStats();
        var init = LocalDateTime.now();

        try (executorService) {
            IntStream.range(0, THREAD_COUNT).forEach(i -> {
                executorService.submit(Main::doTest);
            });
        }

        var end = LocalDateTime.now();

        System.out.printf("Time %dms\n", ChronoUnit.MILLIS.between(init, end));
        System.out.println("Final ram usagem");
        printMemoryStats();
        System.out.printf("GC count %d\n", ManagementFactory.getGarbageCollectorMXBeans().stream()
                .mapToLong((gc) -> gc.getCollectionCount()).sum());
    }

    private static void testVirtualThread() {
        System.out.println("\n\nVirtual");
        doExecuteTest(Executors.newVirtualThreadPerTaskExecutor());
        System.out.println();
    }

    private static void testNormalThread() {
        System.out.println("\n\nNormal");
        doExecuteTest(Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory()));
    }

    public static void main(String args[]) {
        if(args.length >= 1){
            switch(args[0]){
                case "virtual":
                 testVirtualThread();
                 break;
                case "normal":
                testNormalThread();
                break;
                default:
                    System.out.println("pass 'virtual' or 'normal' program arg");
            }
            return;
        }

        System.out.println("pass 'virtual' or 'normal' program arg");
    }
}
