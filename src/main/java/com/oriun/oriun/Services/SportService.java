package com.oriun.oriun.Services;
import java.util.ArrayList;

import javax.transaction.Transactional;

import com.oriun.oriun.Models.SportModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oriun.oriun.Repositories.SportRepository;

@Service
@Transactional
public class SportService {
    @Autowired
    
    SportRepository sportRepository;

    public ArrayList<SportModel> getSports(){
        return (ArrayList<SportModel>)sportRepository.findAll();
    }
    public SportModel saveSport(SportModel sport){
        return sportRepository.save(sport);
    }
}
