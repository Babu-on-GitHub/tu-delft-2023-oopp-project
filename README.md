# Talio: the best task management tool ever

This project is an application built in Java using Spring framework and JavaFX.

## Description of project

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
* **Pull request** - You can implement a feature, or fix a bug, or even completely rewrite application, and after that, if you would want us to incorporate your code into our code base, you should open a pull (aka merge) request. We will most likely take a look at it, and, with non-negative probability approve it, and be grateful to you.
* **Bug reports** - It is quite common that you encounter bugs in software products. And, even though our product is one of the greatest programs created ever in the history of humanity, it still might have bugs. Firstly, you will have to check that the bug you supposedly have found is actually a bug, and not a feature. If this is the case, you can open an issue in this repository, and then we will take a look at it, and there is a chance we will spend some time to fix it. However, since we probably will not bother with spending time on this project after it is done, the only option that you have is to fix the issues yourself by following previous option, about pull requests.
* **Feature suggestions** - While we do not intend to continue the development after the moment of repository becoming read only, there is always a chance that we will continue our work. In such cases, it would be quite beneficial to have a list of features proposed by the users of application, since it would allow to greatly increase the user base and approval after incorporation of such features. Moreover, some features may even lead to our application becoming more popular than Trello, our long standing competitior! Therefore, any feature requests are highly valuable to us. You might be able to submit feature request through the repository issues, or you could send it to the email of one of the main developers.
* **Forking** - Since the repository will become read only at some point in the future, there is a chance you just won't be able to do any modifications with the project. Therefore, it makes sense that if you really care about this project, and the opportunity to use adequate applications makes you shiver, you could fork our repository, and then continue the development by yourself. Moreover, since we assume that such situation is significantly unlikely, you could even write to one of the main developers (five people listed on top of the README), and tell us that you are doing that. We will be fascinated, and maybe will buy you a chocolate bar.

## Copyright / License (opt.)

This project is under Apache License. Icons under the `client/src/main/resources/client/icons` are Google Material icons, licensed under Apache License Version 2 too. It is quite important to notice, that the issuers of the icons were not able to license their work correctly, they just included the boilerplate without the copyright information. One more thing at which our project is better than Google.

```
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

   1. Definitions.

      "License" shall mean the terms and conditions for use, reproduction,
      and distribution as defined by Sections 1 through 9 of this document.

      "Licensor" shall mean the copyright owner or entity authorized by
      the copyright owner that is granting the License.

      "Legal Entity" shall mean the union of the acting entity and all
      other entities that control, are controlled by, or are under common
      control with that entity. For the purposes of this definition,
      "control" means (i) the power, direct or indirect, to cause the
      direction or management of such entity, whether by contract or
      otherwise, or (ii) ownership of fifty percent (50%) or more of the
      outstanding shares, or (iii) beneficial ownership of such entity.

      "You" (or "Your") shall mean an individual or Legal Entity
      exercising permissions granted by this License.

      "Source" form shall mean the preferred form for making modifications,
      including but not limited to software source code, documentation
      source, and configuration files.

      "Object" form shall mean any form resulting from mechanical
      transformation or translation of a Source form, including but
      not limited to compiled object code, generated documentation,
      and conversions to other media types.

      "Work" shall mean the work of authorship, whether in Source or
      Object form, made available under the License, as indicated by a
      copyright notice that is included in or attached to the work
      (an example is provided in the Appendix below).

      "Derivative Works" shall mean any work, whether in Source or Object
      form, that is based on (or derived from) the Work and for which the
      editorial revisions, annotations, elaborations, or other modifications
      represent, as a whole, an original work of authorship. For the purposes
      of this License, Derivative Works shall not include works that remain
      separable from, or merely link (or bind by name) to the interfaces of,
      the Work and Derivative Works thereof.

      "Contribution" shall mean any work of authorship, including
      the original version of the Work and any modifications or additions
      to that Work or Derivative Works thereof, that is intentionally
      submitted to Licensor for inclusion in the Work by the copyright owner
      or by an individual or Legal Entity authorized to submit on behalf of
      the copyright owner. For the purposes of this definition, "submitted"
      means any form of electronic, verbal, or written communication sent
      to the Licensor or its representatives, including but not limited to
      communication on electronic mailing lists, source code control systems,
      and issue tracking systems that are managed by, or on behalf of, the
      Licensor for the purpose of discussing and improving the Work, but
      excluding communication that is conspicuously marked or otherwise
      designated in writing by the copyright owner as "Not a Contribution."

      "Contributor" shall mean Licensor and any individual or Legal Entity
      on behalf of whom a Contribution has been received by Licensor and
      subsequently incorporated within the Work.

   2. Grant of Copyright License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      copyright license to reproduce, prepare Derivative Works of,
      publicly display, publicly perform, sublicense, and distribute the
      Work and such Derivative Works in Source or Object form.

   3. Grant of Patent License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      (except as stated in this section) patent license to make, have made,
      use, offer to sell, sell, import, and otherwise transfer the Work,
      where such license applies only to those patent claims licensable
      by such Contributor that are necessarily infringed by their
      Contribution(s) alone or by combination of their Contribution(s)
      with the Work to which such Contribution(s) was submitted. If You
      institute patent litigation against any entity (including a
      cross-claim or counterclaim in a lawsuit) alleging that the Work
      or a Contribution incorporated within the Work constitutes direct
      or contributory patent infringement, then any patent licenses
      granted to You under this License for that Work shall terminate
      as of the date such litigation is filed.

   4. Redistribution. You may reproduce and distribute copies of the
      Work or Derivative Works thereof in any medium, with or without
      modifications, and in Source or Object form, provided that You
      meet the following conditions:

      (a) You must give any other recipients of the Work or
          Derivative Works a copy of this License; and

      (b) You must cause any modified files to carry prominent notices
          stating that You changed the files; and

      (c) You must retain, in the Source form of any Derivative Works
          that You distribute, all copyright, patent, trademark, and
          attribution notices from the Source form of the Work,
          excluding those notices that do not pertain to any part of
          the Derivative Works; and

      (d) If the Work includes a "NOTICE" text file as part of its
          distribution, then any Derivative Works that You distribute must
          include a readable copy of the attribution notices contained
          within such NOTICE file, excluding those notices that do not
          pertain to any part of the Derivative Works, in at least one
          of the following places: within a NOTICE text file distributed
          as part of the Derivative Works; within the Source form or
          documentation, if provided along with the Derivative Works; or,
          within a display generated by the Derivative Works, if and
          wherever such third-party notices normally appear. The contents
          of the NOTICE file are for informational purposes only and
          do not modify the License. You may add Your own attribution
          notices within Derivative Works that You distribute, alongside
          or as an addendum to the NOTICE text from the Work, provided
          that such additional attribution notices cannot be construed
          as modifying the License.

      You may add Your own copyright statement to Your modifications and
      may provide additional or different license terms and conditions
      for use, reproduction, or distribution of Your modifications, or
      for any such Derivative Works as a whole, provided Your use,
      reproduction, and distribution of the Work otherwise complies with
      the conditions stated in this License.

   5. Submission of Contributions. Unless You explicitly state otherwise,
      any Contribution intentionally submitted for inclusion in the Work
      by You to the Licensor shall be under the terms and conditions of
      this License, without any additional terms or conditions.
      Notwithstanding the above, nothing herein shall supersede or modify
      the terms of any separate license agreement you may have executed
      with Licensor regarding such Contributions.

   6. Trademarks. This License does not grant permission to use the trade
      names, trademarks, service marks, or product names of the Licensor,
      except as required for reasonable and customary use in describing the
      origin of the Work and reproducing the content of the NOTICE file.

   7. Disclaimer of Warranty. Unless required by applicable law or
      agreed to in writing, Licensor provides the Work (and each
      Contributor provides its Contributions) on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
      implied, including, without limitation, any warranties or conditions
      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
      PARTICULAR PURPOSE. You are solely responsible for determining the
      appropriateness of using or redistributing the Work and assume any
      risks associated with Your exercise of permissions under this License.

   8. Limitation of Liability. In no event and under no legal theory,
      whether in tort (including negligence), contract, or otherwise,
      unless required by applicable law (such as deliberate and grossly
      negligent acts) or agreed to in writing, shall any Contributor be
      liable to You for damages, including any direct, indirect, special,
      incidental, or consequential damages of any character arising as a
      result of this License or out of the use or inability to use the
      Work (including but not limited to damages for loss of goodwill,
      work stoppage, computer failure or malfunction, or any and all
      other commercial damages or losses), even if such Contributor
      has been advised of the possibility of such damages.

   9. Accepting Warranty or Additional Liability. While redistributing
      the Work or Derivative Works thereof, You may choose to offer,
      and charge a fee for, acceptance of support, warranty, indemnity,
      or other liability obligations and/or rights consistent with this
      License. However, in accepting such obligations, You may act only
      on Your own behalf and on Your sole responsibility, not on behalf
      of any other Contributor, and only if You agree to indemnify,
      defend, and hold each Contributor harmless for any liability
      incurred by, or claims asserted against, such Contributor by reason
      of your accepting any such warranty or additional liability.

   END OF TERMS AND CONDITIONS

   APPENDIX: How to apply the Apache License to your work.

      To apply the Apache License to your work, attach the following
      boilerplate notice, with the fields enclosed by brackets "[]"
      replaced with your own identifying information. (Don't include
      the brackets!)  The text should be enclosed in the appropriate
      comment syntax for the file format. We also recommend that a
      file or class name and description of purpose be included on the
      same "printed page" as the copyright notice for easier
      identification within third-party archives.

   Copyright [2023] [Daniel Popovici, Roman Knyazhitskiy, Halil Betmezoglu, Mate Rodic, Adrian-Robert Buse]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   ```
