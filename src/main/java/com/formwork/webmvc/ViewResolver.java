package com.formwork.webmvc;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2020/5/13.
 */
//将静态文件变为动态文件
// 用户数据和静态模版结合
public class ViewResolver {

    private String viewName;
    private File templateFile;

    public ViewResolver(String viewName, File templateFile) {
        this.viewName = viewName;
        this.templateFile = templateFile;
    }

    public String viewResolver(ModelAndView mv) throws IOException {
        StringBuffer sb = new StringBuffer();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.templateFile), "UTF-8"));
        String line = null;
        while (null != (line = reader.readLine())) {
            Matcher matcher = matcher(line);

            while (matcher.find()) {
                for(int i = 1; i <= matcher.groupCount(); i++) {
                    String paramName = matcher.group(i);
                    Object paramValue = mv.getModel().get(paramName);
                    if(paramValue == null) {
                        continue;
                    }
                    line = line.replaceAll("\\$\\{"  + paramName +"\\}", paramValue.toString());
                }
            }
            sb.append(line);
        }
        return sb.toString();
    }

    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("^.*\\$\\{(.*)\\}.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }
}
