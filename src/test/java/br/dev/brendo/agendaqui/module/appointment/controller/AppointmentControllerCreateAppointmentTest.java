package br.dev.brendo.agendaqui.module.appointment.controller;

import br.dev.brendo.agendaqui.module.appointment.repository.AppointmentRepository;
import br.dev.brendo.agendaqui.module.patient.entity.PatientEntity;
import br.dev.brendo.agendaqui.module.patient.repository.PatientRepository;
import br.dev.brendo.agendaqui.module.specialization.entity.SpecializationEntity;
import br.dev.brendo.agendaqui.module.specialization.entity.TimeIntervalEntity;
import br.dev.brendo.agendaqui.module.specialization.repository.SpecializationRepository;
import br.dev.brendo.agendaqui.module.specialization.repository.TimeIntervalRepository;
import br.dev.brendo.agendaqui.utils.TestUtils;
import com.jayway.jsonpath.JsonPath;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AppointmentControllerCreateAppointmentTest {
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

        var intervals = TimeIntervalEntity.builder().weekDay(DayOfWeek.TUESDAY.getValue()).timeStartInMinutes(60 * 8).timeEndInMinutes(60 * 9).enabled(true).specialization(specialization).build();
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
    public void tearDown() {
        this.appointmentRepository.deleteAll();
        this.patientRepository.deleteAll();
        this.timeIntervalRepository.deleteAll();
        this.specializationRepository.deleteAll();
        this.specializationRepository.deleteAll();
    }


    @Test
    public void should_create_an_appointment() throws Exception {
        var today = LocalDateTime.now().withHour(8).withSecond(0);
        var nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        record AppointmentDTO(String date, String notes, String specializationSlug, String patientId) {
        }

        var form = new AppointmentDTO(
                nextTuesday.format(formatter),
                "test",
                this.specialization.getSlug(),
                this.patient.getId()
        );

        var url = "/appointments";

        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(form));

        var response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String status = JsonPath.read(response.getResponse().getContentAsString(), "$.status");
        String note = JsonPath.read(response.getResponse().getContentAsString(), "$.notes");
        String date = JsonPath.read(response.getResponse().getContentAsString(), "$.date");

        assertEquals("QUEUE", status);
        assertEquals("test", note);
        assertEquals(nextTuesday.withMinute(0).format(formatter), date);
    }

    @Test
    public void should_throw_error_patient_not_found() throws Exception {
        var today = LocalDateTime.now().withHour(8).withSecond(0);
        var nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        record AppointmentDTO(String date, String notes, String specializationSlug, String patientId) {
        }

        var form = new AppointmentDTO(
                nextTuesday.format(formatter),
                "test",
                this.specialization.getSlug(),
                "9d714167-63ab-43bd-9a40-8b287267234f"
        );

        var url = "/appointments";

        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(form));

        var response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String message = JsonPath.read(response.getResponse().getContentAsString(), "$.message");

        assertEquals("Patient not found", message);
    }

    @Test
    public void should_throw_error_specialization_not_found() throws Exception {
        var today = LocalDateTime.now().withHour(8).withSecond(0);
        var nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        record AppointmentDTO(String date, String notes, String specializationSlug, String patientId) {
        }

        var form = new AppointmentDTO(
                nextTuesday.format(formatter),
                "test",
                "fake",
                "9d714167-63ab-43bd-9a40-8b287267234f"
        );

        var url = "/appointments";

        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(form));

        var response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String message = JsonPath.read(response.getResponse().getContentAsString(), "$.message");

        assertEquals("Specialization not found", message);
    }

    @Test
    public void should_throw_error_cannot_create_in_the_past() throws Exception {
        var today = LocalDateTime.now().withHour(8).withSecond(0);
        var nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        record AppointmentDTO(String date, String notes, String specializationSlug, String patientId) {
        }

        var form = new AppointmentDTO(
                nextTuesday.withYear(2000).format(formatter),
                "test",
                "fake",
                "9d714167-63ab-43bd-9a40-8b287267234f"
        );

        var url = "/appointments";

        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(form));

        var response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String message = JsonPath.read(response.getResponse().getContentAsString(), "$.message");

        assertEquals("Cannot create appointment in the past", message);
    }


    @Test
    public void should_throw_error_conflict() throws Exception {
        var today = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0);
        var nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        record AppointmentDTO(String date, String notes, String specializationSlug, String patientId) {
        }

        var form = new AppointmentDTO(
                nextTuesday.format(formatter),
                "test",
                this.specialization.getSlug(),
                patient.getId()
        );

        var url = "/appointments";

        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(form));

        mvc.perform(requestBuilder);

        var response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String message = JsonPath.read(response.getResponse().getContentAsString(), "$.message");

        assertEquals("There is another appointment in this date and time", message);
    }
}
