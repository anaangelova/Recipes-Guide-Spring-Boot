# Recipes Guide: Taste The Joy
<br>
This is a <b><i>Spring Boot</i></b> Web Application with the main purpose of searching for recipes, publishing recipes and the proccess of approving recipes. <br>

There are 2 types of users - regular users and an admin user. Regular users can easily register themselves using the registration form or via single sign-on with their Google or Facebook account (this was implemented using <i><b>Spring Security and OAuth2</b></i>). <br><br>
All authenticated users can add, delete, edit their recipes, save other user's recipes, as well as search for different recipes by different criteria (the communication with the underlying database - <b><i>PostgreSQL</i></b> was done by implementing <b><i>Spring Data JPA Repositories</i></b>). <br><br>
All users (authenticated and unauthenticated) can subscribe with their email to the website's newsletter and as a result they get an email message for successful registration (this was implemented using <b><i>Spring Mail and JavaMailSender</i></b>) <br><br>
The admin user is responsible for approving or dismissing the pending recipes. In other words, when a user adds new recipe they submit the recipe and wait for approval. While the recipe is pending, it is not shown anywhere except in the user's list of recipes. When the admin approves a recipe, that recipe becomes 'public' and is shown on home page and in all appropriate categories. If the admin dismisses a recipe, the recipe's author can make some changes and submit the recipe again for review. <br><br>
<b><i>Thymeleaf</i></b> was used as a template engine and the technologies used for the front-end development are <b><i>HTML, CSS, Bootstrap and Javascript</i></b>.
