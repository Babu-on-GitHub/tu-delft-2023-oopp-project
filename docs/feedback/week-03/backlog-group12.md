### Feedback on Backlog draft for Group 12

### Submission

*Mark: Pass*
The submission consists of a single PDF file, uploaded in the correct directory and with the correct name.

### Backlog Structure

*Mark: Pass.*
The document has the correct structure (list of stakeholders, terminology, list of user stories). All of these are covered with a reasonable amount of details.

### Epics

*Mark: Insufficient.*
While user stories are sorted by priority using the MoSCoW method (following the format from last year's backlog), this assignment required the use of epics, as described in the Requirements Engineering lecture and the [assignment description](https://se.ewi.tudelft.nl/oopp/assignments/backlog/#epics).
For the final version of the Backlog, please make sure that the following points are met:
- The first epic is the Minimal App (minimal viable product);
- The subsequent epics correspond to features;
- Every epic has a clarifying description;
- The epics form a complete and prioritised representation of all of the features for this project.

Additionally (and optionally), consider using mock-ups for epics. These will aid both the reader of the backlog in better understanding the descriptions of the epics, and you in ensuring that there are no overlaps or gaps.


### User Stories

*Mark: Very Good*

All of the user stories follow the correct **format** (As a ... I want to ... so I can ...). Additionally, they are **focused on the user perspective**, don't contain contradicting statements and each describe one particular workflow with no overlaps. Great job on perfectly meeting these criteria!

Please make sure not to add functionalities which are not required in the project description. I noticed that you included details such as storing user information and "separation between the boards that I am an owner of and boards that I was invited to". It is your responsibility check what is required of you to implement. Features that were not described during the lectures will not be graded, and you will only lose time implementing them.

Also, the backlog contains a number of non-functional requirements ("*run a server-side application using SpringBoot and JavaFX*", "*have the application load in under 1 second so it feels more reliable and professional.*"). Check the Requirement Engineering slides to see why non-functional requirements are not needed for User Stories.

Following is a list of ambiguities I identified in the user stories:
- "*create new cards characterised by a descriptive title*" - where are new cards displayed after being created? Is there an implicit section or do they appear in the last active list?
- "*remove cards and lists*" - what happens when a list is deleted? Do all of its cards disappear as well or are they moved to another list?
- "*be able to distinguish between different cards on the board, so that it is possible to navigate among them.*" - Distinguish what? Navigate how? Does this simply mean that cards have outlines, or is there another meaning? 
- "*manage and delete any boards*" - What does *manage* mean in this context?
- "*be able to use standard keyboard shortcuts*" - What shortcuts can be used and what are their effects?

Besides the above-mentioned ambiguities, the user stories are a good representation of the required interactions and workflows. Keep in mind that in order to ensure the **completeness** of the user stories, they must fully cover the epics, their descriptions and, if included, the mockups. 

### Acceptance Criteria
*Mark: Excellent*

- *Conciseness:* The size of most user stories is small and their effect is clear, so their acceptance criteria are implicit.
- *Clarity:* User stories/acceptance criteria are formulated in a measurable/observable way that allows to decide when a story is done/finished.
