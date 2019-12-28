package api.util;

import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liran on 2017/1/21.
 */
public class NewReport implements IReporter{
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
       List<ITestResult>  list=new ArrayList<ITestResult>();
        for(ISuite suite:suites){
            Map<String,ISuiteResult> suiteResults=suite.getResults();
            for(ISuiteResult suiteResult:suiteResults.values()){
                ITestContext testContext=suiteResult.getTestContext();
                list.addAll(testContext.getPassedTests().getAllResults());
                list.addAll(testContext.getFailedTests().getAllResults());
                list.addAll(testContext.getSkippedTests().getAllResults());
                list.addAll(testContext.getFailedConfigurations().getAllResults());
            }
        }
        this.sort(list);
        this.outputResult(list,outputDirectory+"/test.html");
    }
    private void sort(List<ITestResult> list){
        Collections.sort(list, new Comparator<ITestResult>() {
            @Override
            public int compare(ITestResult r1, ITestResult r2) {
                if(r1.getStartMillis()>r2.getStartMillis()) {
                    return 1;
                }else{
                return -1;
            }
        }});
        }

    private  void outputResult(List<ITestResult> list,String path){
        try{
            BufferedWriter output=new BufferedWriter(new FileWriter(new File(path),true));
            StringBuffer sb= new StringBuffer();
            sb.append("<html><body><table table border='1' align='middle' cellpadding='1' cellspacing='1'><th>接口名称</th><th>测试方法用例</th><th>执行开始时间</th><th>执行时长</th><th>测试结果</th>");
            for(ITestResult result:list){
                if(sb.length()!=0){
                    sb.append("\r\n");
                }
                sb.append("<tr>")
                        .append("<td>")
                        .append(result.getTestContext().getName())
                        .append("</td><td>")
                        .append(result.getMethod().getMethodName())
                        .append(result.getMethod().getDescription())
                        .append("</td><td>")
                        .append(this.formatDate(result.getStartMillis()))
                        .append("</td><td>")
                        .append(result.getEndMillis()-result.getStartMillis())
                        .append("毫秒</td><td>")
                        .append(this.getStatus(result.getStatus()))
                        .append("</td></tr>");

            }
            sb.append("</table></body></html>");
            output.write(sb.toString());
            output.flush();
            output.close();
        }catch(IOException e){
             e.printStackTrace();
      }
    }

    private String formatDate(long date){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    private String getStatus(int status) {
        String statusString = null;
        switch (status) {
            case 1:
                statusString = "成功";
                break;
            case 2:
                statusString = "失败";
                break;
            case 3:
                statusString = "跳过";
                break;
            default:
                break;
        }
        return statusString;

        }

}
