package com.example.demo;

import com.example.demo.common.api.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class OrderApiTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;



    @Test
    void createOrder_shouldBeIdempotent_whenSameRequestIdAndSameBody() throws Exception {
        String body = """
                {"productId": 12, "count": 1}
                """;

        String r1 = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-Id", "idem-1001")
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String r2 = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-Id", "idem-1001")
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode j1 = objectMapper.readTree(r1);
        JsonNode j2 = objectMapper.readTree(r2);

        assertThat(j1.get("code").asInt()).isEqualTo(0);
        assertThat(j2.get("code").asInt()).isEqualTo(0);
        assertThat(j1.get("data").asLong()).isEqualTo(j2.get("data").asLong());
    }

    @Test
    void createOrder_shouldFail_whenStockNotEnough() throws Exception {
        String body = """
                {"productId": 12, "count": 999999}
                """;

        String resp = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-Id", "stock-1001")
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode j = objectMapper.readTree(resp);
        assertThat(j.get("code").asInt()).isEqualTo(ErrorCode.STOCK_NOT_ENOUGH.getCode());
    }

    @Test
    void cancel_shouldBeIdempotent_whenCancelTwice() throws Exception {
        String createBody = """
                {"productId": 12, "count": 1}
                """;

        String createResp = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Request-Id", "cancel-1001")
                        .content(createBody))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        long orderId = objectMapper.readTree(createResp).get("data").asLong();

        String c1 = mockMvc.perform(post("/api/order/" + orderId + "/cancel"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String c2 = mockMvc.perform(post("/api/order/" + orderId + "/cancel"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(objectMapper.readTree(c1).get("code").asInt()).isEqualTo(0);
        assertThat(objectMapper.readTree(c2).get("code").asInt()).isEqualTo(0);
    }
}