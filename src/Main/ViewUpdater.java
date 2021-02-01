package Main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.GameConfig;
import util.SwitchSceneTo;

import java.util.LinkedHashMap;
import java.util.Optional;

public class ViewUpdater {

    private TableView<PlayerRow> leaderBoardTable;
    private TableView<GameRow> savedGamesTable;
    private Button[] buttons;

    private String replayPlayerOneName;
    private String replayPlayerTwoName;
    // Note we use a LinkedHashMap not a regular map because order matters here
    private LinkedHashMap<Integer, String> buttonsMap;

    public void setLeaderBoardTable(TableView<PlayerRow> leaderBoardTable) {
        this.leaderBoardTable = leaderBoardTable;
    }

    public void updateLeaderBoardTableView(JSONArray users){
        Platform.runLater(()->{
            ObservableList<PlayerRow> usersList = FXCollections.observableArrayList();
            for(Object user : users){
                JSONObject player = (JSONObject) user;
                if(player.get("userName").equals(EntryPoint.getGameClient().getUserName())){
                    continue;
                }
                ImageView image = null;
                if(player.get("status").toString().equalsIgnoreCase("online")){
                    image = Resource.getEmojOn();
                }else{
                    image = Resource.getEmojOff();
                }
                int score = Integer.parseInt(player.get("score").toString());
                usersList.add(new PlayerRow(player.get("userName").toString(), score, image, this.getRank(score)));
            }
            this.leaderBoardTable.setItems(usersList);
        });
    }

    public void updateLoggedInUser(String userName){
        if(this.leaderBoardTable != null){
            Optional<PlayerRow> playerRow = this.leaderBoardTable.getItems().stream().
                    filter(item -> item.getName().equals(userName)).findAny();
            playerRow.get().setPhoto(Resource.getEmojOn());
            this.leaderBoardTable.refresh();
        }
    }

    public void updateLoggedOutUser(String userName){
        if(this.leaderBoardTable != null){
            Optional<PlayerRow> playerRow = this.leaderBoardTable.getItems().stream().
                    filter(item -> item.getName().equals(userName)).findAny();
            playerRow.get().setPhoto(Resource.getEmojOff());
            this.leaderBoardTable.refresh();
        }
    }

    public void updateSignedUpUser(String userName){
        if(this.leaderBoardTable != null){
            this.leaderBoardTable.getItems().add(new PlayerRow(userName, 0, Resource.getEmojOn(), "Novice"));
        }
    }

    public void setBoardButtons(Button[] buttons){
        this.buttons = buttons;
        this.buttons[0].setId("0");
        this.buttons[1].setId("1");
        this.buttons[2].setId("2");
        this.buttons[3].setId("3");
        this.buttons[4].setId("4");
        this.buttons[5].setId("5");
        this.buttons[6].setId("6");
        this.buttons[7].setId("7");
        this.buttons[8].setId("8");
    }

    public void updateBoard(JSONObject replyJson){
        Platform.runLater(()->{
            int index = Integer.parseInt(replyJson.get("index").toString());
            String myTurn = replyJson.get("myTurn").toString();
            if(EntryPoint.getGameClient().getSymbol() == 1){
                if(myTurn.equals("true")){  // So the previous turn was the opponent
                    this.buttons[index].setText("O");
                }else{
                    this.buttons[index].setText("X");
                }
            }else{
                if(myTurn.equals("true")){  // So the previous turn was the opponent
                    this.buttons[index].setText("X");
                }else{
                    this.buttons[index].setText("O");
                }
            }
        });
    }

    public void setSavedGamesTable(TableView<GameRow> savedGamesTable){
        this.savedGamesTable = savedGamesTable;
    }

    public void updateGamesList(JSONArray games){
        Platform.runLater(()->{
            ObservableList<GameRow> gameList = FXCollections.observableArrayList();
            for (Object gameObject : games){
                JSONObject game = (JSONObject) gameObject;
                gameList.add(
                        new GameRow(
                                Integer.parseInt(game.get("id").toString()),
                                game.get("playerOne").toString(),
                                game.get("playerTwo").toString()
                        )
                );
            }
            this.savedGamesTable.setItems(gameList);
        });
    }

    public void showSavedGame(String replayPlayerOneName, String replayPlayerTwoName,
                              JSONObject board, JSONObject state){
        this.replayPlayerOneName = replayPlayerOneName;
        this.replayPlayerTwoName = replayPlayerTwoName;
        this.buttonsMap = new LinkedHashMap<>();
        for (Object keyObj : board.keySet()) {
            String key = (String) keyObj;
            JSONObject coordinate = parseStringToJsonObject(board.get(key).toString());
            for(Object indexObj : coordinate.keySet()){
                String indexStr = indexObj.toString();
                int index = Integer.parseInt(indexStr);
                int symbol = Integer.parseInt(coordinate.get(indexStr).toString());
                String symbolStr = null;
                if(symbol == 1){
                    symbolStr = "X";
                }else{
                    symbolStr = "O";
                }
                this.buttonsMap.put(index, symbolStr);
            }
        }
        GameConfig.setGameMode(0);
        Platform.runLater(()->SwitchSceneTo.showScene(5));
    }

    public String getReplayPlayerOneName(){
        return this.replayPlayerOneName;
    }

    public String getReplayPlayerTwoName(){
        return this.replayPlayerTwoName;
    }

    public LinkedHashMap<Integer, String> getButtonsMap(){
        return this.buttonsMap;
    }

    private String getRank(int score){
        String rank = "";
        if(score < 30){
            rank = "Novice";
        }else if(score < 70){
            rank = "Expert";
        }else{
            rank = "Master";
        }
        return rank;
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
