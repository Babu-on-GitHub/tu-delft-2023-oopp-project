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

    public void initialize(Stage primaryStage, Pair<MainPageCtrl, Parent> mainPage) {
        this.primaryStage = primaryStage;
        this.mainPageCtrl = mainPage.getKey();
        this.mainPage = new Scene(mainPage.getValue());

        showMainPage();
        primaryStage.show();
    }

    public void showMainPage() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(mainPage);
        mainPageCtrl.refresh();
    }
}