/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private MainPageCtrl mainPageCtrl;
    private Scene mainPage;

    private ServerChoiceCtrl serverChoiceCtrl;
    private Scene serverChoice;

    public void initialize(Stage primaryStage, Pair<MainPageCtrl, Parent> mainPage,
                           Pair<ServerChoiceCtrl,Parent> serverChoice) {
        this.primaryStage = primaryStage;
        this.mainPageCtrl = mainPage.getKey();
        this.mainPage = new Scene(mainPage.getValue());
        this.serverChoiceCtrl = serverChoice.getKey();
        this.serverChoice = new Scene(serverChoice.getValue());

        showServerChoice();
        primaryStage.show();
    }

    public Scene getMainPage() {
        return mainPage;
    }

    public void showMainPage() {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(mainPage);
        mainPageCtrl.setAdmin(false);
        mainPageCtrl.initializeServerStuff();
        mainPageCtrl.refresh();
    }

    public void showAdminMainPage() {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(mainPage);
        mainPageCtrl.setAdmin(true);
        mainPageCtrl.refresh();
    }

    public void showServerChoice() {
        primaryStage.setTitle("Server choice");
        primaryStage.setScene(serverChoice);
        serverChoiceCtrl.resetFieldsStyle();
    }
}