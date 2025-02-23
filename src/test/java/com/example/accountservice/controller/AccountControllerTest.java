package com.example.accountservice.controller;

import com.example.accountservice.service.enums.BalanceType;
import com.example.accountservice.service.vo.AccountDto;
import com.example.accountservice.service.vo.BalanceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @InjectMocks
    private AccountController accountController; // 컨트롤러에 accountService 주입

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private static final String TEST_ACCOUNT_NUMBER = "ABC12345";

    /**
     * 계좌 생성 테스트
     */
    @Test
    void testCreateAccount() throws Exception {
        mockMvc.perform(post("/api/account/create")
                        .param("accountNumber", TEST_ACCOUNT_NUMBER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(TEST_ACCOUNT_NUMBER));
    }

    /**
     * 잔액 조회 테스트
     */
    @Test
    void testFindAccountBalance() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/account/create")
                        .param("accountNumber", TEST_ACCOUNT_NUMBER))
                .andDo(print()) // 요청 & 응답을 콘솔에 출력
                .andExpect(status().isOk())
                .andReturn(); // 응답 결과 저장
        AccountDto accountDto = objectMapper.readValue(result.getResponse().getContentAsString(), AccountDto.class);

        mockMvc.perform(get("/api/account/{id}/balance", accountDto.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountDto.getId()));
    }

    /**
     * 입금 테스트
     */
    @Test
    void testDeposit() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/account/create")
                        .param("accountNumber", TEST_ACCOUNT_NUMBER))
                .andExpect(status().isOk())
                .andReturn(); // 응답 결과 저장
        AccountDto accountDto = objectMapper.readValue(result.getResponse().getContentAsString(), AccountDto.class);

        BalanceDto request = BalanceDto.from(BalanceType.STANDARD);
        request.deposit(BigDecimal.TEN);

        MvcResult depositResult = mockMvc.perform(post("/api/account/{id}/deposit", accountDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        AccountDto depositAccountDto = objectMapper.readValue(depositResult.getResponse().getContentAsString(), AccountDto.class);

        assertEquals(depositAccountDto.getId(), accountDto.getId());
        assertEquals(depositAccountDto.getBalances().getBalances().get(0).getAmount(), BigDecimal.TEN);
    }

    /**
     * 출금 테스트
     */
    @Test
    void testWithdraw() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/account/create")
                        .param("accountNumber", TEST_ACCOUNT_NUMBER))
                .andExpect(status().isOk())
                .andReturn(); // 응답 결과 저장
        AccountDto accountDto = objectMapper.readValue(result.getResponse().getContentAsString(), AccountDto.class);

        BalanceDto request = BalanceDto.from(BalanceType.STANDARD);
        request.deposit(BigDecimal.TEN);

        mockMvc.perform(post("/api/account/{id}/deposit", accountDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult withdrawResult = mockMvc.perform(post("/api/account/{id}/withdraw", accountDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        AccountDto withdrawAccountDto = objectMapper.readValue(withdrawResult.getResponse().getContentAsString(), AccountDto.class);

        assertEquals(withdrawAccountDto.getId(), accountDto.getId());
        assertEquals(0, withdrawAccountDto.getBalances().getBalances().get(0).getAmount().compareTo(BigDecimal.ZERO));
    }
}
