package nl.multicode;

import nl.multicode.model.AutoGroupResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AutoGroupTest {

    private final AutoGroup autoGroup = new AutoGroup();

    @Test
    @DisplayName("Given a list of sentences with common substrings, when auto grouping is performed, then the sentences are grouped accordingly")
    void groupsSentencesWithCommonSubstring() {
        List<String> input = List.of(
                "Order created successfully",
                "Order created with warnings",
                "Payment failed",
                "Order created and paid"
        );

        AutoGroupResult result =
                autoGroup.autoGroupSentences(input, 5, 2);

        assertThat(result.hasGroups()).isTrue();
        assertThat(result.getGroups()).hasSize(1);

        Map.Entry<String, Map<Integer, String>> group =
                result.getGroups().entrySet().iterator().next();

        assertThat(group.getKey())
                .contains("Order created");

        assertThat(group.getValue())
                .hasSize(3)
                .containsValues(
                        "Order created successfully",
                        "Order created with warnings",
                        "Order created and paid"
                );

        assertThat(result.getUngrouped())
                .hasSize(1)
                .containsEntry(2, "Payment failed");
    }

    @Test
    @DisplayName("Given a list of sentences with no commonality, when auto grouping is performed, then all sentences remain ungrouped")
    void returnsAllUngroupedWhenNoCommonalityExists() {
        List<String> input = List.of(
                "Apple",
                "Banana",
                "Carrot"
        );

        AutoGroupResult result =
                autoGroup.autoGroupSentences(input, 4, 2);

        assertThat(result.hasGroups()).isFalse();
        assertThat(result.getGroups()).isEmpty();
        assertThat(result.getUngrouped()).hasSize(3);
    }

    @Test
    @DisplayName("Given an empty list of sentences, when auto grouping is performed, then an empty result is returned")
    void emptyInputReturnsEmptyResult() {
        AutoGroupResult result =
                autoGroup.autoGroupSentences(List.of(), 4, 2);

        assertThat(result.getGroups()).isEmpty();
        assertThat(result.getUngrouped()).isEmpty();
    }

    @Test
    @DisplayName("Given a list of sentences, when auto grouping is performed with a minimum group size, then groups smaller than the minimum are not created")
    void respectsMinimumGroupSize() {
        List<String> input = List.of(
                "System error occurred",
                "System rebooted",
                "User logged in"
        );

        AutoGroupResult result =
                autoGroup.autoGroupSentences(input, 6, 3);

        assertThat(result.hasGroups()).isFalse();
        assertThat(result.getGroups()).isEmpty();
        assertThat(result.getUngrouped()).hasSize(3);
    }
}
