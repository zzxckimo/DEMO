package Test.Demo.td.service;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import Test.Demo.fw.entity.Data;
import Test.Demo.fw.repository.DataRepository;
import Test.Demo.td.bean.ResponseBean;

@Service
public class ApiService {
	
	@Autowired
	private  DataRepository dataRepository;
	
	public String getCoindeskAPI() {
		InputStreamReader streamReader = null;
		BufferedReader in = null;
		HttpURLConnection con = null;
		String contentString = null;
		try {
			URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
			con = (HttpURLConnection) url.openConnection();
			// 設定 http method "GET"
			con.setRequestMethod("GET");
			// 設定連線逾時，送到Server的時間
			con.setConnectTimeout(10000);
			// 設定回應逾時，送到Server，但Server沒回, AP不想等, 想中斷的時間
			con.setReadTimeout(15000);

			int status = con.getResponseCode();
			if (status == 200) {
				// 200 的話是讀取input stream
				streamReader = new InputStreamReader(con.getInputStream());
				
				// 將stream放入buffer reader
				in = new BufferedReader(streamReader);
				String line;
				StringBuffer content = new StringBuffer();
				// 一次讀取一行並加入到content後方
				while ((line = in.readLine()) != null) {
					content.append(line);
				}
				// 輸出結果
//				System.out.println(content);
				contentString = content.toString();
				
				
			} else {
				// 非200 讀取error stream
				streamReader = new InputStreamReader(con.getErrorStream());
				in = new BufferedReader(streamReader);
				contentString = null;
			}

			
		} catch (Exception e) {
			System.out.println("Catch exception");
			e.printStackTrace();
		} finally {
			// Close InputStreamReader
			try {
				streamReader.close();
			} catch (IOException e) {
				System.out.println("Close InputStreamReader exception");
				e.printStackTrace();
			}
			// Close BufferedReader
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Close BufferedReader exception");
				e.printStackTrace();
			}
			// Close HttpURLConnection
			con.disconnect();
			
		}
		return contentString;
	}
	
	public void insertDB( ResponseBean responseBean )  {

		dataRepository.deleteAll();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		Data dataBean =new Data();
		modelMapper.map(responseBean.getBpi().getUsd(), dataBean);
		dataRepository.save(dataBean);
		dataBean =new Data();
		modelMapper.map(responseBean.getBpi().getEur(), dataBean);
		dataRepository.save(dataBean);
		dataBean =new Data();
		modelMapper.map(responseBean.getBpi().getGbp(), dataBean);
		dataRepository.save(dataBean);
	}
	
	public List<Data> queryData(  )  {
		return dataRepository.findAll();
	}
	
	public void formatDate( ResponseBean responseBean ) throws ParseException  {
		DateFormat  df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat  dfUTC = new SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.US);
		SimpleDateFormat  dfBST = new SimpleDateFormat("MMM d, yyyy 'at' HH:mm", Locale.UK);
		SimpleDateFormat  dfCST = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
		
		responseBean.getTime().setUpdated(df.format(dfUTC.parse(responseBean.getTime().getUpdated())));
		responseBean.getTime().setUpdateduk(df.format(dfBST.parse(responseBean.getTime().getUpdateduk())));
		responseBean.getTime().setUpdatediso(df.format(dfCST.parse(responseBean.getTime().getUpdatediso())));
	}
	
	public String insertData( Map<String, Object> params ){
		
		if(params.get("code") != null && params.get("symbol") != null && params.get("rate") != null && params.get("description") != null && params.get("rate_float") != null){
			String code =  params.get("code").toString();
			String symbol =  params.get("symbol").toString();
			String rate =  params.get("rate").toString();
			String description =  params.get("description").toString();
			double rate_float = Double.parseDouble(params.get("rate_float").toString()) ;
			Data data = dataRepository.findByCode(code);
			if(data != null) {
				return "資料已存在";
			}else {
				String cname = "";
				switch(code) { 
		            case "USD": 
		            	cname = "美元";
		                break; 
		            case "EUR": 
		            	cname = "歐元";
		                break; 
		            case "GBP": 
		            	cname = "英鎊";
		                break; 
		        }
				if (dataRepository.insert(code, symbol, rate, description, rate_float, cname) > 0) {
					return "新增成功";
				}else {
					return "新增失敗";
				}
			}
		}
		return "缺少參數!";
	}
	
	public String deleteData( Map<String, Object> params ){
		
		if( params.get("code") != null ){
			String code =  params.get("code").toString();
			if (dataRepository.delete(code) > 0) {
				return "刪除成功";
			}else {
				return "刪除失敗";
			}
		}
		return "缺少參數!";
	}
	
public String updateDate( Map<String, Object> params ){
		
		if( params.get("code") != null ){
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			modelMapper.getConfiguration().setAmbiguityIgnored(true);
			Data dataBean =new Data();
			modelMapper.map(params, dataBean);
			
			Data dataOrig = dataRepository.findByCode(dataBean.getCode().toString());	
			if(dataOrig != null) {

				double rate_float = dataOrig.getRate_float();
				copyNotNullProperties(dataBean,dataOrig);
				//判斷rate_float 是否無輸入
				if(params.get("rate_float") == null) {
					dataOrig.setRate_float(rate_float);
				}
			}else {
				return "無更新資料";
			}
			
			if (dataRepository.save(dataOrig).getCode()  != null) {
				return "更新成功";
			}else {
				return "更新失敗";
			}
		}
		return "缺少參數!";
	}

	public static void copyNotNullProperties(Object src,Object target){
	    BeanUtils.copyProperties(src,target,getNullPropertyNames(src));
	}
	
	private static String[] getNullPropertyNames(Object object) {
	    final BeanWrapperImpl wrapper = new BeanWrapperImpl(object);
	    return Stream.of(wrapper.getPropertyDescriptors())
	            .map(PropertyDescriptor::getName)
	            .filter(propertyName -> wrapper.getPropertyValue(propertyName) == null)
	            .toArray(String[]::new);
	}
}
