import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StressTest extends ProjectTest {

	private static final int WARM_RUNS = 3;
	private static final int TIME_RUNS = 5;

	private static final int THREADS = 5;

	@Rule
	public Timeout globalTimeout = Timeout.seconds(120 * TIME_RUNS);

	@Test
	public void test01IndexConsistency() {
		ThreadTest.OutputTest tester = new ThreadTest.OutputTest(String.valueOf(THREADS));

		for (int i = 0; i < TIME_RUNS; i++) {
			tester.test10Numpy();
		}
	}

	@Test
	public void test02SearchConsistency() {
		ThreadTest.PartialTest tester = new ThreadTest.PartialTest(String.valueOf(THREADS));

		for (int i = 0; i < TIME_RUNS; i++) {
			tester.test05Numpy();
		}
	}

	@Test
	public void test03IndexRuntime() {
		String path = Paths.get("html", "numpy", "user").toString();

		String[] args1 = { "-path", path, "-threads", String.valueOf(1) };
		String[] args2 = { "-path", path, "-threads", String.valueOf(THREADS) };

		double singleAverage = benchmark(args1) / 1000000000.0;
		double threadAverage = benchmark(args2) / 1000000000.0;

		System.out.println();
		System.out.println("Indexing Benchmark:");
		System.out.printf("%d Threads: %.4f s%n", 1, singleAverage);
		System.out.printf("%d Threads: %.4f s%n", THREADS, threadAverage);
		System.out.printf("  Speedup: %.4f %n%n", singleAverage / threadAverage);

		Assert.assertTrue(singleAverage - threadAverage > 0);
	}

	@Test
	public void test04SearchRuntime() {
		String path = Paths.get("html", "numpy", "user").toString();
		String query = Paths.get("query", "letters.txt").toString();

		String[] args1 = { "-path", path, "-query", query, "-threads", String.valueOf(1) };
		String[] args2 = { "-path", path, "-query", query, "-threads", String.valueOf(THREADS) };

		double singleAverage = benchmark(args1) / 1000000000.0;
		double threadAverage = benchmark(args2) / 1000000000.0;

		System.out.println();
		System.out.println("Searching Benchmark:");
		System.out.printf("%d Threads: %.4f s%n", 1, singleAverage);
		System.out.printf("%d Threads: %.4f s%n", THREADS, threadAverage);
		System.out.printf("  Speedup: %.4f %n%n", singleAverage / threadAverage);

		Assert.assertTrue(singleAverage - threadAverage > 0);
	}

	private double benchmark(String[] args) {
		long total = 0;
		long start = 0;

		try {
			for (int i = 0; i < WARM_RUNS; i++) {
				Driver.main(args);
			}

			for (int i = 0; i < TIME_RUNS; i++) {
				start = System.nanoTime();
				Driver.main(args);
				total += System.nanoTime() - start;
			}
		}
		catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			String debug = String.format("%nArguments:%n    [%s]%nException:%n    %s%n", String.join(" ", args),
					writer.toString());
			Assert.fail(debug);
		}

		return (double) total / TIME_RUNS;
	}
}
