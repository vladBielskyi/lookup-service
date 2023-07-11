package com.lookup.service.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListUtilTest {

    @Test
    public void createSubList_shouldCreateSubListsOfSizeTwo() {
        List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6);
        int subListSize = 2;

        List<List<Integer>> result = ListUtil.createSubList(inputList, subListSize);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(Arrays.asList(1, 2), result.get(0));
        Assertions.assertEquals(Arrays.asList(3, 4), result.get(1));
        Assertions.assertEquals(Arrays.asList(5, 6), result.get(2));
    }

    @Test
    public void createSubList_shouldCreateEmptyListWhenInputListIsEmpty() {
        List<Integer> inputList = Arrays.asList();
        int subListSize = 2;

        List<List<Integer>> result = ListUtil.createSubList(inputList, subListSize);

        Assertions.assertTrue(result.isEmpty());
    }
}
