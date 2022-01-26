package com.oriun.oriun.Repositories;
import java.util.List;

import com.oriun.oriun.Models.User_sportsModel;
import com.oriun.oriun.Models.User_sportsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface User_sportsRepository extends JpaRepository<User_sportsModel,User_sportsPK>{
    @Query(value = "SELECT * FROM oriun_db.user_sports  WHERE USER_NAME = ?1 ",
    nativeQuery = true)
    List<User_sportsModel>findByUSER_NAME(String username);
}