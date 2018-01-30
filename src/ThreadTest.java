import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Enclosed.class)
public class ThreadTest extends ProjectTest {

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class ExceptionTest {

		@Rule
		public Timeout globalTimeout = Timeout.seconds(60);

		@Test
		public void test01NegativeThreads() {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Paths.get("query", "simple.txt").toString();
			String threads = "-1";
			String[] args = { "-path", path, "-query", query, "-threads", threads };
			checkExceptions(args);
		}

		@Test
		public void test02ZeroThreads() {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Paths.get("query", "simple.txt").toString();
			String threads = "0";
			String[] args = { "-path", path, "-query", query, "-threads", threads };
			checkExceptions(args);
		}

		@Test
		public void test03FractionThreads() {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Paths.get("query", "simple.txt").toString();
			String threads = "3.14";
			String[] args = { "-path", path, "-query", query, "-threads", threads };
			checkExceptions(args);
		}

		@Test
		public void test04WordThreads() {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Paths.get("query", "simple.txt").toString();
			String threads = "fox";
			String[] args = { "-path", path, "-query", query, "-threads", threads };
			checkExceptions(args);
		}

		@Test
		public void test05DefaultThreads() {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Paths.get("query", "simple.txt").toString();
			String[] args = { "-path", path, "-query", query, "-threads" };
			checkExceptions(args);
		}

	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	@RunWith(Parameterized.class)
	public static class OutputTest extends IndexTest.OutputTest {

		@Rule
		public Timeout globalTimeout = Timeout.seconds(180);

		@Parameters(name = "{0} Threads")
		public static Object[] data() {
			return new String[] { "1", "2", "5" };
		}

		private String threads;

		public OutputTest(String threads) {
			this.threads = threads;
		}

		/**
		 * Test helper method. Sets up the input, actual, and expected paths,
		 * and then sets up the arguments to send to the
		 * {@link Driver#main(String[])} method.
		 *
		 * @param name
		 *            name to use in the actual and expected filename
		 * @param path
		 *            relative path to use for the input
		 */
		@Override
		public void test(String name, Path path) {
			Path input = Paths.get("html").resolve(path);
			Path actual = Paths.get("out", String.format("index-path-%s-%s.json", name, threads));
			Path expected = Paths.get("expected", "index-path", String.format("index-path-%s.json", name));

			String[] args = { "-path", input.toString(), "-index", actual.toString(), "-threads", threads };
			checkOutput(expected, actual, args);
		}

		// Runs the same tests as IndexTest.OutputTest
	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	@RunWith(Parameterized.class)
	public static class PartialTest extends SearchTest.SearchPartialTest {

		@Rule
		public Timeout globalTimeout = Timeout.seconds(120);

		@Parameters(name = "{0} Threads")
		public static Object[] data() {
			return new String[] { "1", "2", "5" };
		}

		private String threads;

		public PartialTest(String threads) {
			this.threads = threads;
		}

		/**
		 * Test helper method. Sets up the input, actual, and expected paths,
		 * and then sets up the arguments to send to the
		 * {@link Driver#main(String[])} method.
		 *
		 * @param name
		 *            name to use in the actual and expected filename
		 * @param path
		 *            relative path to use for the html input
		 * @param queries
		 *            filename of query file to use
		 */
		@Override
		public void test(String name, Path path, String queries) {
			Path input = Paths.get("html").resolve(path);
			Path query = Paths.get("query", queries);

			Path actual = Paths.get("out", String.format("results-path-%s-%s.json", name, threads));
			Path expected = Paths.get("expected", "results-path", String.format("results-path-%s.json", name));

			String[] args = { "-path", input.toString(), "-query", query.toString(), "-results", actual.toString(), "-threads", threads };
			checkOutput(expected, actual, args);
		}

		// Runs the same tests as SearchTest.PartialTest
	}
}
