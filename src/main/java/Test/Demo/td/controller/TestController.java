package Test.Demo.td.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Test.Demo.td.bean.ResponseBean;
import Test.Demo.td.service.ApiService;

@RestController
public class TestController {
	@Autowired
	private ApiService apiService;
	
    @RequestMapping("test")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	ResponseBean responseBean = apiService.getCoindeskAPI();
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
}