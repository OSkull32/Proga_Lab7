package common.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTService {
    private static final String SECRET = "secret123@$&#@^secret";
    private static final long DEFAULT_EXPIRE_IN_MILLISECONDS = 15 * 60 * 1000; //миллисекунд
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final String ISSUER = "My server";
    private static final String CLAIM_USERNAME = "username";

    private JWTService () {}

    public static String generateToken(String username) {
        long currentTime = (new Date()).getTime();
        Date expireDate = new Date(currentTime + DEFAULT_EXPIRE_IN_MILLISECONDS); //когда токен истекает
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(CLAIM_USERNAME, username)
                .withExpiresAt(expireDate)
                .sign(ALGORITHM);
    }

    public static boolean verifyToken(String jwtToken) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(jwtToken);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static String getUsername(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaims().get(CLAIM_USERNAME).toString().replace("\"", "");
        } catch (Exception e) {
            return "";
        }
    }
}
