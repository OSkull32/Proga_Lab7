package common.interaction.responses;

import java.io.Serializable;

/**
 * Класс для получения значения ответа с сервера.
 */
public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;

    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
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

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
