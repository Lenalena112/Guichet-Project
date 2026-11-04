package dev.guichet.rdv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RendezVousController.class)
@Import(RendezVousRepository.class)
class RendezVousControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationClient notificationClient;

    @Test
    void listeLesRendezVousDeDemonstration() throws Exception {
        mockMvc.perform(get("/api/rendez-vous"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].demandeur").value("ines.martin"));
    }

    @Test
    void renvoie404PourUnRendezVousInconnu() throws Exception {
        mockMvc.perform(get("/api/rendez-vous/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void creeUnRendezVousEtNotifieLeDemandeur() throws Exception {
        mockMvc.perform(post("/api/rendez-vous")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"demandeur": "ines.martin", "conseiller": "maelle.le-goff",
                                 "debut": "2099-01-15T10:00:00", "objet": "Bilan"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.objet").value("Bilan"));

        verify(notificationClient).notifier(eq("ines.martin"), anyString());
    }

    @Test
    void refuseUnRendezVousDansLePasse() throws Exception {
        mockMvc.perform(post("/api/rendez-vous")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"demandeur": "ines.martin", "conseiller": "maelle.le-goff",
                                 "debut": "2000-01-15T10:00:00", "objet": "Bilan"}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(notificationClient);
    }
}
