package SecureChat;

import SecureChat.network.Sender;
import SecureChat.network.Receiver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Sender sender;
    private ObservableList<Person> people;
    private Receiver receiver;

    @FXML
    private ListView<Person> listPeople;

    @FXML
    private ListView<String> listMessage;

    @FXML
    private Button btnConnect,
            btnSend,
            btnJohn,
            btnLilly;

    @FXML
    private TextArea taMessage;

    @FXML
    void connect(ActionEvent event) {
        listPeople.getSelectionModel().getSelectedItem();
        Person personToConnect = listPeople.getSelectionModel().getSelectedItem();
        sender = new Sender(personToConnect);
        btnConnect.setDisable(true);
    }

    @FXML
    void runAsJohn(ActionEvent event) {
        btnLilly.setDisable(true);
        btnJohn.setText("Running as John");
        Receiver receiver = new Receiver(
                people.stream()
                        .filter(person -> person.getName().equals("john"))
                        .findFirst().get());
        receiver.setMessageList(listMessage.getItems());
    }

    @FXML
    void runAsLilly(ActionEvent event) {
        btnJohn.setDisable(true);
        btnLilly.setText("Running as Lilly");
        receiver = new Receiver(
                people.stream()
                        .filter(person -> person.getName().equals("lilly"))
                        .findFirst().get());
        receiver.setMessageList(listMessage.getItems());
    }

    @FXML
    void send(Event event) {
        String message = taMessage.getText();
        if (message.isEmpty()) return;
        sender.send(message);
        listMessage.getItems().add("me: " + message);
        taMessage.clear();
    }

    @FXML
    void lookForEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            send(event);
            event.consume();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpPeopleList();
        listPeople.setItems(people);
    }

    private void setUpPeopleList() {
        people = FXCollections.observableArrayList(
                new Person("john", "127.0.0.1", 8888, new byte[]{-125, -110, -69, -88, -63, 107, 16, -4, -58, -116, -111, 21, 28, 16, 41, 15, -122, 57, 18, 5, -84, -100, -38, -39, 77, 110, -95, -59, -106, 102, -112, -111}),
                new Person("lilly", "127.0.0.1", 8889, new byte[]{-123, -48, 45, 63, -79, 17, 105, 122, 112, 19, -27, 112, -93, -35, 126, -2, 21, -7, 101, -51, 22, 28, 28, -113, -128, -58, 74, -19, -3, 9, 16, 114}),
                new Person("frank", "127.0.0.1", 8899, new byte[]{-48, -120, 10, 2, -100, 77, -105, -35, 13, 60, -40, -127, 65, -101, 42, -113, -49, 16, -81, -88, -96, -126, 49, -107, -104, -22, 49, -96, -70, 118, 26, -3})
        );
    }
}
