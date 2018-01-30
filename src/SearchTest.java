import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(Enclosed.class)
public class SearchTest extends ProjectTest {

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class ExceptionsTest {
		@Rule
		public Timeout globalTimeout = Timeout.seconds(30);

		@Test
		public void test01MissingQueryPath() {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String[] args = { "-path", path, "-query" };
			checkExceptions(args);
		}

		@Test
		public void test02InvalidQueryPath() {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Long.toHexString(Double.doubleToLongBits(Math.random()));
			String[] args = { "-path", path, "-query", query };
			checkExceptions(args);
		}

		@Test
		public void test03InvalidExactPath() {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Long.toHexString(Double.doubleToLongBits(Math.random()));
			String[] args = { "-path", path, "-query", query, "-exact" };
			checkExceptions(args);
		}

		@Test
		public void test04NoOutput() throws IOException {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Paths.get("query", "simple.txt").toString();
			String[] args = { "-path", path, "-query", query };

			// make sure to delete old index.json and results.json if it exists
			Path index = Paths.get("index.json");
			Path results = Paths.get("results.json");
			Files.deleteIfExists(index);
			Files.deleteIfExists(results);

			checkExceptions(args);

			// make sure a new index.json and results.json were not created
			Assert.assertFalse(Files.exists(index) || Files.exists(results));
		}

		@Test
		public void test05DefaultOutput() throws IOException {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Paths.get("query", "simple.txt").toString();
			String[] args = { "-path", path, "-query", query, "-results" };

			// make sure to delete old index.json and results.json if it exists
			Path index = Paths.get("index.json");
			Path results = Paths.get("results.json");
			Files.deleteIfExists(index);
			Files.deleteIfExists(results);

			checkExceptions(args);

			// make sure a new results.json was not created (but index.json was not)
			Assert.assertTrue(Files.exists(results) && !Files.exists(index));
		}

		@Test
		public void test06EmptyIndex() throws IOException {
			String query = Paths.get("query", "simple.txt").toString();
			String[] args = { "-query", query, "-results" };

			// make sure to delete old index.json and results.json if it exists
			Path index = Paths.get("index.json");
			Path results = Paths.get("results.json");
			Files.deleteIfExists(index);
			Files.deleteIfExists(results);

			checkExceptions(args);

			// make sure a new results.json was not created (but index.json was not)
			Assert.assertTrue(Files.exists(results) && !Files.exists(index));
		}

		@Test
		public void test07EmptyQuery() throws IOException {
			String[] args = { "-results" };

			// make sure to delete old index.json and results.json if it exists
			Path index = Paths.get("index.json");
			Path results = Paths.get("results.json");
			Files.deleteIfExists(index);
			Files.deleteIfExists(results);

			checkExceptions(args);

			// make sure a new results.json was not created (but index.json was not)
			Assert.assertTrue(Files.exists(results) && !Files.exists(index));
		}

		@Test
		public void test08SwitchedOrder() throws IOException {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String query = Paths.get("query", "simple.txt").toString();
			String[] args = { "-query", query, "-results", "-path", path, "-exact" };

			// make sure to delete old index.json and results.json if it exists
			Path index = Paths.get("index.json");
			Path results = Paths.get("results.json");
			Files.deleteIfExists(index);
			Files.deleteIfExists(results);

			checkExceptions(args);

			// make sure a new results.json was not created (but index.json was not)
			Assert.assertTrue(Files.exists(results) && !Files.exists(index));
		}
	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class SearchPartialTest {

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
		public void test(String name, Path path, String queries) {
			String filename = String.format("results-path-%s.json", name);

			Path input = Paths.get("html").resolve(path);
			Path query = Paths.get("query", queries);

			Path actual = Paths.get("out", filename);
			Path expected = Paths.get("expected", "results-path", filename);

			String[] args = { "-path", input.toString(), "-query", query.toString(), "-results", actual.toString() };
			checkOutput(expected, actual, args);
		}

		@Test
		public void test01Simple() {
			test("simple", Paths.get("simple"), "simple.txt");
		}

		@Test
		public void test02Birds() {
			test("birds", Paths.get("birds"), "birds.txt");
		}

		@Test
		public void test03Gutenberg() {
			test("gutenberg", Paths.get("gutenberg"), "gutenberg.txt");
		}

		@Test
		public void test04CSS() {
			test("wdgcss", Paths.get("wdgcss"), "letters.txt");
		}

		@Test
		public void test05Numpy() {
			test("numpy", Paths.get("numpy", "user"), "letters.txt");
		}
	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class SearchExactTest {

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
		public void test(String name, Path path, String queries) {
			String filename = String.format("results-path-%s-exact.json", name);

			Path input = Paths.get("html").resolve(path);
			Path query = Paths.get("query", queries);

			Path actual = Paths.get("out", filename);
			Path expected = Paths.get("expected", "results-path", filename);

			String[] args = { "-path", input.toString(), "-query", query.toString(), "-results", actual.toString(), "-exact" };
			checkOutput(expected, actual, args);
		}

		@Test
		public void test01Simple() {
			test("simple", Paths.get("simple"), "simple.txt");
		}

		@Test
		public void test02Birds() {
			test("birds", Paths.get("birds"), "birds.txt");
		}

		@Test
		public void test03Gutenberg() {
			test("gutenberg", Paths.get("gutenberg"), "gutenberg.txt");
		}
	}
}
