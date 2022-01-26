package com.oriun.oriun.Repositories;
import com.oriun.oriun.Models.ElementModel;
import com.oriun.oriun.Models.ElementBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import java.util.ArrayList;


public interface ElementRepository extends JpaRepository<ElementModel,Integer>{
    @Query(value = "SELECT * FROM oriun_db.element LIMIT ?1,?2",
            nativeQuery = true)
    ArrayList<ElementModel> findSubList(int init,int size);
    @Modifying
    @Query(value = "UPDATE oriun_db.element SET AVAILABLE =NOT AVAILABLE WHERE ID_ELEMENT= ?1",
            nativeQuery = true)
    int changeAvailablebyID(int id);
    @Modifying
    @Query(value = "UPDATE oriun_db.element SET AVAILABLE =?2,DESCRIPTION=?3,ELEMENT_NAME=?4,NAME_LOCATION=?5,NAME_SPORT=?6,ElEMENT_IMAGE=?7 WHERE ID_ELEMENT= ?1",
            nativeQuery = true)
    int updatebyID(int id_element,boolean available,String description,
                   String element_name,String name_location,String name_sport,byte[] im);
    @Modifying
    @Query(value = "UPDATE oriun_db.element SET AVAILABLE =?2,DESCRIPTION=?3,NAME_LOCATION=?4,NAME_SPORT=?5,ElEMENT_IMAGE=?6 WHERE ID_ELEMENT= ?1",
            nativeQuery = true)
    int updatebyIDnoname(int id_element,boolean available,String description,String name_location,String name_sport,byte[] im);
    @Query(value = "SELECT * FROM oriun_db.element WHERE NAME_LOCATION=?1 LIMIT ?2,?3",
            nativeQuery = true)
    ArrayList<ElementModel> findbyLocation(String name_location,int init,int size);
    @Query(value = "SELECT * FROM oriun_db.element WHERE NAME_SPORT=?1 LIMIT ?2,?3",
            nativeQuery = true)
    ArrayList<ElementModel> findbySport(String name_sport,int init,int size);
    @Query(value = "SELECT * FROM oriun_db.element WHERE AVAILABLE=TRUE LIMIT ?1,?2",
            nativeQuery = true)
    ArrayList<ElementModel> findAvailables(int init,int size);
    @Query(value = "SELECT id_element,element_name,name_location,name_sport,available,description from oriun_db.element LIMIT ?1,?2",
            nativeQuery = true)
    ArrayList<ElementBasic> ListElements(int init,int size);
    @Modifying
    @Query(value = "DELETE from oriun_db.element WHERE NAME_LOCATION=?1",
            nativeQuery = true)
    int deleteElementsinLSibu(String name);
    //public List<ElementModel>findByNAME_SPORT();
    @Modifying
    @Query(value = "UPDATE oriun_db.element SET ElEMENT_IMAGE=?2 WHERE ID_ELEMENT= ?1",
            nativeQuery = true)
    int updateimg(int id, byte[] im);
}
