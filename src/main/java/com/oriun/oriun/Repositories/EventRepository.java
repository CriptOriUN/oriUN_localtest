package com.oriun.oriun.Repositories;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.ArrayList;

import com.oriun.oriun.Models.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EventRepository extends JpaRepository<EventModel,Integer>{
    @Query(value = "SELECT * FROM oriun_db.event  WHERE other_sport IS NOT NULL",
       nativeQuery = true)
    List<EventModel>findByOthers();
    @Query(value = "SELECT other_sport FROM oriun_db.event  WHERE other_sport IS NOT NULL ",
       nativeQuery = true)
    List<String>findByOtherSports();
    @Query(value = "SELECT other_sport,COUNT(*) FROM oriun_db.event  WHERE other_sport IS NOT NULL GROUP BY other_sport",
       nativeQuery = true)
    List<Object[]>findByOtherSportscount();
    @Query(value = "SELECT id_event FROM oriun_db.event  WHERE other_sport =?1 ",
       nativeQuery = true)
    List<Integer>findByOther_Sport(String other_sport);
    @Query(value = "SELECT * FROM oriun_db.event  WHERE event_init>current_date() OR (event_init=current_date() AND event_init_hour>current_time()) LIMIT ?1,?2",
            nativeQuery = true)
    ArrayList<EventModel>findCurrent(int init,int size);
    @Modifying
    @Query(value = "UPDATE oriun_db.event SET name_sport = ?1, other_sport= NULL WHERE other_sport = ?1"
    , nativeQuery = true)
    void updateEventSport(String new_sport);
    @Modifying
    @Query(value = "UPDATE oriun_db.event SET name_loc_sport=?2,name_sport=?3,event_description=?4,event_init=?5,event_end=?6,capacity=?7,other_sport=?8,event_init_hour=?9,event_finish_hour=?10,event_title=?11 WHERE id_event=?1"
            , nativeQuery = true)//user_name=?12,
    void updatebyID(int id_event, String name_loc_sport, String nsport, String edescription, Date event_i, Date event_e, double cap, String Oth_sport, Time eih,Time efh,String event_title);//, String user_name
    
    @Query(value = "SELECT oriun_db.event.* FROM oriun_db.user_event inner join oriun_db.event ON oriun_db.event.id_event = oriun_db.user_event.id_event where oriun_db.user_event.user_name=?1 and oriun_db.event.user_name!=?1 and (event_init>current_date() OR (event_init=current_date() AND event_init_hour>current_time()))",
            nativeQuery = true)
    List<EventModel>findUserEventsAsist(String user_name);

    @Query(value = "SELECT * FROM oriun_db.event  where oriun_db.event.user_name=?1 and (event_init>current_date() OR (event_init=current_date() AND event_init_hour>current_time()))",
            nativeQuery = true)
    List<EventModel>findUserEvents(String user_name);
    @Modifying
    @Query(value = "DELETE FROM oriun_db.event WHERE (id_event = ?1)"
    , nativeQuery = true)
    void deleteEvent(int id_event);
    
    @Query(value = "SELECT id_event,event_init,event_init_hour,event_end,event_finish_hour,name_loc_sport FROM oriun_db.event  WHERE event_init= ?1",
            nativeQuery = true)
    List<Object>findDateEvents(Date date);

    @Query(value = "SELECT id_event,event_init,event_init_hour,event_end,event_finish_hour,name_loc_sport FROM oriun_db.event  WHERE event_init>= ?1 AND event_init<= ?2 ORDER BY event_init,event_init_hour",
            nativeQuery = true)
    List<Object>findWeekEvents(Date date,Date enddate);
    @Query(value = "SELECT id_event FROM oriun_db.event  WHERE name_loc_sport=?1 AND name_sport=?2 AND ((TIMESTAMP(event_end,event_finish_hour) BETWEEN TIMESTAMP(?3,?4) AND TIMESTAMP(?5,?6)) OR (TIMESTAMP(event_init,event_init_hour) BETWEEN TIMESTAMP(?3,?4) AND TIMESTAMP(?5,?6)) OR (TIMESTAMP(?3,?4) BETWEEN TIMESTAMP(event_init,event_init_hour) AND TIMESTAMP(event_end,event_finish_hour))) LIMIT 1",
            nativeQuery = true)
    List<Integer> findSimilarEvents(String loc, String sport, Date datei,Time timei, Date datee,Time timee);
    /*@Query(value = "SELECT * FROM oriun_db.event  WHERE event_title= "+event_t,
            nativeQuery = true)
    List<String>findByEvent_title(String event_t);*/
}