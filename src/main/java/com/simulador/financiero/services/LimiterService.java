package com.simulador.financiero.services;

import org.springframework.stereotype.Service;

import com.simulador.financiero.Exceptions.RequestDenied;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.controllers.GlobalExceptionHandler;

@Service 
public class LimiterService {

    private int request;
    private long windowStart=System.currentTimeMillis();

    public boolean allowRequest(){
        long now=System.currentTimeMillis();

        if(now - windowStart  >= 60000){
            request=0;
            windowStart=now;
        }

        if(request>=50){
            throw new RequestDenied(ExceptionMessageConstants.TOO_MANY_REQUEST);
        }
        
        request ++;
        return true;
    }
}
