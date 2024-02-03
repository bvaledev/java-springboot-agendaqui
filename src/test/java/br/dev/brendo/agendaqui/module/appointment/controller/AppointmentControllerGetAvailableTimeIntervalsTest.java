package br.dev.brendo.agendaqui.module.appointment.controller;

import br.dev.brendo.agendaqui.module.appointment.dto.AvailabilityOutputDTO;
import br.dev.brendo.agendaqui.module.appointment.entity.AppointmentEntity;
import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.entity.TimeIntervalEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import br.dev.brendo.agendaqui.module.specialization.repository.TimeIntervalRepository;
import br.dev.brendo.agendaqui.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AppointmentControllerGetAvailableTimeIntervalsTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private TimeIntervalRepository timeIntervalRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private MockMvc mvc;
    private SpecializationEntity specialization;
    private PatientEntity patient;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        this.specialization = SpecializationEntity.builder().name("Java").slug("java").build();
        this.specialization = this.specializationRepository.saveAndFlush(specialization);

        var intervals = TimeIntervalEntity.builder().weekDay(DayOfWeek.TUESDAY.getValue()).timeStartInMinutes(60*8).timeEndInMinutes(60*18).enabled(true).specialization(specialization).build();
        intervals = this.timeIntervalRepository.saveAndFlush(intervals);

        this.specialization.setTimeIntervals(List.of(intervals));

        this.patient = PatientEntity.builder()
                .name("test")
                .cpf("02898287245")
                .email("test@test.com")
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
        this.patient = patientRepository.saveAndFlush(this.patient);
    }

    @After
    public void tearDown(){
        this.appointmentRepository.deleteAll();
        this.patientRepository.deleteAll();
        this.timeIntervalRepository.deleteAll();
        this.specializationRepository.deleteAll();
        this.specializationRepository.deleteAll();
    }

    @Test
    public void should_return_all_available_times() throws Exception {
        var today = LocalDateTime.now().withHour(15).withSecond(0);
        var nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        var queryParam = "?specializationSlug=" + this.specialization.getSlug();
        queryParam += "&dateTime=" + nextTuesday.format(formatter);
        var url = "/appointments/available-time-intervals" + queryParam;

        var requestBuilder = MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON);

        var response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var expectedJson = TestUtils.objectToJson(
                AvailabilityOutputDTO.builder()
                        .availableTimes(List.of(8, 9, 10, 11, 12, 13, 14, 15, 16, 17))
                        .possibleTimes(List.of(8, 9, 10, 11, 12, 13, 14, 15, 16, 17))
                        .build());

        assertEquals(expectedJson, response.getResponse().getContentAsString());
    }

    @Test
    public void should_return_available_times_without_scheduled_time() throws Exception {
        int timeScheduled = 15;
        var today = LocalDateTime.now().withHour(timeScheduled).withSecond(0);
        var nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        var appointment = AppointmentEntity.builder()
                .date(nextTuesday)
                .notes("test")
                .status("QUEUE")
                .specialization(specialization)
                .patient(this.patient)
                .build();
        this.appointmentRepository.saveAndFlush(appointment);

        var queryParam = "?specializationSlug=" + this.specialization.getSlug();
        queryParam += "&dateTime=" + nextTuesday.format(formatter);
        var url = "/appointments/available-time-intervals" + queryParam;

        var requestBuilder = MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON);

        var response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var expectedJson = TestUtils.objectToJson(
                AvailabilityOutputDTO.builder()
                        .availableTimes(Stream.of(8, 9, 10, 11, 12, 13, 14, 15, 16, 17).filter(time -> time != timeScheduled).toList())
                        .possibleTimes(List.of(8, 9, 10, 11, 12, 13, 14, 15, 16, 17))
                        .build());

        assertEquals(expectedJson, response.getResponse().getContentAsString());
    }
}
