package com.oriun.oriun.Repositories;

import com.oriun.oriun.Models.AlqElem;
import com.oriun.oriun.Models.AlquilerModel;
import com.oriun.oriun.Models.User_eventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface AlquilerRepository extends JpaRepository<AlquilerModel,Integer> {
    @Query(value = "SELECT alquiler.*,name_sport,description, available, element_name FROM oriun_db.alquiler LEFT JOIN element ON alquiler.id_element=element.id_element WHERE name_location=?1 order by rent_date ,rent_time,rent_duration",
            nativeQuery = true)
    List<AlqElem> LAlquilerplus(String nloc);
    @Query(value = "SELECT * FROM oriun_db.alquiler WHERE id_element=?1 AND rent_date=?2",
            nativeQuery = true)
    List<AlquilerModel> LAfechaElemento(int ide, Date rdate);
    @Query(value = "SELECT rent_date,rent_time,rent_duration, id_rent,user_name,element.* FROM oriun_db.alquiler LEFT JOIN element ON alquiler.id_element=element.id_element WHERE user_name=?1",
            nativeQuery = true)
    List<AlqElem> LAbyUser(String usern);
    @Query(value = "SELECT * FROM oriun_db.alquiler WHERE rent_date=?1",
            nativeQuery = true)
    List<AlquilerModel> LAbyDate(Date rdate);
    @Query(value = "SELECT * FROM oriun_db.alquiler WHERE id_element=?1",
            nativeQuery = true)
    List<AlquilerModel> LAbyElemento(int ide);
    
    @Query(value = "SELECT * FROM oriun_db.alquiler  WHERE ID_ELEMENT = ?4 AND RENT_DATE = ?1 AND (RENT_TIME BETWEEN ?2 AND ?3 OR RENT_DURATION BETWEEN ?2 AND ?3 )",
            nativeQuery = true)
    List<AlquilerModel>IsRented(Date rdate,Time itime, Time ftime, int id_elem);
}
