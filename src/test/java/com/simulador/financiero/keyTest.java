package com.simulador.financiero;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.TempTokenRepository;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.services.email.EmailSender;
import com.simulador.financiero.services.email.TempKeyService;
import com.simulador.financiero.services.email.catalog.AccountRecoveryData;
import com.simulador.financiero.services.email.catalog.AccountRecoveryEmail;
import com.simulador.financiero.utils.TockenGenerate;
import com.simulador.financiero.validate.ValidateTemp;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


}
