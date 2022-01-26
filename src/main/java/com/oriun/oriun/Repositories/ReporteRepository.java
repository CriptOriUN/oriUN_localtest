package com.oriun.oriun.Repositories;

import com.oriun.oriun.Models.ReporteModel;
import com.oriun.oriun.Models.ReporteEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteModel,Integer> {
    @Query(value = "SELECT * FROM oriun_db.reporte  WHERE user_name=?1 AND id_event=?2 LIMIT 1",
            nativeQuery = true)
    List<ReporteModel> findByReporterandIEvent(String user, int ide);

    @Modifying
    @Query(value = "DELETE FROM oriun_db.reporte WHERE id_event = ?1"
            , nativeQuery = true)
    void deletebyEvent(int id_event);

    /*@Query(value = "SELECT event.* FROM oriun_db.reporte left join event on reporte.id_event=event.id_event GROUP BY reporte.id_event"
            , nativeQuery = true)
    List<EventModel> findallWithEvent();*/

    @Query(value = "SELECT event.*,COUNT(*) as count FROM oriun_db.reporte left join event on reporte.id_event=event.id_event GROUP BY reporte.id_event"
            , nativeQuery = true)
    List<ReporteEvent> findallWithEvent();
}
