package Test.Demo.fw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Test.Demo.fw.entity.Data;

@Repository
public interface DataRepository  extends JpaRepository<Data, String>{
	@Transactional
	@Modifying
	@Query(value = "insert into data values ( ?1, ?2, ?3, ?4, ?5, ?6)"
			, nativeQuery=true)
	int insert(String code, String cname, String rate, String description, double rate_float
			,String symbol);
	
	@Transactional
	@Modifying
	@Query(value = "delete from data where code = ?1"
			, nativeQuery=true)
	int delete(String code);
	
	@Query(value = "select * from data where code = ?1"
			, nativeQuery=true)
	Data findByCode(String code);
	
}