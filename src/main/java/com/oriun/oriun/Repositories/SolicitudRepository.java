package com.oriun.oriun.Repositories;

import com.oriun.oriun.Models.ReporteModel;
import com.oriun.oriun.Models.SolicitudModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudModel,Integer> {
    @Query(value = "SELECT * FROM oriun_db.solicitud WHERE user_name=?1 LIMIT 1",
            nativeQuery = true)
    List<SolicitudModel> findByReporter(String user);
    @Modifying
    @Query(value = "DELETE FROM oriun_db.solicitud WHERE user_name=?1"
            , nativeQuery = true)
    void deletebyUser(String user);
}
