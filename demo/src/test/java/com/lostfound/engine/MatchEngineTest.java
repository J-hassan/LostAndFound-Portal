package com.lostfound.engine;

import com.lostfound.models.FoundItem;
import com.lostfound.models.Item;
import com.lostfound.models.LostItem;
import com.lostfound.storage.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for MatchEngine.
 *
 * NOTE: These tests require a live database connection because
 * MatchEngine.findMatches() reads items from DatabaseManager.getAllItems().
 * Remove or replace the @Disabled annotation when running against a real database.
 *
 * Scoring rules (score >= 70 required for a match):
 *   name match        : +30
 *   description match : +20
 *   location match    : +10
 *   category match    : +20
 *   date within 2 days: +20
 *
 * Items are excluded when:
 *   - same ID as the reported item
 *   - same reporter email
 *   - same type (Lost vs. Found must differ)
 */
@Disabled("Requires a live database connection: MatchEngine.findMatches() reads from DatabaseManager")
class MatchEngineTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2024, 6, 1);

    /** Track IDs of items inserted during each test so we can clean up afterwards. */
    private final List<String> insertedIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        insertedIds.clear();
    }

    @AfterEach
    void cleanUp() {
        for (String id : insertedIds) {
            DatabaseManager.deleteItem(id);
        }
    }

    // Helper to add an item via DatabaseManager and record its ID for cleanup
    private void saveItem(Item item) {
        DatabaseManager.saveItem(item);
        insertedIds.add(item.getId());
    }

    // =================== MATCH SCENARIOS ===================

    /**
     * Name(+30) + Category(+20) + Date(+20) = 70 → match (boundary).
     */
    @Test
    void match_whenScoreExactly70_itemIsIncluded() {
        // Candidate found item stored in FileManager
        FoundItem candidate = new FoundItem("Wallet", "WALLET", "unrelated description", "SomePlace",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        // Reported lost item (same name, same category, same date, different description/location)
        LostItem reported = new LostItem("Wallet", "WALLET", "completely different desc", "OtherPlace",
                "loser@uni.edu");
        reported.setDate(BASE_DATE);
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().anyMatch(m -> m.getId().equals(candidate.getId())),
                "Candidate should match (score = name30 + category20 + date20 = 70)");
    }

    /**
     * Full score: Name(+30) + Description(+20) + Location(+10) + Category(+20) + Date(+20) = 100 → match.
     */
    @Test
    void match_withAllFieldsMatching_scoreIs100() {
        FoundItem candidate = new FoundItem("Wallet", "WALLET", "brown leather wallet", "Library",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        LostItem reported = new LostItem("Wallet", "WALLET", "brown leather wallet", "Library",
                "loser@uni.edu");
        reported.setDate(BASE_DATE);
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().anyMatch(m -> m.getId().equals(candidate.getId())));
    }

    /**
     * Name(+30) + Description(+20) + Category(+20) = 70 → match.
     */
    @Test
    void match_nameDescriptionCategory_scoreIs70() {
        FoundItem candidate = new FoundItem("Keys", "KEYS", "house keys", "Main Hall",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        // Use a very old date so date score is 0, no location overlap
        LostItem reported = new LostItem("Keys", "KEYS", "house keys", "Other Building",
                "loser@uni.edu");
        reported.setDate(BASE_DATE.minusDays(30));
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().anyMatch(m -> m.getId().equals(candidate.getId())));
    }

    /**
     * Name(+30) + Description(+20) + Date(+20) = 70 → match.
     */
    @Test
    void match_nameDescriptionDate_scoreIs70() {
        FoundItem candidate = new FoundItem("Phone", "ELECTRONICS", "black smartphone", "Library",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        LostItem reported = new LostItem("Phone", "WALLET", "black smartphone", "Cafeteria",
                "loser@uni.edu");
        reported.setDate(BASE_DATE.plusDays(1));
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().anyMatch(m -> m.getId().equals(candidate.getId())));
    }

    /**
     * Location match partial contribution: Name(+30) + Location(+10) + Category(+20) + Date(+20) = 80.
     */
    @Test
    void match_nameLocationCategoryDate_scoreIs80() {
        FoundItem candidate = new FoundItem("Jacket", "CLOTHING", "desc A", "Library",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        LostItem reported = new LostItem("Jacket", "CLOTHING", "desc B", "Library",
                "loser@uni.edu");
        reported.setDate(BASE_DATE);
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().anyMatch(m -> m.getId().equals(candidate.getId())));
    }

    // =================== NO-MATCH SCENARIOS ===================

    /**
     * Name only (+30) → score 30 < 70, no match.
     */
    @Test
    void noMatch_nameOnly_scoreIs30() {
        FoundItem candidate = new FoundItem("Wallet", "ELECTRONICS", "different desc", "Far Away",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE.minusDays(30));
        saveItem(candidate);

        LostItem reported = new LostItem("Wallet", "CLOTHING", "unrelated desc", "Other Place",
                "loser@uni.edu");
        reported.setDate(BASE_DATE);
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().noneMatch(m -> m.getId().equals(candidate.getId())));
    }

    /**
     * Category + Date only (+20 + +20 = 40) → no match.
     */
    @Test
    void noMatch_categoryAndDate_scoreIs40() {
        FoundItem candidate = new FoundItem("Phone", "ELECTRONICS", "some phone", "Hall",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        LostItem reported = new LostItem("Laptop", "ELECTRONICS", "gaming laptop", "Building C",
                "loser@uni.edu");
        reported.setDate(BASE_DATE.plusDays(1));
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().noneMatch(m -> m.getId().equals(candidate.getId())));
    }

    // =================== EXCLUSION SCENARIOS ===================

    /**
     * Same ID → excluded regardless of score.
     */
    @Test
    void noMatch_sameId_excluded() {
        LostItem reported = new LostItem("Wallet", "WALLET", "brown wallet", "Library",
                "loser@uni.edu");
        reported.setDate(BASE_DATE);
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().noneMatch(m -> m.getId().equals(reported.getId())));
    }

    /**
     * Same reporter email → excluded.
     */
    @Test
    void noMatch_sameReporterEmail_excluded() {
        FoundItem candidate = new FoundItem("Wallet", "WALLET", "brown wallet", "Library",
                "same@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        LostItem reported = new LostItem("Wallet", "WALLET", "brown wallet", "Library",
                "same@uni.edu");
        reported.setDate(BASE_DATE);
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().noneMatch(m -> m.getId().equals(candidate.getId())));
    }

    /**
     * Same type (both Lost) → excluded.
     */
    @Test
    void noMatch_sameType_excluded() {
        LostItem candidate = new LostItem("Wallet", "WALLET", "brown wallet", "Library",
                "other@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        LostItem reported = new LostItem("Wallet", "WALLET", "brown wallet", "Library",
                "loser@uni.edu");
        reported.setDate(BASE_DATE);
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().noneMatch(m -> m.getId().equals(candidate.getId())));
    }

    // =================== DATE PROXIMITY SCORING ===================

    /**
     * Date exactly 2 days apart → still gets +20.
     */
    @Test
    void dateScore_exactly2DaysApart_earnsBonusPoints() {
        // Name(+30) + Category(+20) + Date within 2 days(+20) = 70 → match
        FoundItem candidate = new FoundItem("Wallet", "WALLET", "desc A", "SomePlace",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        LostItem reported = new LostItem("Wallet", "WALLET", "desc B", "OtherPlace",
                "loser@uni.edu");
        reported.setDate(BASE_DATE.plusDays(2));
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().anyMatch(m -> m.getId().equals(candidate.getId())));
    }

    /**
     * Date 3 days apart → no date bonus. Name(+30) + Category(+20) = 50 < 70 → no match.
     */
    @Test
    void dateScore_3DaysApart_noBonusPoints() {
        FoundItem candidate = new FoundItem("Wallet", "WALLET", "desc A", "SomePlace",
                "finder@uni.edu");
        candidate.setDate(BASE_DATE);
        saveItem(candidate);

        LostItem reported = new LostItem("Wallet", "WALLET", "desc B", "OtherPlace",
                "loser@uni.edu");
        reported.setDate(BASE_DATE.plusDays(3));
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().noneMatch(m -> m.getId().equals(candidate.getId())));
    }

    // =================== MULTIPLE MATCHES ===================

    /**
     * Multiple candidates in the store — only those with score >= 70 are returned.
     */
    @Test
    void multipleItems_onlyHighScoreOnesReturned() {
        // High score candidate: Name(+30) + Category(+20) + Date(+20) = 70
        FoundItem highScore = new FoundItem("Wallet", "WALLET", "other desc", "Other Loc",
                "finder1@uni.edu");
        highScore.setDate(BASE_DATE);
        saveItem(highScore);

        // Low score candidate: Category only = 20
        FoundItem lowScore = new FoundItem("Purse", "WALLET", "different", "Somewhere Else",
                "finder2@uni.edu");
        lowScore.setDate(BASE_DATE.minusDays(30));
        saveItem(lowScore);

        LostItem reported = new LostItem("Wallet", "WALLET", "brown wallet", "Library",
                "loser@uni.edu");
        reported.setDate(BASE_DATE);
        saveItem(reported);

        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.stream().anyMatch(m -> m.getId().equals(highScore.getId())),
                "High-score candidate should be in results");
        assertTrue(matches.stream().noneMatch(m -> m.getId().equals(lowScore.getId())),
                "Low-score candidate should not be in results");
    }

    /**
     * Empty store → empty result list.
     */
    @Test
    void findMatches_emptyStore_returnsEmptyList() {
        LostItem reported = new LostItem("Wallet", "WALLET", "brown wallet", "Library",
                "loser@uni.edu");
        // Do not add it to FileManager so getAllItems() is empty
        List<Item> matches = MatchEngine.findMatches(reported);
        assertTrue(matches.isEmpty());
    }
}
