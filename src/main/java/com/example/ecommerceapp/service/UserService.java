package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dto.UserGetDTO;
import com.example.ecommerceapp.dto.UserLoginDTO;
import com.example.ecommerceapp.dto.UserPostDTO;
import com.example.ecommerceapp.exception.EmailFailureException;
import com.example.ecommerceapp.exception.UserAlreadyExistsException;
import com.example.ecommerceapp.exception.UserNameOrPasswordNotFoundException;
import com.example.ecommerceapp.exception.UserNotVerifiedException;
import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.model.VerificationToken;
import com.example.ecommerceapp.repository.UserRepository;
import com.example.ecommerceapp.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private static final Integer ONE_HOUR = 3600;

    @Transactional
    public UserGetDTO registerUser(UserPostDTO userPostDTO) {
        if (userRepository.findByUserNameIgnoreCase(userPostDTO.getUserName()).isPresent()
                || userRepository.findByEmailIgnoreCase(userPostDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email or username already exists!");
        }
        User user = convertFromDto(userPostDTO);
        user.setPassword(encryptionService.encryptPassword(userPostDTO.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        User savedUser = userRepository.save(user);
        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(verificationToken);


        return convertToUserGetDTO(savedUser);
    }

    private VerificationToken createVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationToken(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);

        return verificationToken;
    }

    public String loginUser(UserLoginDTO userLoginDTO) {
        Optional<User> userFromDb = userRepository.findByUserNameIgnoreCase(userLoginDTO.getUserName());
        if (userFromDb.isPresent()) {
            User user = userFromDb.get();
            if (encryptionService.checkPassword(userLoginDTO.getPassword(), user.getPassword())) {
                if (user.getEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationToken = user.getVerificationTokenList();
                    boolean resend = verificationToken.size() == 0 || verificationToken.get(0).getCreatedTimestamp()
                            .before(new Timestamp(System.currentTimeMillis() - (ONE_HOUR)));
                    if(resend) {
                        VerificationToken resendVerificationToken = createVerificationToken(user);
                        verificationTokenRepository.save(resendVerificationToken);
                        emailService.sendVerificationEmail(resendVerificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        throw new UserNameOrPasswordNotFoundException();
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);
        if(optionalToken.isPresent()) {
            VerificationToken verificationToken = optionalToken.get();
            User user = verificationToken.getUser();
            if(!user.getEmailVerified()) {
                user.setEmailVerified(true);
                userRepository.save(user);
                verificationTokenRepository.deleteByUser(user);

                return true;
            }
        }
        return false;
    }

    public User convertFromDto(UserPostDTO userPostDTO) {
        return modelMapper.map(userPostDTO, User.class);
    }

    public UserGetDTO convertToUserGetDTO(User user) {
        return modelMapper.map(user, UserGetDTO.class);
    }

    public User convertToUser(UserGetDTO userGetDTO) {
        return modelMapper.map(userGetDTO, User.class);
    }
}
