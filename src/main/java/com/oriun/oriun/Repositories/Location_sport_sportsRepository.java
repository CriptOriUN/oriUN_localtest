package com.oriun.oriun.Repositories;
import com.oriun.oriun.Models.Location_sportsPK;

import java.util.ArrayList;

import com.oriun.oriun.Models.Location_sport_sportsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//@Repository
public interface Location_sport_sportsRepository extends JpaRepository<Location_sport_sportsModel,Location_sportsPK>{
    @Query(value = "SELECT NAME_SPORT FROM oriun_db.location_sport_sports where NAME_LOC_SPORT= ?1 ",
            nativeQuery = true)
    ArrayList<String> ListsportsbyLocation(String name_location);
}