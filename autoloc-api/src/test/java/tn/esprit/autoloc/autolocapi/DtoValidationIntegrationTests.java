package tn.esprit.autoloc.autolocapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DtoValidationIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void agencyListUsesAgencyDto() throws Exception {
        mockMvc.perform(get("/agences"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void vehicleCreationReturnsFieldValidationMessages() throws Exception {
        mockMvc.perform(post("/vehicules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"immatriculation":"", "marque":"", "modele":"", "categorie":null,
                                 "tarifJournalier":0, "statut":null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.immatriculation").isNotEmpty())
                .andExpect(jsonPath("$.fieldErrors.marque").isNotEmpty());
    }

    @Test
    void vehicleUpdateReturnsFieldValidationMessages() throws Exception {
        String validVehicle = """
                {"immatriculation":"TEST-DTO-01", "marque":"Toyota", "modele":"Yaris",
                 "categorie":"CITADINE", "tarifJournalier":100, "statut":"DISPONIBLE"}
                """;
        String created = mockMvc.perform(post("/vehicules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validVehicle))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("idVehicule").asLong();

        mockMvc.perform(put("/vehicules/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"immatriculation":"", "marque":"", "modele":"", "categorie":null,
                                 "tarifJournalier":0, "statut":null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.immatriculation").isNotEmpty());
    }
}
