package com.oriun.oriun.Repositories;
import com.oriun.oriun.Models.User_eventModel;
import com.oriun.oriun.Models.User_eventPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
@Repository
public interface User_eventRepository extends JpaRepository<User_eventModel,User_eventPK>{

    @Query(value = "SELECT * FROM oriun_db.user_event  WHERE USER_NAME = ?1 ",
       nativeQuery = true)
    List<User_eventModel>findByUSER_NAME(String username);
    /*
    @Query("select a from user_event a where a. USER_NAME = ?1" )
    public List<User_eventModel>findByUSER_NAME(String username);*/
    @Query(value = "SELECT * FROM oriun_db.user_event  WHERE ID_EVENT = ?1 ",
            nativeQuery = true)
    List<User_eventModel>findByID_EVENT(int idevent);
    
    @Query(value = "SELECT * FROM oriun_db.user_event  WHERE ID_EVENT = ?1 AND USER_NAME = ?2 LIMIT 1",
            nativeQuery = true)
    List<User_eventModel>findUSER_NAMEinID_EVENT(int idevent,String username);

    @Modifying
    @Query(value = "DELETE oriun_db.user_event WHERE ID_EVENT = ?1"
    , nativeQuery = true)
    void deleteUserEvent(int id_event);
}
