package org.example.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.dto.PaymentDto;
import org.example.dto.UserDto;
import org.example.model.User;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class JwtService {
    //TODO: лучше это в env переменной хранить
    private static final String SECRET = "38c86b553adefeb3e579e39789e38664aef9176e";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier verifier = JWT.require(algorithm).build();

    private final UserService userService;

    public JwtService(UserService userService) {
        this.userService = userService;
    }

    public String create(User user) {
        try {
            return JWT.create()
                    .withClaim("userId", user.getId())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Can't create JWT.");
        }
    }

    public UserDto find(String jwt) {
        try {
            DecodedJWT decodedJwt = verifier.verify(jwt);
            Long id = decodedJwt.getClaim("userId").asLong();
            User user = userService.findById(id);
            List<PaymentDto> paymentList = userService.getPayments(id)
                    .stream()
                    .map(payment -> {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
                        String date = payment.getDate().format(formatter);
                        return new PaymentDto(payment.getUser_id(), payment.getAmount(), payment.getType(), date);
                    })
                    .toList();
            return new UserDto(user.getName(),
                    user.getSurname(),
                    user.getPhoneNumber(),
                    user.getBalance(),
                    paymentList);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}
