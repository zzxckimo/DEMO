package Test.Demo.fw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import Test.Demo.fw.entity.Data;

@Repository
public interface DataRepository  extends JpaRepository<Data, Integer>{

}