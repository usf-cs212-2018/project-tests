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
public class IndexTest extends ProjectTest {

	public static class EnvironmentTest {

		@Test
		public void testEnvironment() {
			String warning = "Check your environment setup for the correct directory structure.";
			Assert.assertTrue(warning, isEnvironmentSetup() && Files.isReadable(Paths.get("html")));
		}

	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class ExceptionsTest {
		@Rule
		public Timeout globalTimeout = Timeout.seconds(30);

		@Test
		public void test01NoArguments() {
			String[] args = {};
			checkExceptions(args);
		}

		@Test
		public void test02BadArguments() {
			String[] args = { "hello", "world" };
			checkExceptions(args);
		}

		@Test
		public void test03MissingPath() {
			String[] args = { "-path" };
			checkExceptions(args);
		}

		@Test
		public void test04InvalidPath() {
			// generates a random path name
			String path = Long.toHexString(Double.doubleToLongBits(Math.random()));
			String[] args = { "-path", path};
			checkExceptions(args);
		}

		@Test
		public void test05NoOutput() throws IOException {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String[] args = { "-path", path };

			// make sure to delete old index.json if it exists
			Path output = Paths.get("index.json");
			Files.deleteIfExists(output);

			checkExceptions(args);

			// make sure a new index.json was not created
			Assert.assertFalse(Files.exists(output));
		}

		@Test
		public void test06DefaultOutput() throws IOException {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String[] args = { "-path", path, "-index" };

			// make sure to delete old index.json if it exists
			Path output = Paths.get("index.json");
			Files.deleteIfExists(output);

			checkExceptions(args);

			// make sure a new index.json was created
			Assert.assertTrue(Files.exists(output));
		}

		@Test
		public void test07EmptyOutput() throws IOException {
			String[] args = { "-index" };

			// make sure to delete old index.json if it exists
			Path output = Paths.get("index.json");
			Files.deleteIfExists(output);

			checkExceptions(args);

			// make sure a new index.json was created
			Assert.assertTrue(Files.exists(output));
		}

		@Test
		public void test08SwitchedOrder() throws IOException {
			String path = Paths.get("html", "simple", "hello.html").toString();
			String[] args = { "-index", "-path", path };

			// make sure to delete old index.json if it exists
			Path output = Paths.get("index.json");
			Files.deleteIfExists(output);

			checkExceptions(args);

			// make sure a new index.json was created
			Assert.assertTrue(Files.exists(output));
		}
	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class OutputTest {

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
		public void test(String name, Path path) {
			String filename = String.format("index-path-%s.json", name);

			Path input = Paths.get("html").resolve(path);
			Path actual = Paths.get("out", filename);
			Path expected = Paths.get("expected", "index-path", filename);

			String[] args = { "-path", input.toString(), "-index", actual.toString() };
			checkOutput(expected, actual, args);
		}

		@Test
		public void test01Hello() {
			test("hello", Paths.get("simple", "hello.html"));
		}

		@Test
		public void test02Simple() {
			test("simple", Paths.get("simple"));
		}

		@Test
		public void test03Birds() {
			test("birds", Paths.get("birds"));
		}

		@Test
		public void test04Recurse() {
			test("recurse", Paths.get("recurse"));
		}

		@Test
		public void test05SecondVariety() {
			test("gutenberg-32032", Paths.get("gutenberg", "32032-h"));
		}

		@Test
		public void test06Gutenberg() {
			test("gutenberg", Paths.get("gutenberg"));
		}

		@Test
		public void test07CSSProperties() {
			test("wdgcss-properties", Paths.get("wdgcss", "all-properties.html"));
		}

		@Test
		public void test08CSS() {
			test("wdgcss", Paths.get("wdgcss"));
		}

		@Test
		public void test09NumpyQuick() {
			test("numpy-quick", Paths.get("numpy", "user", "quickstart.html"));
		}

		@Test
		public void test10Numpy() {
			test("numpy", Paths.get("numpy", "user"));
		}

	}
}
