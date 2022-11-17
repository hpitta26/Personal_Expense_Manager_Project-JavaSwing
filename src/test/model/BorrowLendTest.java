package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BorrowLendTest {
    private BorrowLend testBorrowLend1;
    private BorrowLend testBorrowLend2;
    private BorrowLend testBorrowLend3;
    private BorrowLend testBorrowLend4;
    private BorrowLend testBorrowLend5;
    private BorrowLend testBLWrong;


    @BeforeEach
    void runBefore() {
        testBorrowLend1 = new BorrowLend("Henry", 90, "hi", 9, 14, true);
        testBorrowLend2 = new BorrowLend("Kate", 90, "hi", 10, 2, false);
        testBorrowLend3 = new BorrowLend("Sam", 90.0, "hi", 10, 8, true);
        testBorrowLend4 = new BorrowLend("John", 90, "hi", 10, 17, true);
        testBorrowLend5 = new BorrowLend("Bob", 90, "hi", 11, 6, true);
        testBLWrong = new BorrowLend("Jack", -5, "hi", 13, -5, true);
    }

    @Test
    void testConstructor() {
        assertEquals("Henry", testBorrowLend1.getName());
        assertEquals(90.0, testBorrowLend1.getPrice());
        assertEquals("hi", testBorrowLend1.getDescription());
        assertEquals(9, testBorrowLend1.getMonth());
        assertEquals(14, testBorrowLend1.getDay());
        assertEquals(true, testBorrowLend1.getBorrowLend());
    }

    @Test
    void testConstructorInvalidInput() {
        assertEquals(0, testBLWrong.getPrice());
        assertEquals(1, testBLWrong.getMonth());
        assertEquals(1, testBLWrong.getDay());
    }

    @Test
    void testToString() {
    String toString1 = "Borrowed $90.0 from Henry on 9/14";
    String toString2 = "Lent $90.0 to Kate on 10/2";
        assertEquals(toString1, testBorrowLend1.toString());
        assertEquals(toString2, testBorrowLend2.toString());
    }

    @Test
    void testBorrowLendCompareTo() {
        assertEquals(1, testBorrowLend2.compareTo(testBorrowLend1));
        BorrowLend testBorrowLend6 = new BorrowLend("Nancy", 90.0,
                "blanket", 10,2, true);
        assertEquals(0, testBorrowLend2.compareTo(testBorrowLend6));
        assertEquals(-1, testBorrowLend2.compareTo(testBorrowLend5));
        assertEquals(1, testBorrowLend3.compareTo(testBorrowLend2));
        assertEquals(-1, testBorrowLend2.compareTo(testBorrowLend3));
    }

    @Test
    void testGettersAndSetters() {
        testBorrowLend1.setName("Bob");
        testBorrowLend1.setBorrowLend(false);
        assertEquals("Bob", testBorrowLend1.getName());
        assertFalse(testBorrowLend1.getBorrowLend());
    }

    @Test
    void testToJson() {
        BorrowLend b = new BorrowLend("Henry", 90, "hi", 9, 14, true);

        JSONObject json1 = b.toJson();
        assertEquals("Henry", json1.getString("Name"));
        assertEquals(90, json1.getDouble("Amount"));
        assertEquals("hi", json1.getString("Description"));
        assertEquals(9, json1.getInt("Month"));
        assertEquals(14, json1.getInt("Day"));
        assertEquals(true, json1.getBoolean("BorrowLend"));
    }

}