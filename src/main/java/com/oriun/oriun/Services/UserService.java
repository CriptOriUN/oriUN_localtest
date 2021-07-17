package com.oriun.oriun.Services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import com.oriun.oriun.Models.UserModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.oriun.oriun.Repositories.UserRepository;

@Service
@Transactional
public class UserService {
    @Autowired
    
    UserRepository userRepository;

    public ArrayList<UserModel> getUsers(){
        return (ArrayList<UserModel>)userRepository.findAll();
    }
    public Optional< UserModel>  getUser(String name){
        return userRepository.findById(name);
    } 
    public UserModel getUserByEmail(String email){
        return userRepository.findByEMAIL(email);
    }
    public void updateUserState(String user_name){
        userRepository.updateUserState(user_name);
    }
    public void updateUserPassword(String user_name,String password){
        userRepository.updateUserPassword(user_name,password);
    }
    public UserModel saveUser(UserModel user){
        return userRepository.save(user);
    }
    public UserModel updateUser(String userid,UserModel newuser) {
        Optional<UserModel> olduser = userRepository.findById(userid);
        if(olduser.isPresent()){
            userRepository.delete(olduser.get());
            UserModel updatedUser = userRepository.save(newuser);
            return updatedUser;
        }else{
            return olduser.get();
        }
    }
    public Boolean existUser(String userid){
        return userRepository.existsById(userid);
    }
    public boolean disUser(String userid){
        if(userid!=null && userRepository.existsById(userid)){
            return (userRepository.disableUserState(userid)>0);
        }
        return false;
    }
    public boolean banUser(String userid){
        if(userid!=null && userRepository.existsById(userid)){
            return (userRepository.bannedUserState(userid)>0);
        }
        return false;
    }
    public boolean chanceUser(String userid){
        if(userid!=null && userRepository.existsById(userid)){
            return (userRepository.chanceUser(userid)>0);
        }
        return false;
    }
    public boolean Userisbanned(String userid){
        if(userid!=null && userRepository.existsById(userid)){
            return (userRepository.UserisBanned(userid).size()>0);
        }
        return false;
    }
    public List<String> UsersBanned(int init,int size){
        if(size<1)size=1000;
        if(init<1)init=0;
        return userRepository.UsersBanned(init,size);
    }
}
