package api.util;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.log4testng.Logger;

import java.io.File;

/**
 * Created by liran on 2017/2/7.
 */
public class TestFinish extends TestListenerAdapter {
    private static Logger logger = Logger.getLogger(TestFinish.class);

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        logger.info(tr.getName() + " Failure");
        Reporter.log("怎么执行失败了");
        takeFile(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        logger.info(tr.getName() + " Skipped");
        takeFile(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        logger.info(tr.getName() + " Success");
        takeFile(tr);
    }

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        logger.info(tr.getName() + " Start");

    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        File file=new File("D:\\liran2");
        file.mkdir();

    }


    private void takeFile(ITestResult tr) {

    File file=new File("D:\\liran3");
        file.mkdir();
    }


}
