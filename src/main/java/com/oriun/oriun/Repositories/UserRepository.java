package com.oriun.oriun.Repositories;
import java.util.List;

import com.oriun.oriun.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//@Repository
public interface UserRepository extends JpaRepository<UserModel,String>{
  //@Query("select u from user u where u.USER_NAME = ?1")
  //UserModel findByUsername(String username);
  @Query(value = "SELECT * FROM oriun_db.user  WHERE EMAIL = ?1 LIMIT 1 ",
    nativeQuery = true)
    UserModel findByEMAIL(String email);

  @Modifying
  @Query(value = "UPDATE oriun_db.user SET ENABLED = 1 WHERE USER_NAME= ?1",
          nativeQuery = true)
  void updateUserState(String user_name);
  @Modifying
  @Query(value = "UPDATE oriun_db.user SET PASSWORD = ?2 WHERE USER_NAME= ?1",
          nativeQuery = true)
  void updateUserPassword(String user_name,String password);
  @Modifying
  @Query(value = "UPDATE oriun_db.user SET ENABLED = 0 WHERE USER_NAME= ?1",
          nativeQuery = true)
  int disableUserState(String user_name);
  @Modifying
  @Query(value = "UPDATE oriun_db.user SET NBANNED = least(NBANNED+1,3), LAST_BAN=curdate() WHERE USER_NAME= ?1",
          nativeQuery = true)
  int bannedUserState(String user_name);
  @Modifying
  @Query(value = "UPDATE oriun_db.user SET NBANNED =greatest(NBANNED-1,0) WHERE USER_NAME= ?1",
          nativeQuery = true)
  int chanceUser(String user_name);
  @Query(value = "SELECT USER_NAME FROM oriun_db.user  WHERE USER_NAME= ?1 AND (NBANNED>2 OR (NBANNED>0 AND LAST_BAN =curdate())) LIMIT 1",
          nativeQuery = true)
  List<String> UserisBanned(String user_name);
  @Query(value = "SELECT USER_NAME FROM oriun_db.user  WHERE NBANNED>2 OR (NBANNED>0 AND LAST_BAN =curdate()) LIMIT ?1,?2",
          nativeQuery = true)
  List<String> UsersBanned(int init,int size);
}
