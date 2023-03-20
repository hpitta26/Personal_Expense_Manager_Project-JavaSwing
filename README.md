# Personal Expense Management Desktop App

## Current Project



## Features:
- Used JSwing for GUI
- Used JSON for serialization
- Used JUnit for testing
- JFreeChart for graph visualization

## Current Implementations:
- Console based App: PersonalExpenseApp
- GUI without month implementation: PersonalExpenseAppGUI (project presented at the end of the semester)
- GUI with month implementation: MainJFrameGUI

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
There must be a bunch of students in similar situations, so I think they could all benefit from this.

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
