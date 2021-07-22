package com.oriun.oriun.Services;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.oriun.oriun.Models.EventModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oriun.oriun.Repositories.EventRepository;
import com.oriun.oriun.Repositories.LocationsportRepository;
import com.oriun.oriun.Repositories.SportRepository;
import com.oriun.oriun.Repositories.UserRepository;
import com.oriun.oriun.Repositories.NotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
@Transactional
public class EventService {
    @Autowired
    
    EventRepository eventRepository;
    @Autowired
    LocationsportRepository locationsportRepository;
    @Autowired
    SportRepository sportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationRepository notificationRepository;

    public ArrayList<EventModel> getEvents(){
        return (ArrayList<EventModel>)eventRepository.findAll();
    }
    public List<EventModel> getUserEvents(String user_name){
        return eventRepository.findUserEvents(user_name);
    }
    public List<Object> getDateEvents(Date date){
        return eventRepository.findDateEvents(date);
    }
    public List<Object> getWeekEvents(Date date,Date enddate){
        return eventRepository.findWeekEvents(date,enddate);
    }
    public EventModel saveEvent(EventModel event){
        /*Date sqlDate = new Date(event.getEVENT_INIT().getTime()+(86400000));//60000*60*24
        event.setEVENT_INIT(sqlDate);
        sqlDate = new Date(event.getEVENT_END().getTime()+(86400000));//60000*60*24
        event.setEVENT_END(sqlDate);*/
        if(event.getOTHER_SPORT()!=null){
            event.setOTHER_SPORT(FormatText(event.getOTHER_SPORT()));
        }
        return eventRepository.save(event);
    }
    public Optional<EventModel> getEventById(int event_id) {
        return eventRepository.findById(event_id);
    }
    public ResponseEntity updateEvent(EventModel newevent) {
        int Id_ne=newevent.getID_EVENT();
        if(eventRepository.existsById(Id_ne)){
            String nl=newevent.getNAME_LOC_SPORT();
            if(!locationsportRepository.existsById(nl)){
                return new ResponseEntity<>("Ubicacion no encontrada",
                        HttpStatus.NOT_FOUND );
            }
            else{
                String ns= newevent.getNAME_SPORT();
                if(!sportRepository.existsById(ns)){
                    return new ResponseEntity<>("Deporte no encontrado",
                            HttpStatus.NOT_FOUND );
                }
                else{
                    /*String nu=newevent.getUSER_NAME();
                    if(!userRepository.existsById(nu)){
                        return new ResponseEntity<>("Usuario no encontrado",
                                HttpStatus.NOT_FOUND );
                    }
                    else {*/
                        String des =newevent.getEVENT_DESCRIPTION();
                        Date dei=new Date(newevent.getEVENT_INIT().getTime());//60000*60*24
                        Date dee=new Date(newevent.getEVENT_END().getTime());//60000*60*24
                        double cap=newevent.getCAPACITY();
                        String os=newevent.getOTHER_SPORT();
                        Time tei=newevent.getEVENT_INIT_HOUR();
                        Time tef=newevent.getEVENT_FINISH_HOUR();
                        String et = newevent.getEVENT_TITLE();
                        /*NotificationModel notification = new NotificationModel();
                        notification.setNAME_SPORT(ns);
                        notification.setID_EVENT(Id_ne);
                        notification.setNOTIFICATION_DATE(dei);
                        notification.setTIME_NOTIFICATION(tei);
                        notification.setEXPIRATION_TIME(tef);
                        notification.setNOTIFICATION_DESCRIPTION("A brand new event is coming : "+et);
                        notificationService.saveNotification(notification);*/
                        notificationRepository.updateNotificationbyIdevent(Id_ne,ns,dei,tei,tef,"Un nuevo evento ha sido creado : "+et);
                        eventRepository.updatebyID(Id_ne,nl,ns,des,dei,dee,cap,os,tei,tef,et);//,nu
                        return new ResponseEntity<>("Actualizado",
                                HttpStatus.OK);
                    //}
                }
            }
        }
        else{
            return new ResponseEntity<>("Elemento antiguo no encontrado",
                    HttpStatus.BAD_REQUEST );
        }
    }
    public EventModel deleteEvent(EventModel event){
        eventRepository.delete(event);
        return event;
    }
    public List<EventModel> getOtherEvents(){
        return eventRepository.findByOthers();
    }
    public List<String> getOtherSports(){
        return eventRepository.findByOtherSports();
    }
    public Boolean existEvent(int id_eve){
        return eventRepository.existsById(id_eve);
    }
    public List<Object[]> getOtherSportsCount(){
        return eventRepository.findByOtherSportscount();
    }
    public List<Integer> getIdEventByOther(String other_sport){
        return eventRepository.findByOther_Sport(other_sport);
    }
    public ArrayList<EventModel> getEventVigentes(int init,int size){
        if(size<1)size=10;
        if(init<1)init=0;
        return eventRepository.findCurrent(init,size);
    }
    public long Nevents(){
        return eventRepository.count();
    }
    public void updateEventSport(String sport_name){
        eventRepository.updateEventSport(sport_name);
    }
    public String creadorevento(int id_eve){
        return getEventById(id_eve).get().getUSER_NAME();
    }
    public void adiosEvent(int id_event){
        eventRepository.deleteEvent(id_event);
    }
    private String FormatText(String in){
        String out=in.toLowerCase();
        String M=String.valueOf((char)(out.charAt(0)-32));
        out=out.replaceFirst(String.valueOf(out.charAt(0)),M);
        return out;
    }
    public Boolean Eventovalido(String dep, String nu, String ld){
        if(dep!=null && nu!=null  && ld!=null){
            if( sportRepository.existsById(dep)&& userRepository.existsById(nu)&& locationsportRepository.existsById(ld)){
                return true;
            }
        }
        return false;
    }
    public Boolean EventosSimi(String loc,String sport, Date datei,Time timei, Date datee,Time timee){
        //Timestamp ts=new Timestamp(datei.getTime()+timei.getTime());
        if(eventRepository.findSimilarEvents(loc,sport,datei,timei,datee,timee).size()>0)return false;
        else return true;
    }
    public List<EventModel> getUserAsistanceEvents(String user_name) {
        return eventRepository.findUserEventsAsist(user_name);
    }
}
