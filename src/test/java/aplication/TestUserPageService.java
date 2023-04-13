package aplication;

import aplication.model.classes.Operation;
import aplication.service.UserPageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestUserPageService {

    private final UserPageService userPageService;

    @Autowired
    public TestUserPageService(UserPageService userPageService) {
        this.userPageService = userPageService;
    }

    @Test
    public void testGetChartData() {
        Set<Operation> testSet = new HashSet<Operation>();
        Calendar creationDate = new GregorianCalendar();
        List<Object> resultList1 = new ArrayList<>();
        resultList1.add(List.of("Category1", 400D));
        List<Object> resultList2 = new ArrayList<>();
        resultList2.add(List.of("Category3", 70D));
        Operation operation1 = new Operation(null, creationDate, "Test Operation1", "Category1", "Description", 100);
        Operation operation2 = new Operation(null, creationDate, "Test Operation2", "Category2", "Description", -200);
        Operation operation3 = new Operation(null, creationDate, "Test Operation3", "Category1", "Description", 300);
        Operation operation4 = new Operation(null, creationDate, "Test Operation4", "Category3", "Description", 70);
        Operation operation5 = new Operation(null, creationDate, "Test Operation5", "Category2", "Description", -40);
        testSet.add(operation1);
        testSet.add(operation2);
        testSet.add(operation3);
        testSet.add(operation4);
        testSet.add(operation5);

        List<List<Object>> listOfLists = userPageService.getChartData(testSet, "Income");

        if(listOfLists.get(0) == resultList1.get(0)) {
            assertEquals(resultList1.get(0), listOfLists.get(0));
            assertEquals(resultList2.get(0), listOfLists.get(1));
        }
        else {
            assertEquals(resultList1.get(0), listOfLists.get(1));
            assertEquals(resultList2.get(0), listOfLists.get(0));
        }


    }
}
