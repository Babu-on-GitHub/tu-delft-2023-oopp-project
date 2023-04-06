# Starting template

This README will need to contain a description of your project, how to run it, how to set up the development environment, and who worked on it.
This information can be added throughout the course, except for the names of the group members.
Add your own name (do not add the names for others!) to the section below.

## Description of project
This project is an application built in Java using Spring framework and JavaFX.

The purpose of the application is to be used as a task management tool. It can be used from the perspective of users - people that can start the client and connect to a server or an admin - a person that maintains the server and represents the contact for users, if there are
issues with the application. 
Each user starts its client application on their own computer. After the application is started, user defines IP of the server they want to connect to. Admins log in through the same screen, by clicking the "log in as an admin button" and entering the correct password.
If IP is correct, user will connect to the server and will be able to use the application.

Main page of the application shows an overview of all the boards user is a member of, buttons with additional features as well as the contents of the board user is currently looking at.
Each board can have a number of lists and every list can contain cards. Card is a representation of a task. Main component of a card is its title.
Apart from title, cards can have descriptions, subtasks and tags.
Subtasks can be completed, deleted and changed and each tag has its color and title. Tags can be assigned to any card of the board they exist on.

User can add, remove or rename lists and cards as well as all the components of cards. Individual cards can be moved inside the list as well as in between all the lists on the board.
From the main page user can log out of the server he is and go back to server login page.
User is able to create new boards or join them by ID. ID of the board is shown upon pressing share button, which gives option to copy the ID. Other users can join that board by using that ID.
When the board has more than one member, data of the board is synced between them in real time using websockets.
Aspects of the app such as color scheme of the boards, colors of tags and color preset of tasks can be customized through either customization menu of the app or detailed card view.
App supports standard keyboard shortcuts of which a list can be seen when pressing ? anywhere.

When client closes his application, the data he modified will be saved on the server, so when he logs back in he will be able to access all boards from before as well as content that was on them.

## Group members

| Profile Picture                                                                                         | Name               | Email                             |
|---------------------------------------------------------------------------------------------------------|--------------------|-----------------------------------|
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/5842/avatar.png?width=50" width = "50" height = "50"/>                | Daniel Popovici    | d.i.popovici-1@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Halil Betmezoglu   | h.betmezoglu-1@student.tudelft.nl |
| ![](misc/rknyazhitskiy-icon.png)                                                                        | Roman Knyazhitskiy | r.knyazhitskiy@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/1d11e62d8b683a281515ec43e84b4e8e?s=800&d=identicon&size=50)      | Adrian-Robert Buse | A.R.Buse@student.tudelft.nl       |
| ![](https://secure.gravatar.com/avatar/e6c58a4957d1070a0048ef2640ab522b?s=800&d=identicon&size=50)      | Mate Rodić         | M.Rodic@student.tudelft.nl        |

## How to run it

The project is built using quite a modern set of technologies: Java 19, Gradle 7.6.1 and JavaFX 19. You don't have to worry about the Gradle version, since it will be installed automatically by the included wrapper `gradlew`. However, you will have to install both JavaFX and, at the very least JRE, to run the application. Moreover, there is also a possibility to run the project using Java 17 LTS, which will be supported for many years to come. 

JavaFX is generally hard to install: you will have to follow the tutorial from the [official website](https://openjfx.io/openjfx-docs//index.html#install-javafx) to do that.

After all is done, you can run the project easily!
```
git clone ....
cd oopp-team-12
```
And after that you could start a client application, via 
```
./gradlew run
```
However, client application is quite useless without server: indeed, the only thing it can do is shyly say that there are no servers to which it can connect.. One can resolve such an uncomfortable situations by running the server application in parallel (e.g. in other terminal window):
```
./gradlew bootRun
```

## How to contribute to it

*If you want to contribute to the application - don't.*

But if you are sure in your decision, you could simply install JDK instead of JRE in the previous step, and after this you could run it as an professional developer. There will be a lot of logs, which might help you during development. 

There are few ways you can contribute to the project:
* *Pull request* - You can implement a feature, or fix a bug, or even completely rewrite application, and after that, if you would want us to incorporate your code into our code base, you should open a pull (aka merge) request. We will most likely take a look at it, and, with non-negative probability approve it, and be grateful to you.
* *Make issues* - It is quite common that you encounter bugs in software products. And, even though our product is one of the greatest programs created ever in the history of humanity, it still might have bugs. Firstly, you will have to check that the bug you supposedly have found is actually a bug, and not a feature. If this is the case, you can open an issue in this repository, and then we will take a look at it, and there is a chance we will spend some time to fix it. However, since we probably will not bother with spending time on this project after it is done, the only option that you have is to fix the issues yourself by following previous option, about pull requests.
* *Forking* - Since the repository will become read only at some point in the future, there is a chance you just won't be able to do any modifications with the project. Therefore, it makes sense that if you really care about this project, and the opportunity to use adequate applications makes you shiver, you could fork our repository, and then continue the development by yourself. Moreover, since we assume that such situation is significantly unlikely, you could even write to one of the main developers (five people listed on top of the README), and tell us that you are doing that. We will be fascinated, and maybe will buy you a chocolate bar.

## Copyright / License (opt.)
