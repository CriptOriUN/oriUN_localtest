package com.oriun.oriun.Repositories;

import com.oriun.oriun.Models.NotificationModel;

import java.sql.Time;
import java.time.LocalDate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.sql.Date;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel,Integer> {
    @Query(value = "SELECT * FROM oriun_db.notifications  WHERE NOTIFICATION_DATE >= NOW() AND NAME_SPORT = ?1 ",
       nativeQuery = true)
    List<NotificationModel>findByNAME_SPORT(String sport);

    @Query(value = "SELECT * FROM oriun_db.notifications  WHERE NOTIFICATION_DATE = ?1 ",
       nativeQuery = true)
    List<NotificationModel>findByNOTIFICATION_DATE(Date date);

    @Query(value = "SELECT * FROM oriun_db.notifications  WHERE NOTIFICATION_DATE >= ?1 ", 
       nativeQuery = true)
    List<NotificationModel>findByAVTIVE_DATE(Date date);
    
    @Query(value = "SELECT * FROM oriun_db.notifications  WHERE ID_EVENT = ?1 ",
    nativeQuery = true)
    List<NotificationModel>findByEVENT(int id_event);
    
    @Modifying
    @Query(value = "UPDATE oriun_db.notifications SET name_sport = ?1 WHERE id_event = ?2"
    , nativeQuery = true)
    void updateNotificationSport(String new_sport, int id_event);

    @Modifying
    @Query(value = "DELETE oriun_db.notifications WHERE ID_EVENT = ?1"
    , nativeQuery = true)
    void deleteNotification(int id_event);
   @Query(value= "SELECT oriun_db.notifications.* FROM oriun_db.notifications inner join oriun_db.user_sports ON oriun_db.notifications.name_sport = oriun_db.user_sports.name_sport where oriun_db.user_sports.user_name = ?1 and oriun_db.notifications.NOTIFICATION_DATE >= NOW()"
    , nativeQuery= true)
   List<NotificationModel> findByUsername(String username);
    @Modifying
    @Query(value = "UPDATE oriun_db.notifications inner join event on notifications.id_event=event.id_event SET notifications.name_sport = ?1  where event.other_sport=?1"
            , nativeQuery = true)
    int updateNotificationOtherSport(String new_sport);
    @Modifying
    @Query(value = "UPDATE oriun_db.notifications SET name_sport=?2,notification_date=?3,time_notification=?4,expiration_time=?5,notification_description=?6 WHERE id_event=?1"
            , nativeQuery = true)
    int updateNotificationbyIdevent(int id_event, String name_sport, Date not_date, Time t_notification,Time t_expiration,String not_desc);
}
