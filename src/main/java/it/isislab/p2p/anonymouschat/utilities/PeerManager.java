package it.isislab.p2p.anonymouschat.utilities;

import javafx.application.Platform;
import javafx.scene.control.TextArea;;
import java.util.HashMap;
/*
*   Singleton per mantenere le informazioni del peer
 */
public class PeerManager {


    class MessageListenerImpl implements MessageListener {

        public Object parseMessage(Object obj) {

            MessageP2P mss = (MessageP2P) obj;

            //To edit UI, i'm to be sure to work on FX-main-thread
            Platform.runLater(() -> {
                TextArea chat = chatJoined.get(mss.getRoom());
                chat.appendText("\n" + mss.getMessage());
            });

            return "success";
        }


    }

    private static PeerManager instance;
    private AnonymousChatImpl peer = null;

    static {
        try {
            instance = new PeerManager();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred!", e);
        }
    }

    private int id = -1;
    private String master = null;

    private PeerManager() { }

    final private HashMap<String, TextArea> chatJoined = new HashMap<>();

    public boolean init(int _id, String _master)
    {
        if( id != -1  && master != null && peer != null) return false;
        id = _id;
        master = _master;
        try {
            peer = new AnonymousChatImpl(id, master, new MessageListenerImpl());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static PeerManager getInstance(){
        return instance;
    }

    public AnonymousChatImpl getPeer(){ return peer;}

    public HashMap getChat() { return chatJoined; }
    public void addChat(String roomName, TextArea chat) { chatJoined.put(roomName, chat); }
    public boolean isChatJoined(String chatName){   return chatJoined.containsKey((chatName)); }
    public void removeChat(String roomName){ chatJoined.remove(roomName); }
}
