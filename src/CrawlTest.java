import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(Enclosed.class)
public class CrawlTest extends ProjectTest {

	// Old server - http://cs.usfca.edu/~cs212/
	// New server - http://vis.cs.ucdavis.edu/~cjbryan/cs212/
	private static final String serverPath = "http://vis.cs.ucdavis.edu/~cjbryan/cs212/";
	
	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class IndexTest {

		
		/**
		 * Test helper method. Sets up the input, actual, and expected paths,
		 * and then sets up the arguments to send to the
		 * {@link Driver#main(String[])} method.
		 *
		 * @param name
		 *            name to use in the actual and expected filename
		 * @param path
		 *            relative path to use for the input
		 * @throws MalformedURLException
		 */
		public void test(String name, String url, int limit) throws MalformedURLException {
			String filename = String.format("index-url-%s.json", name);

			URL input = new URL(url);
			Path actual = Paths.get("out", filename);
			Path expected = Paths.get("expected", "index-url", filename);

			String[] args = { "-url", input.toString(), "-index", actual.toString(), "-limit", Integer.toString(limit) };
			checkOutput(expected, actual, args);
		}

		@Test
		public void test01Hello() throws MalformedURLException {
			test("hello", serverPath + "simple/hello.html", 1);
		}

		@Test
		public void test02Simple() throws MalformedURLException {
			test("simple", serverPath + "simple/index.html", 10);
		}

		@Test
		public void test03Birds() throws MalformedURLException {
			test("birds", serverPath + "birds/birds.html", 50);
		}

		@Test
		public void test04Recurse() throws MalformedURLException {
			test("recurse", serverPath + "recurse/link01.html", 100);
		}

		@Test
		public void test05SecondVariety() throws MalformedURLException {
			test("gutenberg-32032", serverPath + "gutenberg/32032-h/32032-h.htm", 1);
		}

		@Test
		public void test06Gutenberg() throws MalformedURLException {
			test("gutenberg", serverPath + "gutenberg/", 5);
		}

		@Test
		public void test07CSSProperties() throws MalformedURLException {
			test("wdgcss-properties", serverPath + "wdgcss/properties.html", 1);
		}

		@Test
		public void test08CSS() throws MalformedURLException {
			test("wdgcss", serverPath + "wdgcss/properties.html", 60);
		}

		@Test
		public void test09NumpyQuick() throws MalformedURLException {
			test("numpy-quick", serverPath + "numpy/user/quickstart.html", 1);
		}

		@Test
		public void test10Numpy() throws MalformedURLException {
			test("numpy", serverPath + "numpy/user/index.html", 10);
		}
	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class SearchTest {
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
		 * @throws MalformedURLException
		 */
		public void test(String name, String url, String queries, int limit) throws MalformedURLException {
			String filename = String.format("results-url-%s.json", name);

			URL input = new URL(url);
			Path query = Paths.get("query", queries);

			Path actual = Paths.get("out", filename);
			Path expected = Paths.get("expected", "results-url", filename);

			String[] args = {
					"-url", input.toString(), "-query", query.toString(),
					"-results", actual.toString(), "-limit", Integer.toString(limit) };
			checkOutput(expected, actual, args);
		}

		@Test
		public void test01Simple() throws MalformedURLException {
			test("simple", serverPath + "simple/index.html", "simple.txt", 10);
		}

		@Test
		public void test02Birds() throws MalformedURLException {
			test("birds", serverPath + "birds/birds.html", "birds.txt", 50);
		}

		@Test
		public void test03Gutenberg() throws MalformedURLException {
			test("gutenberg", serverPath + "gutenberg/", "gutenberg.txt", 5);
		}

		@Test
		public void test04CSS() throws MalformedURLException {
			test("wdgcss", serverPath + "wdgcss/properties.html", "letters.txt", 60);
		}

		@Test
		public void test05Numpy() throws MalformedURLException {
			test("numpy", serverPath + "numpy/user/index.html", "letters.txt", 10);
		}
	}
}
