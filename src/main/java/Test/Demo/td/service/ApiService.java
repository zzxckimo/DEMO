package Test.Demo.td.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
	
	public ResponseBean getCoindeskAPI() {
		InputStreamReader streamReader = null;
		BufferedReader in = null;
		HttpURLConnection con = null;
		ResponseBean responseBean = new ResponseBean();
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
				System.out.println(content);
				
				// 解析結果
				responseBean = new Gson().fromJson(content.toString(), ResponseBean.class);
				
				DateFormat  df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				SimpleDateFormat  dfUTC = new SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.US);
				SimpleDateFormat  dfBST = new SimpleDateFormat("MMM d, yyyy 'at' HH:mm", Locale.UK);
				SimpleDateFormat  dfCST = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
				
				System.out.println(df.format(dfUTC.parse(responseBean.getTime().getUpdated())));
				System.out.println(df.format(dfBST.parse(responseBean.getTime().getUpdateduk())));
				System.out.println(df.format(dfCST.parse(responseBean.getTime().getUpdatediso())));
				
			} else {
				// 非200 讀取error stream
				streamReader = new InputStreamReader(con.getErrorStream());
				in = new BufferedReader(streamReader);
				responseBean = null;
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
		return responseBean;
	}
	
	public void insertDB( ResponseBean responseBean )  {
		System.out.println(dataRepository.findAll());
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
	
}
