package Main;

import javafx.application.Platform;
import javafx.scene.control.TreeTableView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import popups.*;
import util.GameConfig;
import util.SwitchSceneTo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameClient {

    //public static final String ONLINE_SYMBOL = "*";

    // Socket related variables
    final AtomicBoolean running = new AtomicBoolean(false); // Ignore it for now
    private Thread listenToServerThread;
    private Socket clientSocket;
    private DataInputStream dis;
    private PrintStream ps;

    // Game related variables
    private int gameId;   // -1 is the default, means this client is not in a game
    private String userName;   // null is the default, means user hasn't logged in (or signed up) yet
    private String opponentName;   // null is the default, gets a value when game is accepted by opponent
    private int symbol;
    private boolean myTurn;
    private int score;
    private int opponentScore;

    // Ui related variables
    private TreeTableView<String> playersTableTV = null;


    public GameClient(){
        try {
            this.gameId = -1;
            this.userName = null;
            this.opponentName = null;
            this.symbol = 0;
            this.myTurn = false;
            this.score = 0;

            this.clientSocket = new Socket("localhost", 5000);
            this.dis = new DataInputStream(this.clientSocket.getInputStream());
            this.ps= new PrintStream(this.clientSocket.getOutputStream());

            this.listenToServerThread = new Thread(()->{
                this.listenToServer();
            });
            this.listenToServerThread.start();

        } catch (IOException e) {
            // Handle exception here if user tries to connect and server is down
            //Platform.runLater(()-> PopupWindow.display("Can't connect to server! Please try again later."));
            e.printStackTrace();
        }
    }

    private void listenToServer(){
        running.set(true);
        while (running.get()){
            String str= null;
            try {
                str = dis.readLine();
                handleServerResponse(str);
            } catch (IOException e) {
                // Handle here server sudden shut down while client is running
                e.printStackTrace();
                closeConnection();
                //Platform.runLater(()->PopupWindow.display("Server connection lost! Please try again later."));
            }
        }
        System.out.println("Closing");
    }

    private void closeConnection(){
        this.running.set(false);
        this.ps.close();
        try {
            this.clientSocket.close();
            this.dis.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("Socket Closed");
    }

    private void handleServerResponse(String reply){
        JSONObject replyJson = parseStringToJsonObject(reply);
        String type = replyJson.get("type").toString();
        if(type.equals("startGame")){
            int id = Integer.parseInt(replyJson.get("gameId").toString());
            String opponent = replyJson.get("opponent").toString();
            String myTurn = replyJson.get("myTurn").toString();
            this.gameId = id;
            this.opponentName = opponent;
            this.opponentScore = Integer.parseInt(replyJson.get("opponentScore").toString());
            if(myTurn.equals("true")){
                this.symbol = 1;  // 'X'
                this.myTurn = true;
            }else{
                this.symbol = -1; // 'O'
                this.myTurn = false;
            }
            Platform.runLater(() -> {
                GameConfig.setGameMode(2);
                SwitchSceneTo.showScene(5);
            });
        }else if(type.equals("gameTurnResult")) {
            handleGameTurnResult(replyJson);
        }else if(type.equals("loginResult")){
            String success = replyJson.get("success").toString();
            if(success.equals("true")){
                this.userName = replyJson.get("userName").toString();
                this.score = Integer.parseInt(replyJson.get("score").toString());
                Platform.runLater(() -> SwitchSceneTo.showScene(1));
            }else{
                Platform.runLater(() -> PopupWindow.display("Error. Check your credentials!"));
            }
        }else if(type.equals("signupResult")){
            String success = replyJson.get("success").toString();
            if(success.equals("true")){
                this.userName = replyJson.get("userName").toString();
                this.score = Integer.parseInt(replyJson.get("score").toString());
                Platform.runLater(() -> SwitchSceneTo.showScene(1));
            }else{
                Platform.runLater(() -> PopupWindow.display("Error. Check your credentials!"));
            }
        }else if(type.equals("usersList")){
            JSONArray users = (JSONArray) replyJson.get("users");
            EntryPoint.getViewUpdater().updateLeaderBoardTableView(users);
        }else if(type.equals("newLoggedInUser")){
            String newLoggedInUser = replyJson.get("loggedInUser").toString();
            EntryPoint.getViewUpdater().updateLoggedInUser(newLoggedInUser);
        }else if(type.equals("newSignedUpUser")){
            String newLoggedInUser = replyJson.get("signedUpUser").toString();
            EntryPoint.getViewUpdater().updateSignedUpUser(newLoggedInUser);
        }else if(type.equals("loggedOutUser")){
            String loggedOutUser = replyJson.get("loggedOutUser").toString();
            EntryPoint.getViewUpdater().updateLoggedOutUser(loggedOutUser);
        }else if(type.equals("gameTerminated")){
            //this.cleanAfterGameIsOver();
            Platform.runLater(()->{
                PopupWindow.display("Error: Opponent left!");
                SwitchSceneTo.showScene(1);
            });
        }else if(type.equals("startGameRequest")){
            String opponentName = replyJson.get("opponentName").toString();
            if(GameConfig.getGameMode() == 1 ){  // User is busy
                JSONObject sendToServer = new JSONObject();
                sendToServer.put("type", "startGameResponse");
                sendToServer.put("result", false);
                sendToServer.put("opponent", opponentName);
                this.ps.println(sendToServer.toJSONString());
            }else{
                Platform.runLater(()->{
                    GameRequestPopUp gameRequestPopUp = new GameRequestPopUp();
                    boolean isGameAccepted = gameRequestPopUp.display(opponentName);
                    JSONObject sendToServer = new JSONObject();
                    sendToServer.put("type", "startGameResponse");
                    sendToServer.put("result", isGameAccepted);
                    sendToServer.put("opponent", opponentName);
                    this.ps.println(sendToServer.toJSONString());
                });
            }

        }else if(type.equals("gameRejected")){
            String error = replyJson.get("error").toString();
            Platform.runLater(()->{
                PopupWindow.display(error);
            });
        }else if(type.equals("savedGamesList")){
            JSONArray games = (JSONArray) replyJson.get("games");
            EntryPoint.getViewUpdater().updateGamesList(games);
        }else if(type.equals("savedGame")){
            String playerOneName = replyJson.get("playerOneName").toString();
            String playerTwoName = replyJson.get("playerTwoName").toString();
            JSONObject board = parseStringToJsonObject(replyJson.get("board").toString());
            JSONObject state = parseStringToJsonObject(replyJson.get("state").toString());
            EntryPoint.getViewUpdater().showSavedGame(playerOneName, playerTwoName, board, state);
        }
    }


    public void login(String name, String password){
        JSONObject object = createJsonObject();
        object.put("type", "login");
        object.put("userName", name);
        object.put("password", password);
        this.ps.println(object.toJSONString());
    }

    public void signUp(String fullName, String name, String password, String email, String gender){
        JSONObject object = createJsonObject();
        object.put("type", "signup");
        object.put("name", fullName);
        object.put("userName", name);
        object.put("password", password);
        object.put("email", email);
        object.put("gender", gender);
        this.ps.println(object.toJSONString());
    }

    public void logOut(){
        JSONObject object = createJsonObject();
        object.put("type", "logout");
        this.ps.println(object.toJSONString());
    }

    public void requestUsers(){
        JSONObject object = createJsonObject();
        object.put("type", "getUsers");
        this.ps.println(object.toJSONString());
    }

    // Gets called from Main class (Fx UI) when user clicks on a button
    public void sendMoveToServer(String index, int symbol){
        JSONObject jsonObject = createJsonObject();
        jsonObject.put("type", "gameTurn");
        jsonObject.put("gameId", this.gameId);
        jsonObject.put("index", index);
        jsonObject.put("symbol", symbol);
        this.ps.println(jsonObject.toJSONString());
    }

    public void startGameWithOpponent(String opponentName){
        JSONObject jsonObject = createJsonObject();
        jsonObject.put("type", "tryGameWithOpponent");
        jsonObject.put("opponent", opponentName);
        this.ps.println(jsonObject.toJSONString());
    }

    private void handleGameTurnResult(JSONObject replyJson){
        String won = replyJson.get("won").toString();
        String lost = replyJson.get("lost").toString();
        String tie = replyJson.get("tie").toString();
        // Displaying game turn result
        EntryPoint.getViewUpdater().updateBoard(replyJson);
        // Check win, lose or tie conditions
        if(won.equals("false") && lost.equals("false") && tie.equals("false")){ // Game is still running
            if(replyJson.get("myTurn").toString().equals("true")){
                this.myTurn = true;
            }else{
                this.myTurn = false;
            }
        }else if(won.equals("true")){  // This user has won
            Platform.runLater(()->{
                WinnerPopup winnerPopup = new WinnerPopup();
                winnerPopup.display();
            });
            this.score += 10;
            //this.cleanAfterGameIsOver();
        }else if(lost.equals("true")){  // This user has lost
            Platform.runLater(()->{
                LoserPopUp loserPopUp = new LoserPopUp();
                loserPopUp.display();
            });
            //this.cleanAfterGameIsOver();
        }else if(tie.equals("true")){  // No one has won, it's a tie (it's a trap :D)
            Platform.runLater(()->{
                TiePopUp tiePopUp = new TiePopUp();
                tiePopUp.display();
            });
            //this.cleanAfterGameIsOver();
        }
    }

    private void cleanAfterGameIsOver(){
        this.gameId = -1;
        this.opponentName = null;
        this.symbol = 0;
        this.myTurn = false;
    }

    public void getUserGames(){
        JSONObject sendToServer = new JSONObject();
        sendToServer.put("type", "getSavedGames");
        this.ps.println(sendToServer.toJSONString());
    }

    public void getSavedGame(int gameId){
        JSONObject sendToServer = new JSONObject();
        sendToServer.put("type", "getSavedGame");
        sendToServer.put("savedGameId", gameId);
        this.ps.println(sendToServer.toJSONString());
    }

    public void sendAcceptSaveGame(){
        JSONObject sendToServer = new JSONObject();
        sendToServer.put("type", "saveGame");
        sendToServer.put("gameId", this.gameId);
        this.ps.println(sendToServer.toJSONString());
    }

    public boolean getMyTurn(){
        return this.myTurn;
    }

    public int getSymbol(){
        return this.symbol;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getOpponentName(){
        return this.opponentName;
    }

    public int getGameId(){
        return this.gameId;
    }

    public int getScore(){
        return this.score;
    }

    public int getOpponentScore(){
        return this.opponentScore;
    }

    public AtomicBoolean getRunning(){
        return running;
    }

    private JSONObject createJsonObject(){
        return new JSONObject();
    }

    private JSONObject parseStringToJsonObject(String jsonString){
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
