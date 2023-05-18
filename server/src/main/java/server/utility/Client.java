package server.utility;

import common.interaction.requests.Request;
import common.interaction.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client implements Cloneable {

    private final Socket clientSocket;
    private final ObjectInputStream clientInputStream;
    private final ObjectOutputStream clientOutputStream;

    private Request request;
    private Response serverResponse;

    public Client(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public Client clone() throws CloneNotSupportedException {
        return (Client) super.clone();
    }
    //кидает ошибку если клиент всё
    public void waitRequest() throws IOException, ClassNotFoundException {
        request = (Request) clientInputStream.readObject(); //блокируется
    }

    public Request getRequest() {
        return request;
    }

    public ObjectOutputStream getClientOutputStream() {
        return clientOutputStream;
    }

    public void setServerResponse(Response serverResponse) {
        this.serverResponse = serverResponse;
    }

    public Response getServerResponse() {
        return serverResponse;
    }

    public void disconnectClient() {
        try {
            clientSocket.close();
            clientInputStream.close();
            clientOutputStream.close();
        } catch (IOException e) {

        }
    }
}
