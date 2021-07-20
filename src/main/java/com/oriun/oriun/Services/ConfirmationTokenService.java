package com.oriun.oriun.Services;

import com.oriun.oriun.Models.ConfirmationTokenModel;
import com.oriun.oriun.Repositories.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConfirmationTokenService
{
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public List<ConfirmationTokenModel> getTokens(){
        return confirmationTokenRepository.findAll();
    }
    public ConfirmationTokenModel getbyToken(String token){
        return confirmationTokenRepository.findByConfirmationToken(token);
    }
    public ConfirmationTokenModel getbyUser(String user){
        return confirmationTokenRepository.findByUser(user);
    }
    public ConfirmationTokenModel saveTK(ConfirmationTokenModel token){
        return confirmationTokenRepository.save(token);
    }
    public ConfirmationTokenModel OnlyaTokennew(ConfirmationTokenModel token){
        if(getbyUser(token.getUSER_NAME())!=null) {
            confirmationTokenRepository.chancetoken(token.getUSER_NAME(), token.getCONFIRMATION_TOKEN(), token.getCREATE_DATE());
        }
        else{
            confirmationTokenRepository.save(token);
        }
        return token;
    }
    public ConfirmationTokenModel OnlyaTokenold(ConfirmationTokenModel tokennew){
        ConfirmationTokenModel tokenold = confirmationTokenRepository.findByUser(tokennew.getUSER_NAME());
        if(getbyUser(tokenold.getUSER_NAME())!=null) {
            return tokenold;
        }
        else{
            confirmationTokenRepository.save(tokennew);
            return tokennew;
        }
    }
    public void DeleteCTbyID(Integer idTK){
        confirmationTokenRepository.deleteById(idTK);
    }
}
