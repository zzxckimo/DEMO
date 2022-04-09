package Test.Demo.td.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import Test.Demo.fw.entity.Data;
import Test.Demo.td.bean.ResponseBean;
import Test.Demo.td.service.ApiService;

@RestController
public class TestController {
	@Autowired
	private ApiService apiService;
	//初始化
    @RequestMapping("/init")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	String contentString = apiService.getCoindeskAPI();
    	ResponseBean responseBean = new Gson().fromJson(contentString, ResponseBean.class);
    	response.setHeader("Content-type", "text/html;charset=UTF-8"); 

    	if(responseBean != null) {
    		apiService.insertDB( responseBean );
    		
    		PrintWriter out = response.getWriter();
        	out.println("<html><body>");
        	out.println("<table style='width:100%;border:3px solid;padding:5px;' rules='all' cellpadding='5';><tr>");
        	out.println("<th>CNAME</th>");
        	out.println("<th>CODE</th>");
        	out.println("<th>SYMBOL</th>");
        	out.println("<th>RATE</th>");
        	out.println("<th>RATE_FLOAT</th>");
        	out.println("<th>DESCRIPTION</th>");
        	out.println("</tr><tr>");
        	out.println("<th>"+responseBean.getBpi().getUsd().getCname()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getUsd().getCode()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getUsd().getSymbol()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getUsd().getRate()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getUsd().getRate_float()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getUsd().getDescription()+"</th>");
        	out.println("</tr><tr>");
        	out.println("</tr><tr>");
        	out.println("<th>"+responseBean.getBpi().getEur().getCname()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getEur().getCode()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getEur().getSymbol()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getEur().getRate()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getEur().getRate_float()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getEur().getDescription()+"</th>");
        	out.println("</tr><tr>");
        	out.println("</tr><tr>");
        	out.println("<th>"+responseBean.getBpi().getGbp().getCname()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getGbp().getCode()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getGbp().getSymbol()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getGbp().getRate()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getGbp().getRate_float()+"</th>");
        	out.println("<th>"+responseBean.getBpi().getGbp().getDescription()+"</th>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</body></html>");
        	return;
    	}
    	PrintWriter out = response.getWriter();
    	out.println("<html><body>");
    	out.println("<h1>查無資料</h1>");
    	out.println("</body></html>");
    }
    
    
    //newAPI
    @RequestMapping("/newAPI")
    public Object newAPI() throws ParseException {
    	String contentString = apiService.getCoindeskAPI();
    	ResponseBean responseBean = new Gson().fromJson(contentString, ResponseBean.class);
    	apiService.formatDate( responseBean );
    	return new Gson().toJson(responseBean);
    	
    }
    
    @RequestMapping("/origAPI")
    public String origAPI() throws ParseException {
    	String contentString = apiService.getCoindeskAPI();
    	return contentString;
    	
    }
    
  //查詢資料
    @RequestMapping("/queryData")
    public void queryData(HttpServletResponse response) throws IOException {
    	List<Data> dataList= apiService.queryData();
    	response.setHeader("Content-type", "text/html;charset=UTF-8"); 
    	if(dataList.size() >0 ) {
    		PrintWriter out = response.getWriter();
        	out.println("<html><body>");
        	out.println("<table style='width:100%;border:3px solid;padding:5px;' rules='all' cellpadding='5';><tr>");
        	out.println("<th>CNAME</th>");
        	out.println("<th>CODE</th>");
        	out.println("<th>SYMBOL</th>");
        	out.println("<th>RATE</th>");
        	out.println("<th>RATE_FLOAT</th>");
        	out.println("<th>DESCRIPTION</th>");
        	for(Data data : dataList) {
        		out.println("<tr>");
        		out.println("<th>"+data.getCname()+"</th>");
            	out.println("<th>"+data.getCode()+"</th>");
            	out.println("<th>"+data.getSymbol()+"</th>");
            	out.println("<th>"+data.getRate()+"</th>");
            	out.println("<th>"+data.getRate_float()+"</th>");
            	out.println("<th>"+data.getDescription()+"</th>");
            	out.println("</tr>");
        	}
        	out.println("</tr><tr>");
        	out.println("</table>");
        	out.println("</body></html>");
        	return;
    	}
    	PrintWriter out = response.getWriter();
    	out.println("<html><body>");
    	out.println("<h1>查無資料</h1>");
    	out.println("</body></html>");
    }
    
    //新增資料
    @RequestMapping("/insertData")
    public Object insertData(HttpServletRequest request) throws ParseException {

    	Map<String, Object> params = handleRequestParameter(request);
    	
    	return apiService.insertData(params);

    }
    
    //刪除資料
    @RequestMapping("/deletaData")
    public Object deletaData(HttpServletRequest request) throws ParseException {
    	Map<String, Object> params = handleRequestParameter(request);
    	
    	return apiService.deleteData(params);

    }
    //更新資料
    @RequestMapping("/updateData")
    public Object updateData(HttpServletRequest request) throws ParseException {
    	Map<String, Object> params = handleRequestParameter(request);
    	
    	return apiService.updateDate(params);

    }
    
    
    //Request 參數取得
    public Map<String, Object> handleRequestParameter(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		Enumeration<String> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value =request.getParameter(key);
			System.out.println(key + " : " + value);
			if (isJSONValid(value)) {
				params.put(key, new Gson().fromJson(value, HashMap.class));
			} else {
				params.put(key, value);
			}
		}
		return params;
	}
    
    public static boolean isJSONValid(String jsonInString) {
		try {
			new Gson().fromJson(jsonInString, HashMap.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}