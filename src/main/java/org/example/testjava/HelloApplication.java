package org.example.testjava;

/* This program is a rock paper scissors game with a reset counter, database to store number of wins and losses, and GUI.
 */


// import statements for GUI, we'll set the scene, positioning, and layout for the GUI itself and its other elements.
import javafx.application.Application;
import javafx.geometry.Pos; // defines alignment of
import javafx.scene.Scene; // import to represent visual content of window
import javafx.scene.control.Button; // Ui element for displaying text and triggering actions
import javafx.scene.control.Label;
import javafx.scene.layout.VBox; //layout manager that arranges UI components vertically
import javafx.stage.Stage; // represents main window of the JavaFx Application

import java.sql.*; // import statement used for database operation

/*
declares public class 'HelloApplication' used to run javafx program. Overrides start of main method to determine app behavior
 */
public class HelloApplication extends Application { //extends to Application, inheriting the properties from the Application class (start, primary stage)

    private Label resultLabel; //Displays the results of the round
    private Label scoreLabel; //Displays the current score
    private Connection connection;  //represents connection to database


    /*
    main method and entry point of the program
     */
    public static void main(String[] args) {
        launch(args);
    }
    /*
    main method used to represent window of main application
     */
    @Override
    public void start(Stage primaryStage) {
        setupDatabase(); //connects to database to ensure the table exist
        setupUI(primaryStage); //Setup method called with param primary stage
    }

    /*
    private method to establish connection of database, if table does not exist, we'll just create our table
     */
    private void setupDatabase() {

        //methods below provide  connection to database and creation of table
        connectToDatabase();
        createTableIfNotExists();
    }

    /*
    This method defines and initializes all UI components, it organizes these components and displays them
    in a new window
     */
    private void setupUI(Stage stage) {
        stage.setTitle("Rock-Paper-Scissors Game"); // title of game

        Label titleLabel = createTitleLabel(); //
        // creation of buttons for rock paper scissors
        Button rockButton = createGameButton("Rock");
        Button paperButton = createGameButton("Paper");
        Button scissorsButton = createGameButton("Scissors");
        Button resetButton = createResetButton();

        // stage packaged used throughout program to represent our windows throughout the program

        resultLabel = createResultLabel();
        scoreLabel = createScoreLabel();

      // vertical box layout organized for buttons and UI components
        VBox layout = new VBox(15, titleLabel, rockButton, paperButton, scissorsButton, resetButton, resultLabel, scoreLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px; -fx-font-family: Arial;");

        Scene scene = new Scene(layout, 400, 400); // represents the actual size of our window for the program
        stage.setScene(scene);
        stage.show();
    }


    /*
    Here, well create our methods necessary for the UI, as well as ensuring we have our database connection
     */

    /*
    Private method with Label object is used to create our liable of the scene we've created in our GUI
     */
    private Label createTitleLabel() {
        Label titleLabel = new Label("Rock-Paper-Scissors");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); // here we'll just set the font size
        return titleLabel; // since this is not void, we'll return our titlelabel
    }

    /*
    Much like the method above, we'll create another label object in this method for UI Component
     */
    private Label createResultLabel() {
        Label label = new Label("Make your move!");
        label.setStyle("-fx-font-size: 16px;");
        return label; // again, we'll return the labe;
    }


    /*
    Same method creation applies to our createScoreLabel, we'll our new label object to create our style
     */
    private Label createScoreLabel() {
        Label label = new Label(getScoreText());
        label.setStyle("-fx-font-size: 14px;");
        return label;
    }

    /*
    These two methods we'll be used to configure our buttons properly. Both methods we'll attach our event handlers
    to each button, and will return the button itself
     */

    private Button createGameButton(String choice) {
        Button button = new Button(choice);
        button.setOnAction(e -> playGame(choice));
        // in this line, we'll set our event handler to respond to player choice
        return button;
    }

    private Button createResetButton() {
        Button resetButton = new Button("Reset Scores");
        resetButton.setOnAction(e -> resetScores());
        return resetButton;
    }

    /*
    connectToDatabase method is responsible for establishing
    a connection between java application and SQLite database named RockPaperScissors.db.
     */
    private void connectToDatabase() {
        // try and catch block to handle any errors when establishing our connection
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:RockPaperScissors.db");
            // we'll use our connection field and the driver manager package to accept a url param of just the name of our database
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1); // Terminates program with error code of 1 to display there is an error to the program
        }
    }

    /*
    createTable method is responsible for ensuring that the database table(results) exists. If the table does not exist
    we'll create a table
     */
    private void createTableIfNotExists() {
        // try abd catch statement
        try (Statement statement = connection.createStatement()) {
            // here, we'll create a new statement object called statement to execute SQL queries
            // our connection is an interface in the java sql. package
            String sql = """
                    CREATE TABLE IF NOT EXISTS results (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        wins INTEGER DEFAULT 0,
                        losses INTEGER DEFAULT 0,
                        ties INTEGER DEFAULT 0
                    );
                    """;
            statement.execute(sql);

            // if our table for the game is empty, we'll initialize row with data to increment off of
            if (isTableEmpty(statement)) {
                initializeScoreRow(statement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    This method will check if our table in the database is empty.
     */
    private boolean isTableEmpty(Statement statement) throws SQLException {
        // Here, ResultSet is a part of the class for database connectivity, here rs is an object
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM results;"); // execute query is part of statement instance
        // sql command used to count total number of rows in the table.
        // Here, we'll use our ResultSet object to run an instance of our statement to execute this SQL query

        return rs.next() && rs.getInt(1) == 0;
        // return statement is used to verify if table is empty.
        //Here, we'll ensure if the query returned at least one row

        /*
        RS.next and .getInt method is uss
         */
    }

    /*
    Method to initialize ourScoreRow
     */
    private void initializeScoreRow(Statement statement) throws SQLException {
        statement.execute("INSERT INTO results (wins, losses, ties) VALUES (0, 0, 0);");
    }

    private String getScoreText() {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM results LIMIT 1;");
            if (rs.next()) {
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                int ties = rs.getInt("ties");
                return String.format("Wins: %d | Losses: %d | Ties: %d", wins, losses, ties);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Wins: 0 | Losses: 0 | Ties: 0"; // if any exception occurs, we'll return a score string
    }

    /*
    Method will update our cores based on the result of the outcome between the user and computer
     */
    private void updateScores(String result) {
        String column = switch (result) {
            /*
            Here, we'll use a switch expression which column (wins, losses, or ties) to
            update based on the result: "win": Updates the wins column."lose": Updates the losses column.
            "tie": Updates the ties column. Lambda expression used for different case results
             */
            case "win" -> "wins";
            case "lose" -> "losses";
            case "tie" -> "ties";
            default -> throw new IllegalArgumentException("Invalid result: " + result);
        };

        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE results SET " + column + " = " + column + " + 1 WHERE id = 1;")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scoreLabel.setText(getScoreText());
    }

    /*
    method to reset scores to zero. Try and catch block again to update results if user chooses to reset score.
     */
    private void resetScores() {
        try (Statement statement = connection.createStatement()) {

            /*with our statement object establishing connection to the database, we'll run our SQL command
            execute to update our database through our reset scores method.
             */
            statement.execute("UPDATE results SET wins = 0, losses = 0, ties = 0 WHERE id = 1;");
        } catch (SQLException e) {
            e.printStackTrace(); // in case of error. We'll print this log statement to ensure where the error came from
        }
        scoreLabel.setText(getScoreText());
        resultLabel.setText("Scores reset!");
        // result label will output socres reset!
    }

    /*playGame method handles logic for playing a round of Rock-Paper-Scissors between the user
    and the Computer. Since it is private and void, we'll only be able to access this within the same class
    and we are not returning any values. Since we are calculating the result and updating the UI. Our param is
    whatever the user chooses.
     */
    private void playGame(String playerChoice) {
        String[] options = {"Rock", "Paper", "Scissors"};
        //String array named options to display result rock, paper, scissors. Which is meant to hold the options
        String computerChoice = options[(int) (Math.random() * 3)];
      /*
      for computer choice, we'll use math,.random to generate a random number as far as options for rock paper scissors
      we'll be selecting a random choice from the options array based on the generated index
       */
        String result;
        // if player choice is equal to what the computer chooses. Our result variable will equal a tie
        if (playerChoice.equals(computerChoice)) {
            result = "tie";
            resultLabel.setText(String.format("You chose %s. Computer chose %s.\nIt's a tie!", playerChoice, computerChoice));

            //if not, we'll either display 'You win!' or 'You Lose!'
        } else if ((playerChoice.equals("Rock") && computerChoice.equals("Scissors")) ||
                (playerChoice.equals("Paper") && computerChoice.equals("Rock")) ||
                (playerChoice.equals("Scissors") && computerChoice.equals("Paper"))) {
            result = "win";
            resultLabel.setText(String.format("You chose %s. Computer chose %s.\nYou win!", playerChoice, computerChoice));
        } else {
            result = "lose";
            resultLabel.setText(String.format("You chose %s. Computer chose %s.\nYou lose!", playerChoice, computerChoice));
        }

        updateScores(result);
    }

    @Override
    /*
    Stop method to close database when app is close
     */
    public void stop() {
        //try and catch block to ensure connections is closed
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}