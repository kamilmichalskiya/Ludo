package pl.lodz.chinczyk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.lodz.chinczyk.game.model.dto.GameDTO;
import pl.lodz.chinczyk.pawn.model.Location;
import pl.lodz.chinczyk.pawn.model.dto.PawnDTO;
import pl.lodz.chinczyk.player.model.dto.PlayerDTO;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lodz.chinczyk.pawn.model.Color.GREEN;
import static pl.lodz.chinczyk.pawn.model.Location.B_0;
import static pl.lodz.chinczyk.pawn.model.Location.B_3;
import static pl.lodz.chinczyk.pawn.model.Location.G_0;
import static pl.lodz.chinczyk.pawn.model.Location.G_6;
import static pl.lodz.chinczyk.pawn.model.Location.R_0;
import static pl.lodz.chinczyk.pawn.model.Location.getStartLocation;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
class ChinczykApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Order(1)
    @Test
    void getAllGames() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/games"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<GameDTO> gameDTOList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, GameDTO.class));
        assertEquals(2, gameDTOList.size());
    }

    @Order(2)
    @Test
    void createNewGame() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/games/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        GameDTO gameDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GameDTO.class);
        assertTrue(gameDTO.getPlayers().isEmpty());
    }

    @Order(3)
    @Test
    void getGameInfo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/games/b2303e5a-eab6-4e9a-ad46-592075eeb675"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        GameDTO gameDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GameDTO.class);
        assertEquals(2, gameDTO.getPlayers().size());
    }

    @Order(4)
    @Test
    void cantStartGame() throws Exception {
        this.mockMvc.perform(put("/games/b2303e5a-eab6-4e9a-ad46-592075eeb676/start"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Order(5)
    @Test
    void rollDice() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/games/b2303e5a-eab6-4e9a-ad46-592075eeb675/player/2d96dac3-6934-44b9-bded-f7cb4c1b3867/dice"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<UUID> uuidList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, UUID.class));
        assertTrue(4 == uuidList.size() || 0 == uuidList.size());
    }

    @Order(6)
    @Test
    void joinGame() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put("/players/red/join/b2303e5a-eab6-4e9a-ad46-592075eeb676"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PlayerDTO playerDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PlayerDTO.class);
        assertEquals("red", playerDTO.getNick());
    }

    @Order(7)
    @Test
    void secondPlayerJoinGame() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put("/players/green/join/b2303e5a-eab6-4e9a-ad46-592075eeb676"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PlayerDTO playerDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PlayerDTO.class);
        assertEquals("green", playerDTO.getNick());
    }

    @Order(8)
    @Test
    void startGame() throws Exception {
        this.mockMvc.perform(put("/games/b2303e5a-eab6-4e9a-ad46-592075eeb676/start"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(9)
    @Test
    void cantMovePawn1() throws Exception {
        this.mockMvc.perform(put("/pawns/677c0b29-2daa-4da9-acf8-d737285f1b3f/move/1"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Order(10)
    @Test
    void cantMovePawn2() throws Exception {
        this.mockMvc.perform(put("/pawns/677c0b29-2daa-4da9-acf8-d737285f1b3f/move/2"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Order(11)
    @Test
    void cantMovePawn3() throws Exception {
        this.mockMvc.perform(put("/pawns/677c0b29-2daa-4da9-acf8-d737285f1b3f/move/3"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Order(12)
    @Test
    void cantMovePawn4() throws Exception {
        this.mockMvc.perform(put("/pawns/677c0b29-2daa-4da9-acf8-d737285f1b3f/move/4"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Order(13)
    @Test
    void cantMovePawn5() throws Exception {
        this.mockMvc.perform(put("/pawns/677c0b29-2daa-4da9-acf8-d737285f1b3f/move/5"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Order(14)
    @Test
    void movePawn() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put("/pawns/677c0b29-2daa-4da9-acf8-d737285f1b3f/move/6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PawnDTO pawnDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PawnDTO.class);
        assertEquals(getStartLocation(GREEN), pawnDTO.getLocation());
    }

    @Order(15)
    @Test
    void move2Pawn() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put("/pawns/677c0b29-2daa-4da9-acf8-d737285f1b3f/move/6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PawnDTO pawnDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PawnDTO.class);
        assertEquals(G_6, pawnDTO.getLocation());
    }

    @Order(16)
    @Test
    void move3Pawn() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put("/pawns/677c0b29-2daa-4da9-acf8-d737285f1b3f/move/4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PawnDTO pawnDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PawnDTO.class);
        assertEquals(B_0, pawnDTO.getLocation());
    }

    @Order(16)
    @Test
    void move4Pawn() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put("/pawns/f601cb42-3662-4a18-9168-10bf00a5cc94/move/6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PawnDTO pawnDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PawnDTO.class);
        assertEquals(R_0, pawnDTO.getLocation());
    }
}
