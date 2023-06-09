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
package client;

import atlantafx.base.theme.PrimerLight;
import client.scenes.MainCtrl;
import client.scenes.MainPageCtrl;
import client.scenes.ServerChoiceCtrl;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;


public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);


    public static void main(String[] args) throws URISyntaxException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        var serverCtrl = FXML.load(ServerChoiceCtrl.class, "client", "scenes", "ServerChoice.fxml");
        var mainPage = FXML.load(MainPageCtrl.class, "client", "scenes", "ReworkedMainPage.fxml");


        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, mainPage, serverCtrl);

        primaryStage.setOnCloseRequest(event -> {
            mainCtrl.shutDown();
        });
    }
}