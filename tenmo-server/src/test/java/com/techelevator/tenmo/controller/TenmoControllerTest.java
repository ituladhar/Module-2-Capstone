package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TenmoControllerTest {


    @Autowired
    TenmoController controller;

    @Autowired
    ObjectMapper mapper;

    MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        System.out.println("setup()...");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }




    @Test
    public void getAccountBalance() {
    }

    @Test
    public void getAccountByUserId() {
    }

    @Test
    public void getUserIdByAccountId() {
    }

    @Test
    public void getAllUsers() {
    }

    @Test
    public void listTransfers() {

        @MockBean
        TenmoController tenmoController;
        Transfer(long transferId, long accountFrom, long accountTo, BigDecimal amount, String transferTypeDesc, String transferStatusDesc)

        // Fake data test
        Transfer t1 = new Transfer ( 5,1045,1046,200.00,"send","approve");
        Transfer t2 = new Transfer ( 7,1046,1049,208.00,"send","approve");

        // Retrieve the list of fake data test
        List<Transfer> testList = List.of(t1,t2);



        when(tenmoController.findAll()).thenReturn(testList);

        this.mockMvc.perform(get("/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-list"))
                .andExpect(model().attribute("students", studentList))
                .andExpect(model().attribute("students", Matchers.hasSize(3)))
                .andDo(print());


    }

    @Test
    public void listPendingTransfers() {
    }

    @Test
    public void transferDetails() {
    }

    @Test
    public void startTransfer() {
    }

    @Test
    public void requestTransfer() {
    }

    @Test
    public void acceptTransfer() {
    }

    @Test
    public void rejectTransfer() {
    }

    @Test
    public void username() {
    }

    @Test
    public void whoAmI() {
    }



    private String toJson(Transfer transfer) throws JsonProcessingException {
        return mapper.writeValueAsString(transfer);
    }
}