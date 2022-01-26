package com.oriun.oriun.Repositories;
import com.oriun.oriun.Models.LocationsibuModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Blob;
import java.util.ArrayList;

//@Repository
public interface LocationsibuRepository extends JpaRepository<LocationsibuModel,String>{
    @Query(value = "SELECT name_location FROM oriun_db.locationsibu",
            nativeQuery = true)
    ArrayList<String> ListLsibu();
    @Modifying
    @Query(value = "UPDATE oriun_db.locationsibu SET OPEN=?2, IMAGE_LOCATION=?3 WHERE NAME_LOCATION= ?1",
            nativeQuery = true)
    int updatebyID(String name, boolean open, byte[] im);
    @Modifying
    @Query(value = "UPDATE oriun_db.locationsibu SET IMAGE_LOCATION=?2 WHERE NAME_LOCATION= ?1",
            nativeQuery = true)
    int updateimg(String name, byte[] im);
    @Modifying
    @Query(value = "UPDATE oriun_db.locationsibu SET OPEN=NOT OPEN WHERE NAME_LOCATION= ?1",
            nativeQuery = true)
    int changeOpenbyID(String name);
}