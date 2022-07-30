package com.cydeo.steps;

import com.cydeo.pages.DashBoardPage;
import com.cydeo.pages.LoginPage;
import com.cydeo.utility.BrowserUtil;
import com.cydeo.utility.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class DashboardStepDefs {
    String actualUserNumbers;
    String actualBookNumbers;
    String actualBorrowedBookNumbers;
    LoginPage loginPage = new LoginPage();
    DashBoardPage dashBoardPage = new DashBoardPage();


    @Given("the user logged in as {string}")
    public void the_user_logged_in_as(String user) {
        loginPage.login(user);
        BrowserUtil.waitFor(4);
    }

    @When("user gets all information from modules")
    public void user_gets_all_information_from_modules() {

        actualUserNumbers = dashBoardPage.usersNumber.getText();
        System.out.println("actualUserNumbers = " + actualUserNumbers);
        actualBookNumbers = dashBoardPage.booksNumber.getText();
        System.out.println("actualBookNumbers = " + actualBookNumbers);
        actualBorrowedBookNumbers = dashBoardPage.borrowedBooksNumber.getText();
        System.out.println("actualBorrowedBookNumbers = " + actualBorrowedBookNumbers);

    }

    @Then("the informations should be same with database")
    public void the_informations_should_be_same_with_database() {


        //1.Make a connection
        // DB_Util.createConnection();
        // since we have Before After with custom hooks we don't need this step anymore

        //USERS

        //2.Run Query
        DB_Util.runQuery("select count(*) from users");

        //3.Store data
        String expectedUsers = DB_Util.getFirstRowFirstColumn();

        //4.Make an assertion
        Assert.assertEquals(expectedUsers, actualUserNumbers);


        //BOOKS


        //2.Run Query
        DB_Util.runQuery("select count(*) from books");
        //3.Store data
        String expectedBooks = DB_Util.getCellValue(1, 1);
        //4.Make an assertion
        Assert.assertEquals(expectedBooks, actualBookNumbers);

        //BORROWED BOOKS

        //2.Run Query
        DB_Util.runQuery("select count(*) from book_borrow where is_returned=0");
        //3.Store data
        String expectedBookBorrowedNumber = DB_Util.getFirstRowFirstColumn();
        //4.Make an assertion
        Assert.assertEquals(expectedBookBorrowedNumber, actualBorrowedBookNumbers);

        //5.Close the conn
        //DB_Util.destroy();
        // since we have Before After with custom hooks we don't need this step anymore

    }
}
