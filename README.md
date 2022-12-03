# Personal Expense Management Application

## Purpose:
The purpose of this application is to **track someone's expenses and categorize** those expenses
so that the user can **visualize** how much they are spending on specific
areas. This will help the user budget their income so that they can hit
both their spending and saving goals. The application will come
with a **few default categories** like groceries, housing, school, and restaurants. They
will also be able to add their **own categories** so that they can **customize** the
app to fit their goals/ spending habits. The user will enter their expense 
as well as a short description and **tick a category box** which will add that
expense to that specific category. There will be a **chart** which will show 
what percentage of the user's expenses are in specific categories.
This will allow the user's to more easily **visualize their monthly expenses** and
change their spending habits based on the chart. For example, if the user felt like that
spent more on restaurants than they should have they can adjust that next month.
The user can set goals for specific categories and later adjust them to **find the optimal composition** that
fits their needs/ goals. The app will tell the user when they are **getting near their
goal for a certain category** so that the user knows they have to tune down on that.
It will also provide the functionality of users to **compare monthly or biweekly expense charts**, so they
can compare how they are doing compared to the last time slot. Monthly or biweekly is because 
some people might earn money biweekly and some might monthly.

## Audience:
The target audience for this app is **university students**. This is because they are just
starting to learn about managing expenses, since most people did not have to do so in 
high school. Especially since in university you have many more expense categories compared to 
when you were in high school and that can be very overwhelming and hard to manage for many people.

## Relevance to me:
When I started university I never had any credit cards, or had to manage any money. When I first moved
to UBC I had a really hard time doing this, and I still do. I believe that bank applications don't really provide you
with good tools to manage your expenses, especially if you want to visualize how much
you are spending on specific categories so that you can pin down what you should cut down on. I currently spend hours
on this per month and I believe that if there were an application like this one I could save much more time, while still 
being able to visualize my expenses compared to my goals better. Also, I think that letting the user know when they are 
getting close to their spending goal for that category is really important since many times students end 
up over spending and only realize in a month or longer. So this would definitely help to minimize those occurrences.
There must be a bunch of students in a similar situation, so I think they could all benefit from this.

## User Stories:
- As a user, I want to be able to add expenses to an expense list
- As a user, I want to be able to create an Expense object that has 5 attributes:
  Category, Price, Description, Month, and Day
- As a user, I want to be able to view a graph of the percentages of each category in respect to my total spending
- As a user, I want to be able to view a summary of biweekly and monthly expenses
- As a user, I want to be able to view a list of the current default categories
- As a user, I want to be able to remove an Expense if it was typed incorrectly
- As a user, I want to be able to add Borrow/Lending Objects to an BorrowLend List
- As a user, I want to be able to view my BorrowLend List
- As a user, I want to be able to have the option to save my Personal Expense List when I exit
- As a user, I want to be able to have the option of loading my Personal Expense List from file when I start the App

## Instructions for Grader:
- You can generate the first required event related to adding Xs to a Y by: adding a new Expense through the TextField 
  and the MonthSummaryTable will automatically update. This is because is this is a Personal Expense App and the user  
  should always be able to see summary/ comparison Statistics.
- You can generate the second required event related to adding Xs to a Y by: adding a new Expense through the TextField 
  and the ComparisonGraphs will automatically update. This is because is this is a Personal Expense App and the user 
  should always be able to see summary/ comparison Statistics.
- You can locate my visual component by: the ComparisonGraphs are always on the left of the screen and will 
  automatically update when you add new Expenses.
- You can save the state of my application by: clicking the "Save" button in the bottom right corner.
- You can reload the state of my application by: clicking the "Load" button in the bottom right corner.

## Phase 4: Task 2:
Fri Dec 02 17:03:22 PST 2022
Expense: [dorm, $40.0, hi, 11/23] added to ExpenseTable

Fri Dec 02 17:03:22 PST 2022
SummaryTable percentages updated for month: 11

Fri Dec 02 17:03:22 PST 2022
SummaryTable totals updated for month: 11

Fri Dec 02 17:03:22 PST 2022
Month 11 Graph updated.

Fri Dec 02 17:03:22 PST 2022
Month 10 Graph updated.

Fri Dec 02 17:03:22 PST 2022
Month 9 Graph updated.

Fri Dec 02 17:03:34 PST 2022
Expense: [school, $10.0, hello, 11/30] added to ExpenseTable

Fri Dec 02 17:03:34 PST 2022
SummaryTable percentages updated for month: 11

Fri Dec 02 17:03:34 PST 2022
SummaryTable totals updated for month: 11

Fri Dec 02 17:03:34 PST 2022
Month 11 Graph updated.

Fri Dec 02 17:03:34 PST 2022
Month 10 Graph updated.

Fri Dec 02 17:03:34 PST 2022
Month 9 Graph updated.

## Phase 4: Task 3:
Reflecting back on my UML diagram I believe that the Model package was designed well, using abstraction to minimize code 
repetition between the Expense and BorrowLend classes. However, the GUI design could be improved by reducing Code 
coupling and increasing the Cohesion of the GUI classes. Since they heavily rely on each other changing a piece of code
in one class, usually leads to the other class performing incorrectly. To solve this more GUI classes should be created 
and each one should handle one specific GUI task, instead of multiple classes sharing the work of performing that same 
task. I believe these changes would improve the overall design of the project.
