package Test.Demo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import Test.Demo.fw.entity.Data;
import Test.Demo.fw.repository.DataRepository;
import Test.Demo.td.bean.ResponseBean;
import Test.Demo.td.config.Application;
import Test.Demo.td.service.ApiService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class junitTest {

	@Autowired
	private ApiService apiService;
	@Autowired
	private  DataRepository dataRepository;
	
	@Test
	public void test1() throws Exception {
		System.out.println("=================資料初始化start==================");
		String contentString = apiService.getCoindeskAPI();
    	ResponseBean responseBean = new Gson().fromJson(contentString, ResponseBean.class);
    	if(responseBean != null) {
    		apiService.insertDB( responseBean );
    	}
    	List<Data> dataList = dataRepository.findAll();
    	System.out.println("=================資料初始化end==================");
    	Assertions.assertThat(dataList.size() > 0);//用來判斷結果是否正確，如果錯誤的結果會產生Failures
	}
	@Test
	public void test2() throws Exception {
		System.out.println("=================查詢start==================");
		List<Data> dataList= apiService.queryData();
    	for(Data data : dataList) {
    		System.out.println(data);
    	}
    	System.out.println("=================查詢end==================");
    	Assertions.assertThat(dataList.size() > 0);//用來判斷結果是否正確，如果錯誤的結果會產生Failures
	}
	@Test
	public void test3() throws Exception {
		System.out.println("=================更新成功start==================");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", "USD");
		params.put("rate", "11,767.0066");
		String result= apiService.updateDate(params);
		List<Data> dataList= apiService.queryData();
    	for(Data data : dataList) {
    		System.out.println(data);
    	}
    	System.out.println("=================更新成功end==================");
    	Assertions.assertThat("更新成功".equals(result));//用來判斷結果是否正確，如果錯誤的結果會產生Failures
	}
	@Test
	public void test4() throws Exception {
		System.out.println("=================刪除start==================");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", "USD");
		String result= apiService.deleteData(params);
		List<Data> dataList= apiService.queryData();
    	for(Data data : dataList) {
    		System.out.println(data);
    	}
    	System.out.println("=================刪除end==================");
    	Assertions.assertThat("刪除成功".equals(result));//用來判斷結果是否正確，如果錯誤的結果會產生Failures
	}
	@Test
	public void test5() throws Exception {
		System.out.println("=================新增start==================");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", "USD");
		params.put("symbol", "&#36;");
		params.put("rate", "42,767.0066");
		params.put("description", "United States Dollar");
		params.put("rate_float", "42767.0066");
		params.put("cname", "美元");
		String result = apiService.insertData(params);
		List<Data> dataList= apiService.queryData();
    	for(Data data : dataList) {
    		System.out.println(data);
    	}
    	System.out.println("=================新增end==================");
    	Assertions.assertThat("新增成功".equals(result));//用來判斷結果是否正確，如果錯誤的結果會產生Failures
	}
	@Test
	public void test6() throws Exception {
		System.out.println("=================coindesk API start==================");
		String contentString = apiService.getCoindeskAPI();
		System.out.println(contentString);
	    System.out.println("=================coindesk API end==================");
	    Assertions.assertThat(contentString != null);//用來判斷結果是否正確，如果錯誤的結果會產生Failures
	}
	@Test
	public void test7() throws Exception {
		System.out.println("=================資料轉換的APIstart==================");
		String contentString = apiService.getCoindeskAPI();
    	ResponseBean responseBean = new Gson().fromJson(contentString, ResponseBean.class);
    	apiService.formatDate( responseBean );
    	String json = new Gson().toJson(responseBean);
    	System.out.println(json);
	    System.out.println("=================資料轉換的APIend==================");
	    Assertions.assertThat(json != null);//用來判斷結果是否正確，如果錯誤的結果會產生Failures
	}
}
