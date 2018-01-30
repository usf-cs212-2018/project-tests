import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IndexTest.class, SearchTest.class, ThreadTest.class, StressTest.class })
public class Project3Test {
	/*
	 * To be eligible for code review for Project 3, you must pass this test
	 * suite (EXCEPT for the StressTest tests) on the lab computers using the
	 * `project` script.
	 *
	 * To be clear, you may sign up for code review if you are passing
	 * IndexTest, SearchTest, and ThreadTest. You can still sign up for code
	 * review if you are failing StressTest.
	 */
}
