package com.oriun.oriun.Repositories;
import com.oriun.oriun.Models.ElementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//@Repository
public interface ElementRepository extends JpaRepository<ElementModel,Integer>{
    //public List<ElementModel>findByNAME_SPORT();
}
