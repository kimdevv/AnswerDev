package com.hufs.AnswerDev.Model.User;

import com.hufs.AnswerDev.Model.User.Dto.fromClient.LoginClientDto;
import com.hufs.AnswerDev.Model.User.Dto.fromClient.SignUpClientDto;
import com.hufs.AnswerDev.Model.User.Dto.fromServer.ResponseTokenServerDto;
import com.hufs.AnswerDev.Util.Enum.JwtEnum;
import com.hufs.AnswerDev.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @Async
    public CompletableFuture<ResponseTokenServerDto> signUp(SignUpClientDto dto) {
        String username = dto.getUsername();
        String rawPassword = dto.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        String nickname = dto.getNickname();
        String respectPerson = dto.getRespectPerson();

        // DB에 username과 nickname이 동일한 User가 있다면 오류 발생
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다.");
        } else if (userRepository.existsByNickname(nickname)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.");
        }

        User newUser = User.builder()
                .username(username)
                .password(hashedPassword)
                .nickname(nickname)
                .respectPerson(respectPerson)
                .build();
        userRepository.save(newUser);

        String accessToken = jwtUtil.createToken(newUser.getId(), JwtEnum.ACCESSTOKEN);
        String refreshToken = jwtUtil.createToken(newUser.getId(), JwtEnum.REFRESHTOKEN);

        ResponseTokenServerDto result = new ResponseTokenServerDto(accessToken, refreshToken);
        return CompletableFuture.completedFuture(result);
    }

    @Transactional
    @Async
    public CompletableFuture<ResponseTokenServerDto> login(LoginClientDto dto) {
         String username = dto.getUsername();
         String password = dto.getPassword();

         User theUser = userRepository.findByUsername(username);
        if (theUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 잘못되었습니다.");
        }

        if (passwordEncoder.matches(password, theUser.getPassword())) {
            String accessToken = jwtUtil.createToken(theUser.getId(), JwtEnum.ACCESSTOKEN);
            String refreshToken = jwtUtil.createToken(theUser.getId(), JwtEnum.REFRESHTOKEN);

            ResponseTokenServerDto result = new ResponseTokenServerDto(accessToken, refreshToken);
            return CompletableFuture.completedFuture(result);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 잘못되었습니다.");
        }
    }
}
