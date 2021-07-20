package com.oriun.oriun.Repositories;
import com.oriun.oriun.Models.ConfirmationTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenModel,Integer>{
    @Query(value = "SELECT * FROM oriun_prueba.confirmation_token  WHERE confirmation_token = ?1 LIMIT 1",
    nativeQuery = true)
    ConfirmationTokenModel findByConfirmationToken(String confirmationToken);
    @Query(value = "SELECT * FROM oriun_prueba.confirmation_token  WHERE USER_NAME = ?1 LIMIT 1",
            nativeQuery = true)
    ConfirmationTokenModel findByUser(String user);
    @Modifying
    @Query(value = "UPDATE oriun_prueba.confirmation_token SET confirmation_token = ?2, CREATE_DATE= ?3 WHERE USER_NAME= ?1",
            nativeQuery = true)
    int chancetoken(String user_name, String token, Date cdate);
}
