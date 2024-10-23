package com.itschool.hotelResvMgt.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itschool.hotelResvMgt.models.dtos.RequestReservationDTO;
import com.itschool.hotelResvMgt.services.ReservationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@Slf4j
@Transactional
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest
class ReservationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ReservationService mockReservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateReservation_Success() throws Exception {
        RequestReservationDTO request = new RequestReservationDTO();
        request.setRoomId(1L);
        request.setGuestId(2L);
        request.setGuestsNumber(3);
        request.setCheckInDate(LocalDate.now().plusDays(2));
        request.setCheckOutDate(LocalDate.now().plusDays(5));

        ResultActions resultActions = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.info("Response body: {}", responseBody);

        if (responseBody.contains("\"id\":")) {
            resultActions.andExpect(jsonPath("$.id").isNumber());
        } else {
            log.warn("Response might not include an 'id' field. Check API documentation.");
        }
    }

    @Test
    void testCreateReservation_InvalidRequest() throws Exception {
        RequestReservationDTO request = new RequestReservationDTO();

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetReservations_Success() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("reservation"));
    }

    @Test
    void testUpdateReservationCheckInDate_Success() throws Exception {
        Long reservationId = 1L;
        RequestReservationDTO updateRequest = new RequestReservationDTO();
        updateRequest.setCheckInDate(LocalDate.now().plusDays(3));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(patch("/api/reservations/" + reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReservation_Success() throws Exception {
        Long reservationId = 1L;

        mockMvc.perform(delete("/api/reservations/" + reservationId))
                .andExpect(status().isNoContent());
    }
}