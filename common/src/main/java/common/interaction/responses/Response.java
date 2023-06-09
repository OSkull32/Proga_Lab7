package common.interaction.responses;

import common.interaction.User;

import java.io.Serializable;

/**
 * Класс для получения значения ответа с сервера.
 */
public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;
    private User user;

    public Response(ResponseCode responseCode, String responseBody, User user) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.user = user;
    }

    /**
     * @return Код ответа с сервера
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * @return Тело ответа с сервера
     */
    public String getResponseBody() {
        return responseBody;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
